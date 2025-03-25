SUMMARY = "Virtual package for python3-bs4"
DESCRIPTION = "Provides python3-bs4 virtual package which is provided by python3-beautifulsoup4"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

RDEPENDS:${PN} = "python3-beautifulsoup4"

BBCLASSEXTEND = "native nativesdk"
