IMAGE_INSTALL += " sshd"
ROOTFS_POSTPROCESS_COMMAND += "add_env;"

add_env() {
    install -d ${IMAGE_ROOTFS}/usr/lib/systemd/system/secret-vm-startup.service.d
    cat <<EOF > ${IMAGE_ROOTFS}/usr/lib/systemd/system/secret-vm-startup.service.d/override.conf
[Service]
Environment="DEBUG=true"
EOF
}
