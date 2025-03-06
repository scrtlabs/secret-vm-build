SUMMARY = "Python betterproto module"
HOMEPAGE = "https://pypi.org/project/betterproto/"
# According to PyPI, license is MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6547e6ba273fb42648401421c6e7ffc3"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "7e5a5b7df8edef011cfaa8d053d93544"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "d3e6115c7d5136f1d5974e565b7560273f66b43065e74218e472321ee1258f4c"

PYPI_PACKAGE = "betterproto"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
    python3-grpclib \
    python3-dateutil \
"

# This package requires Python 3.6.2 or newer
PYTHON_BASEVERSION = "3.6"
PYTHON_MAJMIN = "3.6"
