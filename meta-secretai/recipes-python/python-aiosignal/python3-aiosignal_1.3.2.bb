SUMMARY = "aiosignal: a list of registered asynchronous callbacks"
HOMEPAGE = "https://pypi.org/project/aiosignal/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=cf056e8e7a0a5477451af18b7b5aa98c"

inherit pypi setuptools3

SRC_URI[md5sum] = "1cebd65b57dfca568ff8ea624497f30d"
SRC_URI[sha256sum] = "45cde58e409a301715980c2b01d0c28bdde3770d8290b5eb2173759d9acb31a5"

PYPI_PACKAGE = "aiosignal"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
    python3-frozenlist \
"

RDEPENDS:${PN}-dev = ""

# This package requires Python 3.9 or newer
PYTHON_BASEVERSION = "3.9"
PYTHON_MAJMIN = "3.9"
