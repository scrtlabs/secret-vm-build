SUMMARY = "SecretVM initramfs"
LICENSE = "MIT"

inherit core-image

PACKAGE_INSTALL = "base-files \
                   busybox \
                   secret-vm-initramfs-files"

PACKAGE_INSTALL:append:secret-vm-tdx = " attest-tool dstack-util jq"
PACKAGE_INSTALL:append:secret-vm-gcp-tdx = " attest-tool dstack-util jq"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
