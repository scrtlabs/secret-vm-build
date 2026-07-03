# SLSA Build Provenance for SecretVM Releases — Design

**Date:** 2026-06-23
**Repo:** `scrtlabs/secret-vm-build`
**Status:** Approved design, pre-implementation

## Goal

Publish signed SLSA build provenance for every SecretVM release so that
downstream consumers can cryptographically verify that a given release
artifact (kernel, initramfs, OVMF firmware, rootfs image) was produced by
this repository's release workflow at a specific tag and commit.

## Background

Releases are produced by `.github/workflows/build.yaml`, triggered on
version tags (`v[0-9]+.[0-9]+.[0-9]+` and `-alpha/-beta/-patch/-rc`
variants). The job:

- Runs on the **self-hosted** runner `secret-vm-build-runner`.
- Runs `scripts/build_reproducible.sh` (Yocto/poky reproducible build).
- Renames per-target artifacts to include the version, then publishes them
  to a GitHub Release via `softprops/action-gh-release@v1` (`prerelease: true`).

The release contains 14 artifacts across three targets:

| Target    | Artifacts |
|-----------|-----------|
| `tdx`     | `rootfs-dev*.iso`, `rootfs-prod*.iso`, `rootfs-gpu-dev*.iso`, `rootfs-gpu-prod*.iso`, `ovmf*.fd`, `bzImage*`, `initramfs*.cpio.gz` |
| `sev`     | `rootfs-dev*.iso`, `rootfs-prod*.iso`, `ovmf*.fd`, `bzImage*`, `initramfs*.cpio.gz` |
| `gcp-tdx` | `rootfs-dev*.wic.tar.gz`, `rootfs-prod*.wic.tar.gz` |

Artifact sizes range from a few MB up to ~1 GB (`rootfs-gpu-*` ISOs).

## Mechanism

Use **`actions/attest-build-provenance`** (GitHub Artifact Attestations) —
the purpose-built action that GitHub's own SLSA-provenance documentation
uses. **Pin it to v2 or later — recommend the current major, `@v4`.** This
is not optional: the single-bundle behavior this design relies on changed by
version. In **v1**, multiple subjects produced one attestation *per subject*
written as JSON **Lines** (`.jsonl`); in **v2+ (incl. v4)**, multiple
subjects produce **one** attestation referencing all subjects, written as a
single **JSON** bundle. The `.sigstore.json` naming and the multi-subject
bundle premise below are valid only on v2+. (As of v4 this action is a thin
wrapper over `actions/attest` with `predicate-type: slsaprovenance`; GitHub
suggests new implementations may use `actions/attest` directly. Either is
acceptable; this design uses `attest-build-provenance` for clarity.) The
implementation plan confirms the exact current tag at implementation time.

For the supplied subjects the action:

1. Computes SHA-256 digests of each artifact.
2. Generates an in-toto **SLSA v1 provenance predicate** describing the
   build (repo, workflow, commit/tag, trigger, runner).
3. Signs it keylessly via Sigstore: the workflow's OIDC token is exchanged
   for a short-lived Fulcio certificate, and the signing event is recorded
   in the Rekor transparency log. Because this repo is **public**, signing
   uses the **public-good Sigstore** instance.
4. Stores the attestation in GitHub's attestation API for the repository,
   keyed by artifact digest.

### Verification (the command consumers must actually use)

A bare `gh attestation verify <file> --repo scrtlabs/secret-vm-build` checks
only that *some* attestation for this repo matches the digest — it would
also accept an attestation minted by the backfill workflow or any other
repo workflow with attestation permissions. The canonical command therefore
**constrains the signer workflow** (and may pin the source ref):

```
gh attestation verify <artifact-file> \
  --repo scrtlabs/secret-vm-build \
  --signer-workflow scrtlabs/secret-vm-build/.github/workflows/build.yaml
```

Optionally add `--source-ref refs/tags/<tag>` and/or
`--source-digest <commit-sha>` to pin a specific release.

