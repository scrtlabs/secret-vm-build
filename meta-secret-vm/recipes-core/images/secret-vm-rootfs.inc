SUMMARY = "SecretVM Root Filesystem"
LICENSE = "MIT"

inherit core-image

IMAGE_INSTALL = "base-files \
                 busybox \
                 systemd \
                 secret-vm-attest-rest-server \
                 secret-vm-scripts \
                 docker-compose \
                 intel-sgx-primitives"

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
