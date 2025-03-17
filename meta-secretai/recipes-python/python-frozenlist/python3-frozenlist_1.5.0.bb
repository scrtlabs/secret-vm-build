SUMMARY = "Python frozenlist module"
HOMEPAGE = "https://pypi.org/project/frozenlist/"
# According to PyPI, license is Apache-2.0
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=cf056e8e7a0a5477451af18b7b5aa98c"

inherit pypi python_poetry_core

# MD5 hash for the package
SRC_URI[md5sum] = "0882f528872840df39091fb5085e258a"
# SHA256 hash for the package
SRC_URI[sha256sum] = "81d5af29e61b9c8348e876d442253723928dce6433e0e76cd925cd83f1b4b817"

PYPI_PACKAGE = "frozenlist"

DEPENDS += "python3-expandvars-native"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"

# Include all installed files in the package
FILES:${PN} = "${PYTHON_SITEPACKAGES_DIR}/*"
