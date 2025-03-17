SUMMARY = "Recipe for creating the secret user"
DESCRIPTION = "Creates a non-root user account"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit extrausers

DEPENDS += "openssl-native shadow-native"

# Required to make the package architecture-specific
PACKAGE_ARCH = "${MACHINE_ARCH}"

# Create an empty package
ALLOW_EMPTY:${PN} = "1"

# Ensure our package is installed during image creation
RDEPENDS:${PN} += "base-passwd shadow"

# Inherit the EXTRA_USERS_PARAMS from local.conf
EXTRA_USERS_PARAMS:append = " "
