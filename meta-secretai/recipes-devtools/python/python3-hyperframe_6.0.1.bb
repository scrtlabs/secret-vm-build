SUMMARY = "HTTP/2 framing layer for Python"
HOMEPAGE = "https://pypi.org/project/hyperframe/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5bf1c68e73fbaec2b1687b7e71514393"

SRC_URI[md5sum] = "f8fc77e082e2594dd7578a7f81b65006"
SRC_URI[sha256sum] = "0ec6bafd80d8ad2195c4f03aacba3a8265e57bc4cff261e802bf39970ed02a15"

inherit pypi setuptools3

PYPI_PACKAGE = "hyperframe"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"
