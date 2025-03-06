SUMMARY = "Python pycparser module"
HOMEPAGE = "https://pypi.org/project/pycparser/"
# Update this with the actual license
LICENSE = "CLOSED"

inherit pypi setuptools3

SRC_URI[md5sum] = "8922b0b1b53b419e3a38fba4aa43a348"
SRC_URI[sha256sum] = "491c8be9c040f5390f5bf44a5b07752bd07f56edf992381b05c701439eec10f6"

PYPI_PACKAGE = "pycparser"

RDEPENDS:${PN} += "    ${PYTHON_PN}-core "
