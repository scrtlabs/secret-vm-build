SUMMARY = "Python wheel module"
HOMEPAGE = "https://pypi.org/project/wheel/"
LICENSE = "CLOSED"  # Update this with the actual license

inherit pypi setuptools3

SRC_URI[md5sum] = "440ff4fe51579b7ed16f02af8f8d9494"
SRC_URI[sha256sum] = "a29c3f2817e95ab89aa4660681ad547c0e9547f20e75b0562fe7723c9a2a9d49"

PYPI_PACKAGE = "wheel"

RDEPENDS:${PN} += " \\\n    ${PYTHON_PN}-core \\\n"
