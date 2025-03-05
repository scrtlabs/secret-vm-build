SUMMARY = "Python aiosignal module"
HOMEPAGE = "https://pypi.org/project/aiosignal/"
LICENSE = "CLOSED"  # Update this with the actual license

inherit pypi setuptools3

SRC_URI[md5sum] = "md5sum_placeholder"  # Replace with actual md5sum
SRC_URI[sha256sum] = "sha256sum_placeholder"  # Replace with actual sha256sum

PYPI_PACKAGE = "aiosignal"

RDEPENDS:${PN} += " \\\n    ${PYTHON_PN}-core \\\n"
