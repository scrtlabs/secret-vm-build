# SLSA Build Provenance Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Publish signed, verifiable SLSA v1 build-provenance attestations for every SecretVM release (going forward, automatically) and provide a manually-triggered workflow to backfill provenance for specific existing releases.

**Architecture:** Add `actions/attest-build-provenance@v4` to the existing tag-triggered `build.yaml` so each release's artifacts get a single attestation (stored in GitHub's attestation API) plus a `.sigstore.json` bundle attached as a release asset. Add a separate `workflow_dispatch` workflow that, per chosen tag, downloads the release assets, attests them, and uploads the bundle. Document verification (with `--signer-workflow`) and the honest SLSA-Level-2 limitation in the README.

**Tech Stack:** GitHub Actions, `actions/attest-build-provenance@v4` (wraps `actions/attest@v4.1.0`), Sigstore (public-good instance — repo is public), GitHub CLI (`gh attestation` / `gh release`). Validation locally via `python3` (YAML parse) and `actionlint` via Docker.

## Global Constraints

These apply to **every** task; copied verbatim from the design spec (`docs/superpowers/specs/2026-06-23-slsa-build-provenance-design.md`):

- **Action version:** pin `actions/attest-build-provenance@v4` (current major; latest v4.1.0). v2+ is mandatory — v1 emits per-subject `.jsonl`; v2+ emits one attestation referencing all subjects as a single `.json` bundle. The `.sigstore.json` naming below depends on v2+.
- **Required permissions for any job that runs the attest action:** `id-token: write`, `attestations: write`, `artifact-metadata: write` (all three are required by v4). Add `contents: write` where the job also creates/uploads a release. A job-level `permissions:` block drops every unlisted scope to `none`.
- **Bundle naming:** single combined bundle → `provenance-<version-or-tag>.sigstore.json` (`.json`, not `.jsonl`).
- **Verification must constrain the signer workflow.** Canonical command:
  `gh attestation verify <artifact> --repo scrtlabs/secret-vm-build --signer-workflow scrtlabs/secret-vm-build/.github/workflows/build.yaml`. `--signer-workflow` is matched as a **regex** (cli/cli #9507).
- **`gh` floor for verification:** recommend `gh >= 2.60` (confirm against the exact commands).
- **SLSA level claimed:** Build Level 2 at most (self-hosted runner blocks L3; predicate trust caveat). Never use the word "non-forgeable."
- **Every `gh`-calling step must set `env: GH_TOKEN: ${{ github.token }}`.**
- **Avoid script injection:** pass `${{ inputs.* }}` and `${{ matrix.* }}` into `run:` steps via `env:`, never interpolate them directly into the script body.

---

### Task 1: Add build provenance to the release workflow (`build.yaml`)

Adds attestation + bundle publication to the existing tag-triggered build. Going-forward provenance (spec Component 1).

**Files:**
- Modify: `.github/workflows/build.yaml`

**Interfaces:**
- Consumes: existing job `build`, step id `get_version` (output `VERSION`), the renamed artifacts under `artifacts/{tdx,sev,gcp-tdx}/`, and the existing `softprops/action-gh-release@v1` step.
- Produces: a release attestation in GitHub's store; a release asset `provenance-<VERSION>.sigstore.json`. The signer-workflow identity for these attestations is `.github/workflows/build.yaml` (relied on by Task 3 docs and Task 2's separation).

- [ ] **Step 1: Pre-flight — record the repo's current default token permissions**

