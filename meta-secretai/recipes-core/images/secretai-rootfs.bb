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
                 nvidia-container-toolkit \
                 kernel-module-video"

IMAGE_FSTYPES = "cpio"

ROOTFS_POSTPROCESS_COMMAND += "symlink_lib64;"
ROOTFS_POSTPROCESS_COMMAND += "remove_sysvinit_files;"

remove_sysvinit_files() {
    # Remove /etc/init.d directory and its contents
    rm -rf ${IMAGE_ROOTFS}${sysconfdir}/init.d

    # Remove /etc/rc*.d directories and their contents
    for d in ${IMAGE_ROOTFS}${sysconfdir}/rc*.d; do
        rm -rf $d
    done

    # Remove other sysvinit specific files
    rm -f ${IMAGE_ROOTFS}${sysconfdir}/inittab
}

symlink_lib64() {
    ln -s lib ${IMAGE_ROOTFS}/lib64
}
