SUMMARY = "Fast, simple object-to-object and broadcast signaling"
HOMEPAGE = "https://pypi.org/project/blinker/"
# License is MIT according to classifiers
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.rst;md5=42cd19c88fc13d1307a4efd7982bc2e3"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "26605819b98a22f8bc46ee0eb2e0d4d2"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "ba0efaa9080b619ff2f3459d1d500c57bddea4a6b424b60a91141db6fd2f08bc"

PYPI_PACKAGE = "blinker"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"

# This package requires Python 3.9 or newer
PYTHON_BASEVERSION = "3.9"
PYTHON_MAJMIN = "3.9"
