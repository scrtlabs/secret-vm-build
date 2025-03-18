SUMMARY = "Dummy package for Beautiful Soup (beautifulsoup4)"
HOMEPAGE = "https://pypi.org/project/bs4/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit python3-dir

# This is a dummy/virtual package that depends on beautifulsoup4
RDEPENDS:${PN} = "python3-beautifulsoup4"

# Allow empty package as this is just a dependency provider
ALLOW_EMPTY:${PN} = "1"
