SUMMARY = "Install scripts to secret user's home directory"
DESCRIPTION = "Copies script files to /home/secret/scripts directory"
LICENSE = "MIT"
LIC_FILES_CHKSUM = ""

INHIBIT_DEFAULT_DEPS = "1"

# Define the target directory
TARGET_DIR = "/home/secret/scripts"

do_install() {
    # Create the target directory with proper permissions
    install -d -m 0755 ${D}${TARGET_DIR}

    # Copy all files from the recipe's files directory
    cp -R ${WORKDIR}/files/* ${D}${TARGET_DIR}/

    # Ensure proper permissions for all copied files
    chmod -R 0755 ${D}${TARGET_DIR}
}

pkg_postinst:${PN}() {
    # Create the target directory if it doesn't exist
    mkdir -p $D${TARGET_DIR}

    # Set ownership of directory and contents to 'secret' user
    chown -R secret:secret $D${TARGET_DIR}
}

FILES:${PN} += "${TARGET_DIR}"

