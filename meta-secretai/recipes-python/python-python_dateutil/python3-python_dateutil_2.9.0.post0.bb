SUMMARY = "Python python-dateutil module"
HOMEPAGE = "https://pypi.org/project/python-dateutil/"
LICENSE = "CLOSED"  # Update this with the actual license

inherit pypi setuptools3

SRC_URI[md5sum] = "81cb6aad924ef40ebfd3d62eaebe47c6"
SRC_URI[sha256sum] = "37dd54208da7e1cd875388217d5e00ebd4179249f90fb72437e91a35459a0ad3"

PYPI_PACKAGE = "python-dateutil"

RDEPENDS:${PN} += " \\\n    ${PYTHON_PN}-core \\\n"
