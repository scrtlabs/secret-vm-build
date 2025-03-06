SUMMARY = "Pure-Python HPACK header compression"
HOMEPAGE = "https://pypi.org/project/hpack/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5bf1c68e73fbaec2b1687b7e71514393"

SRC_URI[md5sum] = "85e233ffb7939aa41bdbaf5c0c3176d9"
SRC_URI[sha256sum] = "84a076fad3dc9a9f8063ccb8041ef100867b1878b25ef0ee63847a5d53818a6c"

inherit pypi setuptools3

PYPI_PACKAGE = "hpack"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"
