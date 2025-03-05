SUMMARY = "Python bip32utils module"
HOMEPAGE = "https://pypi.org/project/bip32utils/"
LICENSE = "CLOSED"  # Update this with the actual license

inherit pypi setuptools3

SRC_URI[md5sum] = "md5sum_placeholder"  # Replace with actual md5sum
SRC_URI[sha256sum] = "sha256sum_placeholder"  # Replace with actual sha256sum

PYPI_PACKAGE = "bip32utils"

RDEPENDS:${PN} += " \\\n    ${PYTHON_PN}-core \\\n"
