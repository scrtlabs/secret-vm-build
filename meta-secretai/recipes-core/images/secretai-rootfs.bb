SUMMARY = "Minimal Root Filesystem"
LICENSE = "MIT"

inherit core-image

IMAGE_INSTALL = "base-files \
                 attest-tool \
                 crypt-tool \
                 busybox \
                 systemd \
                 openssh \
                 docker-compose \
                 acpid \
                 nvidia \
                 nvidia-firmware \
                 nvidia-persistenced \
                 kernel-module-video"

IMAGE_FSTYPES = "cpio"

ROOTFS_POSTPROCESS_COMMAND += "symlink_lib64;"

symlink_lib64() {
    ln -s lib ${IMAGE_ROOTFS}/lib64
}
