SUMMARY = "Minimal initramfs"
LICENSE = "MIT"

inherit core-image

PACKAGE_INSTALL = "base-files \
                   busybox \
                   attest-tool \
                   kernel-module-tdx-guest \
                   secretai-initramfs-files"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
