SUMMARY = "SecretVM initramfs"
LICENSE = "MIT"

inherit core-image

PACKAGE_INSTALL = "base-files \
                   busybox \
                   jq \
                   secret-vm-initramfs-files"

PACKAGE_INSTALL:append:secret-vm-tdx = " attest-tool kernel-module-tdx-guest dstack-util"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
