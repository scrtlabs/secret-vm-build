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

The release contains ~13 artifacts across three targets:

| Target    | Artifacts |
|-----------|-----------|
| `tdx`     | `rootfs-dev*.iso`, `rootfs-prod*.iso`, `rootfs-gpu-dev*.iso`, `rootfs-gpu-prod*.iso`, `ovmf*.fd`, `bzImage*`, `initramfs*.cpio.gz` |
| `sev`     | `rootfs-dev*.iso`, `rootfs-prod*.iso`, `ovmf*.fd`, `bzImage*`, `initramfs*.cpio.gz` |
| `gcp-tdx` | `rootfs-dev*.wic.tar.gz`, `rootfs-prod*.wic.tar.gz` |

Artifact sizes range from a few MB up to ~1 GB (`rootfs-gpu-*` ISOs).

## Mechanism

Use **`actions/attest-build-provenance@v2`** (GitHub Artifact Attestations).

For the supplied subjects it:

1. Computes SHA-256 digests of each artifact.
2. Generates an in-toto **SLSA v1 provenance predicate** describing the
   build (repo, workflow, commit/tag, trigger, runner).
3. Signs it keylessly via Sigstore: the workflow's OIDC token is exchanged
   for a short-lived Fulcio certificate, and the signature is recorded in
   the Rekor transparency log.
4. Stores the attestation in GitHub's attestation API for the repository,
   keyed by artifact digest.

**Verification** by any consumer:

```
gh attestation verify <artifact-file> --repo scrtlabs/secret-vm-build
```

This confirms the artifact's digest matches an attestation produced by this
repo's workflow, and prints the originating workflow, commit, and tag.
Offline verification is possible against the bundle file (see below) using
`gh attestation verify <artifact> --bundle <bundle.sigstore.jsonl>`.

## SLSA Level — honest statement

This design achieves **SLSA Build Level 2**: provenance exists, is
authenticated, and is non-forgeable (signed by the build platform's
identity, logged in Rekor).

It does **not** achieve **Build Level 3**, which requires a hardened,
isolated build platform. The build runs on the self-hosted
`secret-vm-build-runner`, which does not provide the build-isolation
guarantees L3 requires. This limitation will be documented explicitly
rather than overclaimed.

Reaching true L3 (e.g. moving the build into a GitHub-hosted trusted
context, or using `slsa-framework/slsa-github-generator`) is explicitly
**out of scope** for this work.

## Component 1 — Going-forward provenance (`build.yaml`)

Modify the existing `build` job:

1. **Permissions.** Add to the job (or workflow) `permissions:`:
   - `id-token: write` — required for Sigstore OIDC signing.
   - `attestations: write` — required to write to the attestation store.
   - `contents: write` — required by the existing release step (make explicit).

2. **Attestation step.** After the artifact-renaming `Build` step and
   before (or after) the release step, add a single
   `actions/attest-build-provenance@v2` step whose `subject-path` globs all
   renamed artifacts:

   ```yaml
   - name: Attest build provenance
     id: attest
     uses: actions/attest-build-provenance@v2
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
   bundle covering all listed artifacts. The action exposes the written
   bundle file path via its `bundle-path` output.

3. **Publish the bundle as a release asset.** Copy the bundle produced by
   the step (`${{ steps.attest.outputs.bundle-path }}`) to a stable,
   version-named filename and add it to the `files:` list of the existing
   `softprops/action-gh-release@v1` step, e.g.
   `provenance-${VERSION}.sigstore.jsonl`. This makes provenance visibly
   "published on the release" and enables offline verification.

### Runner prerequisites / risks

- The self-hosted runner must have outbound network access to GitHub's
  attestation API, Fulcio, and Rekor (`fulcio.sigstore.dev`,
  `rekor.sigstore.dev`, `*.githubusercontent.com`).
- GitHub OIDC must function on the self-hosted runner (it does by default;
  no extra secret needed). If the runner is heavily network-restricted,
  this step could fail — to be validated on first real tag.
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
  `contents: write`.
- **Runner:** GitHub-hosted (`ubuntu-latest`) is sufficient and simpler for
  backfill (no Yocto build needed — only download + hash + sign).
- **Steps per tag:**
  1. Download all assets for that release (`gh release download <tag>`),
     **excluding** any previously-uploaded `*.sigstore.jsonl` provenance
     file to avoid attesting prior provenance.
  2. Run `actions/attest-build-provenance@v2` with `subject-path` globbing
     the downloaded files.
  3. Upload the resulting bundle to the release as
     `provenance-<tag>.sigstore.jsonl` (`gh release upload <tag> --clobber`).

### Backfill caveat (documented)

Backfill provenance attests only that "this repo's backfill workflow
observed these exact artifact digests on date X." It **cannot** prove how
the artifacts were originally built, because they were produced outside the
attested workflow. It is effectively signed hash-notarization, weaker than
genuine build provenance. This distinction will be stated in both the
workflow file (comment) and the README.

## Component 3 — Documentation (README)

Add a "Build provenance / verifying releases" section to `README.md`:

- How to install/use `gh attestation verify` (online) and
  `--bundle` (offline) against a downloaded artifact.
- The honest SLSA **Build Level 2** statement and why (self-hosted runner).
- The backfill caveat for provenance on releases created before this change.

## Out of scope

- Achieving SLSA Build Level 3.
- Migrating the build off the self-hosted runner.
- Reproducible-build verification tooling beyond what already exists.
- Automatic backfill of all historical releases (manual, per-tag only).

## Success criteria

1. A newly-tagged release automatically has a verifiable attestation in
   GitHub's store and a `provenance-<version>.sigstore.jsonl` asset.
2. `gh attestation verify <artifact> --repo scrtlabs/secret-vm-build`
   succeeds for each artifact of a new release and reports the correct
   workflow/commit/tag.
3. The backfill workflow, run with a specific tag, produces a verifiable
   attestation and bundle asset for that existing release.
4. README documents verification, the L2 statement, and the backfill caveat.
