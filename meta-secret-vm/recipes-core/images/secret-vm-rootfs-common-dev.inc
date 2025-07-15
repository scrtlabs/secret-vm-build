IMAGE_INSTALL += " sshd git"
ROOTFS_POSTPROCESS_COMMAND += "add_env; configure_dropbear;"

add_env() {
    install -d ${IMAGE_ROOTFS}/usr/lib/systemd/system/secret-vm-startup.service.d
    cat <<EOF > ${IMAGE_ROOTFS}/usr/lib/systemd/system/secret-vm-startup.service.d/override.conf
[Service]
Environment="DEBUG=true"
EOF
}

configure_dropbear() {
    install -d ${IMAGE_ROOTFS}/home/root/.ssh
    echo "ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIN3xIQcDeeqTejNHMAFr+nYpd7BJC3xnR1881hGE5L/I slabs@scrtlabs.com" > ${IMAGE_ROOTFS}/home/root/.ssh/authorized_keys
    echo 'DROPBEAR_EXTRA_ARGS=" -B -s"' > ${IMAGE_ROOTFS}/etc/default/dropbear
}