Run:
```bash
gh api repos/scrtlabs/secret-vm-build/actions/permissions/workflow
```
Expected: JSON like `{"default_workflow_permissions":"read"|"write","can_approve_pull_request_reviews":...}`. Note the value. If `write`, the new job-level block narrows scope (safe — the release step's needs are covered by `contents: write`); if `read`, the existing release step already works under a narrower grant and narrowing is still safe. This is a read-only check; record the result in the commit message body.

- [ ] **Step 2: Add the job-level `permissions:` block**

In `.github/workflows/build.yaml`, the `build` job currently begins:
```yaml
jobs:
  build:
    runs-on: secret-vm-build-runner
    steps:
```
Insert a `permissions:` block between `runs-on:` and `steps:`:
```yaml
jobs:
  build:
    runs-on: secret-vm-build-runner
    permissions:
      id-token: write          # mint OIDC token for Sigstore signing
      attestations: write      # write to the attestation store
      artifact-metadata: write # required by actions/attest@v4
      contents: write          # required by the existing release upload
    steps:
```

- [ ] **Step 3: Add the attest step and bundle-staging step immediately before the `Release` step**

Find the final step (it is `- name: Release` using `softprops/action-gh-release@v1`). Insert these two steps **directly before** it (so a signing failure aborts before publishing):
```yaml
      - name: Attest build provenance
        id: attest
        uses: actions/attest-build-provenance@v4
        with:
          subject-path: |
            artifacts/tdx/rootfs-dev-${{ steps.get_version.outputs.VERSION }}-tdx.iso
            artifacts/tdx/rootfs-prod-${{ steps.get_version.outputs.VERSION }}-tdx.iso
            artifacts/tdx/rootfs-gpu-dev-${{ steps.get_version.outputs.VERSION }}-tdx.iso
            artifacts/tdx/rootfs-gpu-prod-${{ steps.get_version.outputs.VERSION }}-tdx.iso
            artifacts/tdx/ovmf-${{ steps.get_version.outputs.VERSION }}-tdx.fd
            artifacts/tdx/bzImage-${{ steps.get_version.outputs.VERSION }}-tdx
            artifacts/tdx/initramfs-${{ steps.get_version.outputs.VERSION }}-tdx.cpio.gz
            artifacts/sev/rootfs-dev-${{ steps.get_version.outputs.VERSION }}-sev.iso
            artifacts/sev/rootfs-prod-${{ steps.get_version.outputs.VERSION }}-sev.iso
            artifacts/sev/ovmf-${{ steps.get_version.outputs.VERSION }}-sev.fd
            artifacts/sev/bzImage-${{ steps.get_version.outputs.VERSION }}-sev
            artifacts/sev/initramfs-${{ steps.get_version.outputs.VERSION }}-sev.cpio.gz
            artifacts/gcp-tdx/rootfs-dev-${{ steps.get_version.outputs.VERSION }}-gcp-tdx.wic.tar.gz
            artifacts/gcp-tdx/rootfs-prod-${{ steps.get_version.outputs.VERSION }}-gcp-tdx.wic.tar.gz
      - name: Stage provenance bundle
        env:
          BUNDLE_PATH: ${{ steps.attest.outputs.bundle-path }}
          VERSION: ${{ steps.get_version.outputs.VERSION }}
        run: cp "$BUNDLE_PATH" "artifacts/provenance-${VERSION}.sigstore.json"
```

- [ ] **Step 4: Add the bundle to the `Release` step's `files:` list**

The existing `Release` step ends with a `files: |` block listing the 13 artifacts. Append one line to that block (keep existing entries):
```yaml
            artifacts/provenance-${{ steps.get_version.outputs.VERSION }}.sigstore.json
```

- [ ] **Step 5: Validate YAML parses**

Run:
```bash
python3 -c "import yaml,sys; yaml.safe_load(open('.github/workflows/build.yaml')); print('YAML OK')"
```
Expected: `YAML OK` (no traceback).

- [ ] **Step 6: Lint the workflow with actionlint (via Docker)**

Run:
```bash
docker run --rm -v "$PWD:/repo" -w /repo rhysd/actionlint:latest -color .github/workflows/build.yaml
```
Expected: no output and exit code 0. (If actionlint flags `artifact-metadata` as an unknown permission, that is a stale actionlint ruleset, not a real error — confirm the scope name against https://github.com/actions/attest README and proceed; note it in the commit body.)

- [ ] **Step 7: Commit**

```bash
git add .github/workflows/build.yaml
git commit -m "ci: attest SLSA build provenance and publish bundle on release

Default workflow token permission before change: <read|write from Step 1>."
```

---

### Task 2: Backfill workflow for specific existing releases (`provenance-backfill.yaml`)

Manually-triggered, per-tag provenance for already-published releases (spec Component 2). Uses a matrix because the attest action is a step, not a shell call, so it cannot run inside a bash loop — one matrix leg per tag.

**Files:**
- Create: `.github/workflows/provenance-backfill.yaml`

**Interfaces:**
- Consumes: existing GitHub releases by tag; `github.token`.
- Produces: per existing release, an attestation in GitHub's store and a `provenance-<tag>.sigstore.json` asset. Signer-workflow identity is `.github/workflows/provenance-backfill.yaml` — deliberately distinct from `build.yaml` so the two verify separately (Task 3 / spec Success criterion 4).

- [ ] **Step 1: Create the workflow file**

Create `.github/workflows/provenance-backfill.yaml` with exactly:
```yaml
name: Backfill Provenance

# Manually-triggered backfill of SLSA provenance for EXISTING releases.
# CAVEAT: this attests the digests of the release-asset bytes AS DOWNLOADED NOW.
# It cannot prove how the artifacts were originally built (they were produced
# outside this workflow) — it is signed hash-notarization, weaker than the
# build-time provenance produced by build.yaml. Backfilled attestations are
# signed by THIS workflow, so verify them with
#   --signer-workflow scrtlabs/secret-vm-build/.github/workflows/provenance-backfill.yaml
# (a build.yaml signer-workflow check will correctly reject them).

on:
  workflow_dispatch:
    inputs:
      tags:
        description: "Release tag(s) to attest: single, or comma/space-separated (e.g. 'v0.0.30' or 'v0.0.30, v0.0.28')"
        required: true
        type: string

permissions: {}

jobs:
  parse:
    runs-on: ubuntu-latest
    outputs:
      tags: ${{ steps.parse.outputs.tags }}
    steps:
      - name: Parse tags input into a JSON array
        id: parse
        env:
          TAGS_INPUT: ${{ inputs.tags }}
        run: |
          set -euo pipefail
          json=$(printf '%s' "$TAGS_INPUT" | tr ',' ' ' | xargs -n1 | jq -R . | jq -cs .)
          if [ "$json" = "[]" ]; then
            echo "No tags parsed from input '$TAGS_INPUT'" >&2
            exit 1
          fi
          echo "tags=$json" >> "$GITHUB_OUTPUT"
          echo "Parsed tags: $json"

  backfill:
    needs: parse
    runs-on: ubuntu-latest
    permissions:
      id-token: write
      attestations: write
      artifact-metadata: write
      contents: write
    strategy:
      fail-fast: false
      matrix:
        tag: ${{ fromJSON(needs.parse.outputs.tags) }}
    env:
      GH_TOKEN: ${{ github.token }}
    steps:
      - name: Download release assets into a clean directory
        env:
          TAG: ${{ matrix.tag }}
        run: |
          set -euo pipefail
          dir="assets/${TAG}"
          rm -rf "$dir"; mkdir -p "$dir"
          gh release download "$TAG" --repo "$GITHUB_REPOSITORY" --dir "$dir"
          # Drop any previously-uploaded provenance so we never attest a prior bundle
          rm -f "$dir"/*.sigstore.json "$dir"/*.sigstore.jsonl
          echo "Subjects to attest:"; ls -la "$dir"
      - name: Attest build provenance
        id: attest
        uses: actions/attest-build-provenance@v4
        with:
          subject-path: "assets/${{ matrix.tag }}/*"
      - name: Stage and upload provenance bundle
        env:
          TAG: ${{ matrix.tag }}
          BUNDLE_PATH: ${{ steps.attest.outputs.bundle-path }}
        run: |
          set -euo pipefail
          out="provenance-${TAG}.sigstore.json"
          cp "$BUNDLE_PATH" "$out"
          gh release upload "$TAG" "$out" --repo "$GITHUB_REPOSITORY" --clobber
```

- [ ] **Step 2: Validate YAML parses**

Run:
```bash
python3 -c "import yaml,sys; yaml.safe_load(open('.github/workflows/provenance-backfill.yaml')); print('YAML OK')"
```
Expected: `YAML OK`.

- [ ] **Step 3: Lint with actionlint (via Docker)**

Run:
```bash
docker run --rm -v "$PWD:/repo" -w /repo rhysd/actionlint:latest -color .github/workflows/provenance-backfill.yaml
```
Expected: exit code 0. (Same `artifact-metadata` caveat as Task 1, Step 6.)

- [ ] **Step 4: Sanity-check the tag-parsing logic locally**

Run (replicates the parse step's shell):
```bash
TAGS_INPUT="v0.0.30, v0.0.28 v0.0.29"
printf '%s' "$TAGS_INPUT" | tr ',' ' ' | xargs -n1 | jq -R . | jq -cs .
```
Expected: `["v0.0.30","v0.0.28","v0.0.29"]`.

- [ ] **Step 5: Commit**

```bash
git add .github/workflows/provenance-backfill.yaml
git commit -m "ci: add manual backfill workflow for release provenance"
```

---

### Task 3: Document verification in the README

Spec Component 3. Adds a verification section with the constrained commands, `gh` floor, the honest L2 statement, and the backfill caveat.

**Files:**
- Modify: `README.md`

**Interfaces:**
- Consumes: the release assets and signer-workflow identities produced by Tasks 1 and 2.
- Produces: user-facing verification docs. No downstream task depends on this.

- [ ] **Step 1: Append the provenance section to `README.md`**

Add this section at the end of `README.md` (keep existing content above it):
```markdown
## Build provenance — verifying releases

Each release published by `.github/workflows/build.yaml` carries a signed
**SLSA v1 build-provenance attestation**. It is stored in GitHub's
attestation API and also attached to the release as
`provenance-<version>.sigstore.json`.

Verification requires the GitHub CLI (`gh >= 2.60`). Verify an artifact you
downloaded from a release, constraining the signer to the build workflow:

```bash
gh attestation verify <artifact-file> \
  --repo scrtlabs/secret-vm-build \
  --signer-workflow scrtlabs/secret-vm-build/.github/workflows/build.yaml
```

To pin a specific release, add `--source-ref refs/tags/<tag>` and/or
`--source-digest <commit-sha>`.

Offline / air-gapped verification, one artifact at a time, against the
attached bundle:

```bash
gh attestation verify <artifact-file> \
  --repo scrtlabs/secret-vm-build \
  --bundle provenance-<version>.sigstore.json \
  --signer-workflow scrtlabs/secret-vm-build/.github/workflows/build.yaml
```

> `--signer-workflow` is matched as a regular expression, and
> `gh attestation verify` operates on one file at a time (it does not accept
> file globs).

### SLSA level

This provides **SLSA Build Level 2** at most: the provenance exists, is
generated by the build service, and is signed by the build platform's
identity (logged in Sigstore's transparency log). It does **not** reach
Level 3 — the build runs on a self-hosted runner, which shares a trust
boundary with the build job, so the provenance is authenticated but not
isolated/unforgeable. Only the signing certificate and timestamps are
non-manipulable; the predicate's contents are as trustworthy as the
workflow that produced them.

### Backfilled releases

Releases published before provenance was added can be attested with the
manual `Backfill Provenance` workflow. Backfill attests the digests of the
release-asset bytes **as they exist at backfill time** — it cannot prove how
those artifacts were originally built. Backfilled attestations are signed by
`provenance-backfill.yaml`, so verify them with:

```bash
gh attestation verify <artifact-file> \
  --repo scrtlabs/secret-vm-build \
  --signer-workflow scrtlabs/secret-vm-build/.github/workflows/provenance-backfill.yaml
```
```

- [ ] **Step 2: Verify the Markdown renders (no broken fences)**

Run:
```bash
python3 - <<'PY'
t = open('README.md').read()
assert t.count('```') % 2 == 0, "unbalanced code fences"
assert 'gh attestation verify' in t
print("README OK; fences balanced")
PY
```
Expected: `README OK; fences balanced`.

- [ ] **Step 3: Commit**

```bash
git add README.md
git commit -m "docs: document release provenance verification and SLSA level"
```

---

### Task 4: Functional validation gate (requires maintainer action — NOT auto-executed)

The only true end-to-end test pushes a real tag (triggering a multi-hour build on the self-hosted runner) or dispatches the backfill workflow against a live release. Both publish to production, so per project policy they require explicit maintainer consent and are performed by the maintainer, not an implementing subagent. This task documents the acceptance checks; do not run them without the user's go-ahead.

**Files:** none (validation only).

- [ ] **Step 1: (Going-forward) After the next real tagged release, verify each artifact**

For a downloaded artifact from the new release:
```bash
gh attestation verify <artifact> \
  --repo scrtlabs/secret-vm-build \
  --signer-workflow scrtlabs/secret-vm-build/.github/workflows/build.yaml
```
Expected: `✓ Verification succeeded!` reporting the build workflow, tag, and commit. (Spec success criteria 1–2.)

- [ ] **Step 2: (Offline gate) Verify against the attached bundle, per artifact**

```bash
gh release download <tag> --repo scrtlabs/secret-vm-build --pattern 'provenance-*.sigstore.json'
gh attestation verify <artifact> \
  --repo scrtlabs/secret-vm-build \
  --bundle provenance-<tag>.sigstore.json \
  --signer-workflow scrtlabs/secret-vm-build/.github/workflows/build.yaml
```
Expected: success. **If this fails** for the single multi-subject bundle, implement the per-artifact-bundle fallback (spec Mechanism note): loop the attest action per subject and attach one `provenance-<artifact>.sigstore.json` each. (Spec success criterion 3.)

- [ ] **Step 3: (Backfill) Dispatch the backfill workflow for one tag and verify**

```bash
gh workflow run "Backfill Provenance" --repo scrtlabs/secret-vm-build -f tags=v0.0.30
# after it completes:
gh attestation verify <artifact-from-v0.0.30> \
  --repo scrtlabs/secret-vm-build \
  --signer-workflow scrtlabs/secret-vm-build/.github/workflows/provenance-backfill.yaml
```
Expected: success against the backfill signer-workflow.

- [ ] **Step 4: (Cross-workflow rejection) Confirm the signer boundary holds**

```bash
# A build-workflow artifact must FAIL a backfill signer-workflow check, and vice versa:
gh attestation verify <build-release-artifact> \
  --repo scrtlabs/secret-vm-build \
  --signer-workflow scrtlabs/secret-vm-build/.github/workflows/provenance-backfill.yaml
```
Expected: verification **fails** (no matching attestation), confirming the two workflows' provenance are distinguishable. (Spec success criterion 4.)
