ROOTFS_POSTPROCESS_COMMAND += "disable_getty_services;"

disable_getty_services() {
    for srv in getty getty-pre; do
        rm -f ${IMAGE_ROOTFS}/etc/systemd/system/${srv}.target
        rm -f ${IMAGE_ROOTFS}/usr/lib/systemd/system/${srv}.target
    done
    for srv in autovt container-getty console-getty getty-generator serial-getty getty; do
        rm -f ${IMAGE_ROOTFS}/etc/systemd/system/${srv}.service
        rm -f ${IMAGE_ROOTFS}/etc/systemd/system/${srv}@.service
        rm -f ${IMAGE_ROOTFS}/usr/lib/systemd/system/${srv}.service
        rm -f ${IMAGE_ROOTFS}/usr/lib/systemd/system/${srv}@.service
    done
}
