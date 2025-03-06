SUMMARY = "Better Protobuf and gRPC"
HOMEPAGE = "https://pypi.org/project/betterproto/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8036420de60a88b65f30fca0f00ec609"

inherit pypi setuptools3

SRC_URI[md5sum] = "7e5a5b7df8edef011cfaa8d053d93544"
SRC_URI[sha256sum] = "d3e6115c7d5136f1d5974e565b7560273f66b43065e74218e472321ee1258f4c"

PYPI_PACKAGE = "betterproto"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
    ${PYTHON_PN}-grpclib \
    ${PYTHON_PN}-dateutil \
"
