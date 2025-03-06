SUMMARY = "HTTP/2 State-Machine based protocol implementation"
HOMEPAGE = "https://pypi.org/project/h2/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b5e2b8fba162d5286b04373f8de78ea1"

SRC_URI[md5sum] = "626bf04aac156c8e56bd5c6c8a8f9f5c"
SRC_URI[sha256sum] = "a83aca08fbe7aacb79fec788c9c0bac936343560ed9ec18b82a13a12c28d2abb"

inherit pypi setuptools3

PYPI_PACKAGE = "h2"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
    python3-hyperframe \
    python3-hpack \
"
