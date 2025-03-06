SUMMARY = "Python frozenlist module"
HOMEPAGE = "https://pypi.org/project/frozenlist/"
# According to PyPI, license is Apache-2.0
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=cf056e8e7a0a5477451af18b7b5aa98c"

inherit pypi setuptools3

# MD5 hash for the package
SRC_URI[md5sum] = "649401d9ab9b34097739aca8949330d4"
# SHA256 hash for the package
SRC_URI[sha256sum] = "d994863bba198a4a518b467bb971c56e1db3f180a25c6cf7bb1949c267f748c3"

PYPI_PACKAGE = "frozenlist"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"

# Requires Python 3.8 or newer
PYTHON_BASEVERSION = "3.8"
PYTHON_MAJMIN = "3.8"
