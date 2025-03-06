SUMMARY = "Pure-Python gRPC implementation for asyncio"
HOMEPAGE = "https://pypi.org/project/grpclib/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=22773e7c54612631f54166fd6358d9ec"

inherit pypi setuptools3

SRC_URI[md5sum] = "60ce203e5b9f95de3d7e5721dbce2bd9"
SRC_URI[sha256sum] = "2988ef57c02b22b7a2e8e961792c41ccf97efc2ace91ae7a5b0de03c363823c3"

PYPI_PACKAGE = "grpclib"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
    ${PYTHON_PN}-h2 \
    ${PYTHON_PN}-multidict \
"
