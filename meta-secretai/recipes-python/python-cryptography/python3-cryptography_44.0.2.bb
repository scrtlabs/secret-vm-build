SUMMARY = "Python cryptography module"
HOMEPAGE = "https://pypi.org/project/cryptography/"
LICENSE = "CLOSED"  # Update this with the actual license

inherit pypi setuptools3

SRC_URI[md5sum] = "9cb2411324687347a27d349d3e74eb7c"
SRC_URI[sha256sum] = "c63454aa261a0cf0c5b4718349629793e9e634993538db841165b3df74f37ec0"

PYPI_PACKAGE = "cryptography"

RDEPENDS:${PN} += " \\\n    ${PYTHON_PN}-core \\\n"
