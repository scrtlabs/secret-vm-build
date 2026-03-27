SUMMARY = "SecretVM initramfs"
LICENSE = "MIT"

inherit core-image

PACKAGE_INSTALL = "base-files \
                   jq \
                   busybox \
                   secret-vm-initramfs-files"

PACKAGE_INSTALL:append:secret-vm-tdx = " attest-tool dstack-util"
PACKAGE_INSTALL:append:secret-vm-gcp-tdx = " attest-tool-tsm dstack-util"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
