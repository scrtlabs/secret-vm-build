SUMMARY = "Multidict implementation"
HOMEPAGE = "https://pypi.org/project/multidict/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=84c63e2bcd84e619d249af973c89dc6c"

SRC_URI[md5sum] = "ec06a613d871dadfb66f2be3a1f2f3fa"
SRC_URI[sha256sum] = "3666906492efb76453c0e7b97f2cf459b0682e7402c0489a95484965dbc1da49"

inherit pypi setuptools3

PYPI_PACKAGE = "multidict"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"
