ROOTFS_POSTPROCESS_COMMAND += "add_env;"

add_env() {
    echo "DEBUG=true" >> ${IMAGE_ROOTFS}/etc/environment
}
