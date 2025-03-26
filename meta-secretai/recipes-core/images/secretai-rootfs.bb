SUMMARY = "Minimal Root Filesystem"
LICENSE = "MIT"

inherit core-image

IMAGE_INSTALL = "base-files \
                 attest-tool \
                 crypt-tool \
                 busybox \
                 systemd \
                 openssh \
                 caddy \
                 user-config \
                 secret-vm-scripts\
                 docker-compose \
                 python3 \
                 python3-nv-attestation-sdk \
                 python3-nv-local-gpu-verifier \
                 python3-nvidia-ml-py \
                 python3-signxml \
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