> **`--signer-workflow` is matched as a regular expression** (cli/cli
> #9507), not a literal string — the `.` characters in
> `…/build.yaml` match any character, so the constraint is slightly looser
> than it looks. It is still strong enough to separate the build workflow
> from the backfill workflow (different filenames), which is the security
> boundary this design relies on. The implementation MUST verify that
> separation as a test gate (see Success criterion 4): a `build.yaml`
> signer-workflow check rejects a backfill attestation, and a
> `provenance-backfill.yaml` check rejects a build attestation.

Offline verification against the attached bundle still requires `--repo`
(or `--owner`):

```
gh attestation verify <artifact-file> \
  --repo scrtlabs/secret-vm-build \
  --bundle provenance-<version>.sigstore.json \
  --signer-workflow scrtlabs/secret-vm-build/.github/workflows/build.yaml
```

> **Offline-bundle reliability — load-bearing, test it:** offline
> verification works **one artifact at a time** — pass a single artifact
> file plus the bundle. Do **not** expect to verify all 14 artifacts in one
> command: `gh attestation verify` does not accept file globs (cli/cli
> #9215, open), and `--bundle` against a bundle holding multiple entries has
> open issues (#10059). With a v2+ single-attestation-many-subjects bundle,
> verifying one file should find its digest among the subjects — `gh`'s own
> `--bundle` help states it accepts "a single bundle in a JSON file," which
> is exactly this case, so it is expected to work. It is still **not
> guaranteed** across `gh` versions, so the implementation MUST treat
> Success criterion 3 as a real gate, run early, not a formality.
> **Fallback (contingency if the gate fails):** if per-file
> offline verification against the combined bundle fails, emit one
> attestation/bundle per artifact (loop `attest-build-provenance` per
> subject, equivalent to the v1 model) and attach per-artifact bundles.
> Online verification via GitHub's store is the primary, always-reliable
> path; the attached bundle is a convenience for air-gapped consumers.

## SLSA Level — honest statement

This design produces an **authenticated SLSA v1 build-provenance
attestation**: provenance exists, is generated by the build service, and is
signed by the build platform's OIDC identity and logged in Rekor. In SLSA
v1 Build-track terms this corresponds to **Build Level 2** (provenance
exists + is authenticated + service-generated).

It does **not** achieve **Build Level 3**. L3 requires a hardened, isolated
build platform whose provenance is *unforgeable* even against a malicious
build job. The build runs on the self-hosted `secret-vm-build-runner`,
which does not provide that isolation. Note also that for GitHub Actions
attestations only the signing certificate and timestamps are
non-manipulable; the predicate's contents are only as trustworthy as the
workflow that produced them. On a **self-hosted** runner the build job and
the runner share a trust boundary, which softens even the L2
"service-generated" guarantee (a compromised runner could influence what is
attested) — so treat this as L2 *at most*. We will document this honestly
and avoid the word "non-forgeable."

Reaching true L3 (e.g. moving the build into a GitHub-hosted trusted
context, or using `slsa-framework/slsa-github-generator`) is explicitly
**out of scope** for this work.

## Component 1 — Going-forward provenance (`build.yaml`)

Modify the existing `build` job:

1. **Permissions.** Add a job-level `permissions:` block. The workflow
   currently has **none**, so it inherits the repo default token
   permissions; adding a job-level block **narrows** the token to exactly
   what is listed and drops everything else to `none`.
   **Pre-flight check (do this before editing):** inspect the repo's default
   workflow token permission with
   `gh api repos/scrtlabs/secret-vm-build/actions/permissions/workflow`. If
   the default is already read-only, the current release step is succeeding
   via the step's own grant and narrowing is safe; if anything beyond these
   three scopes is in use, account for it. Confirm no existing step needs
   more than these three (the only privileged operations are the release
   upload and the attestation):
   - `id-token: write` — required for Sigstore OIDC signing.
   - `attestations: write` — required to write to the attestation store.
   - `contents: write` — required by the existing release step.
   - `artifact-metadata: write` — **required by current `actions/attest`
     versions** (newer than the older 3-scope provenance example). Whether
     it is needed depends on the pinned tag, and since the job-level block
     drops unlisted scopes to `none`, a missing required scope fails the
     attest step *after* the multi-hour Yocto build. **The implementation
     plan MUST read the pinned tag's own README (not `main`) and include
     exactly the scopes that tag requires** — do not treat this four-item
     list as final.

2. **Attestation step — runs BEFORE the release step.** After the
   artifact-renaming `Build` step and before
   `softprops/action-gh-release`, add a single
   `actions/attest-build-provenance` step whose `subject-path` lists all
   renamed artifacts. Running it first means a signing failure aborts the
   job *before* an unattested release is published.

   ```yaml
   - name: Attest build provenance
     id: attest
     uses: actions/attest-build-provenance@<current-major>
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
   ```

   A single invocation with multiple subjects produces one attestation
   covering all listed subjects, written to the file named by the action's
   `bundle-path` output.

   **Subject-list assumption:** the list is explicit (matching the existing
   workflow's explicit per-file style). This assumes every target is always
   built — true today, since `build_reproducible.sh` always produces all
   three. If a target is ever made conditional, a missing `subject-path`
   entry will fail the step; update this list accordingly. (Subject count is
   well under the action's 1024-subject limit.)

3. **Publish the bundle as a release asset.** The action does not document
   the extension of its `bundle-path` output, so do not assume it; add an
   **explicit `run:` step** between attest and release that copies
   `${{ steps.attest.outputs.bundle-path }}` to a stable, version-named
   `provenance-${VERSION}.sigstore.json` (a single v2+ bundle is JSON, so
   `.json`, not `.jsonl`). Then add that path to the `files:` list of the
   existing `softprops/action-gh-release` step so artifacts and provenance
   publish together. Note: `action-gh-release` may only *warn* (not fail) on
   a missing `files:` entry, so if the copy step is omitted the release
   could publish with no bundle and no hard error — the copy step is
   mandatory. (If the per-artifact fallback is taken, copy and attach each
   `provenance-<artifact>.sigstore.json` instead.)

### Runner prerequisites / risks

- The self-hosted runner needs outbound HTTPS to: GitHub APIs/uploads
  (`api.github.com`, `*.githubusercontent.com`, `uploads.github.com`) and,
  because this repo is public, the public-good Sigstore endpoints
  (`fulcio.sigstore.dev`, `rekor.sigstore.dev`, `tuf-repo-cdn.sigstore.dev`).
  (A private/internal repo would instead use GitHub's private Sigstore
  endpoints — not applicable here while the repo is public.)
- GitHub OIDC must function on the self-hosted runner (it does by default;
  no extra secret needed). If the runner is heavily network-restricted,
  this step could fail — validated on first real tag.
- Hashing ~1 GB ISOs adds modest time/CPU but no special requirements.

## Component 2 — Backfill workflow (`provenance-backfill.yaml`)

A separate, **manually-triggered** workflow (`workflow_dispatch`). A GitHub
Actions workflow is required (not a local shell script) because the
repo-bound attestation identity comes from the workflow's OIDC token; a
local script cannot mint a repo-scoped attestation.

- **Input:** `tags` — a single tag or comma/space-separated list (e.g.
  `v0.0.30` or `v0.0.30, v0.0.28`). No "attest everything" sweep; the
  operator chooses specific releases.
- **Permissions:** `id-token: write`, `attestations: write`,
  `contents: write`, plus `artifact-metadata: write` if the pinned action
  tag requires it (same caveat as Component 1 — match the pinned tag's
  README).
- **Runner:** GitHub-hosted (`ubuntu-latest`) is sufficient and simpler for
  backfill (no Yocto build needed — only download + hash + sign).
- **`gh` auth:** every step that calls `gh` must set
  `env: { GH_TOKEN: ${{ github.token }} }` — `permissions: contents: write`
  alone is not sufficient to authenticate the `gh` CLI.
- **Steps per tag:**
  1. Download the release's assets into a **clean, per-tag directory**
     (`gh release download <tag> --dir <dir>`), then **delete any
     provenance files** (`rm -f <dir>/*.sigstore.json <dir>/*.sigstore.jsonl`)
     before attesting, so a re-run does not attest a previously-uploaded
     bundle. (`gh release download` has `--pattern` include-globs but no
     exclude flag; download-all then remove is chosen as the more robust
     approach — it can't silently miss a newly-introduced artifact type.)
  2. Run `actions/attest-build-provenance` with `subject-path` globbing the
     remaining files in `<dir>`.
  3. Copy the bundle to `provenance-backfill-<tag>.sigstore.json` (a
     **distinct** name from the build workflow's `provenance-<tag>.sigstore.json`,
     so backfill can never `--clobber`/overwrite a stronger build-time bundle)
     and upload it to the release (`gh release upload <tag>
     provenance-backfill-<tag>.sigstore.json --clobber`). Note `--clobber` **deletes the existing same-named asset
     before** uploading the new one (cli/cli #8822); if the upload then
     fails the prior bundle is gone, but the workflow is idempotent so a
     re-run restores it.

### Backfill caveat (documented)

Backfill provenance attests only that "this repo's backfill workflow
observed these exact artifact digests on date X." It **cannot** prove how
the artifacts were originally built, because they were produced outside the
attested workflow. It attests the digests of the release-asset bytes **as
downloaded now** — if an asset were ever re-uploaded or re-encoded, the
attested digest would track the current bytes, not an earlier copy. It is
effectively signed hash-notarization, weaker than
genuine build provenance. Its signer workflow is `provenance-backfill.yaml`,
*not* `build.yaml`, so a `--signer-workflow .../build.yaml` verification
will (correctly) reject it — consumers verifying backfilled releases must
target the backfill workflow explicitly. This distinction will be stated in
both the workflow file (comment) and the README.

## Component 3 — Documentation (README)

Add a "Build provenance / verifying releases" section to `README.md`:

- The canonical verification command **including `--signer-workflow`**
  (online) and the `--bundle` form (offline), with `--source-ref`/
  `--source-digest` pinning shown as options.
- The minimum `gh` CLI version required for `gh attestation verify`:
  base commands work on `gh >= 2.60`; the optional `--source-ref`/
  `--source-digest` pinning flags require `gh >= 2.68`.
- The honest SLSA **Build Level 2** statement and why (self-hosted runner;
  predicate trust caveat).
- The backfill caveat, including that backfilled releases verify against
  the backfill workflow, not `build.yaml`.

## Out of scope

- Achieving SLSA Build Level 3.
- Migrating the build off the self-hosted runner.
- Reproducible-build verification tooling beyond what already exists.
- Automatic backfill of all historical releases (manual, per-tag only).

## Success criteria

1. A newly-tagged release automatically has a verifiable attestation in
   GitHub's store and a `provenance-<version>.sigstore.json` asset.
2. `gh attestation verify <artifact> --repo scrtlabs/secret-vm-build
   --signer-workflow scrtlabs/secret-vm-build/.github/workflows/build.yaml`
   succeeds for each artifact of a new release and reports the correct
   workflow/commit/tag.
3. **Offline-verify test gate:** `gh attestation verify <artifact> --repo
   scrtlabs/secret-vm-build --bundle <attached-bundle> --signer-workflow ...`
   succeeds for the artifacts against the attached bundle. If it fails for
   the single multi-subject bundle, the per-artifact-bundle fallback is
   implemented and this criterion is met with per-artifact bundles.
4. The backfill workflow, run with a specific tag, produces an attestation
   and bundle asset that verify against the backfill workflow's signer
   identity for that existing release. **Cross-workflow rejection holds:** a
   `--signer-workflow .../build.yaml` check rejects a backfill attestation,
   and a `--signer-workflow .../provenance-backfill.yaml` check rejects a
   build attestation.
5. README documents the constrained verification commands, the `gh` version
   floor, the L2 statement, and the backfill caveat.
