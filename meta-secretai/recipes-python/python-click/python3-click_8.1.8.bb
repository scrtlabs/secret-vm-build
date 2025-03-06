SUMMARY = "Python click module"
HOMEPAGE = "https://pypi.org/project/click/"
LICENSE = "CLOSED"  # Update this with the actual license

inherit pypi setuptools3

SRC_URI[md5sum] = "b52ee8e6c33d88a2b4626e6a6002245d"
SRC_URI[sha256sum] = "ed53c9d8990d83c2a27deae68e4ee337473f6330c040a31d4225c9574d16096a"

PYPI_PACKAGE = "click"

RDEPENDS:${PN} += " \\\n    ${PYTHON_PN}-core \\\n"
