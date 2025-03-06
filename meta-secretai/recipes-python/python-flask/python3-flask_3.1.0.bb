SUMMARY = "Python Flask module"
HOMEPAGE = "https://pypi.org/project/Flask/"
LICENSE = "CLOSED"  # Update this with the actual license

inherit pypi setuptools3

SRC_URI[md5sum] = "c95d81666442bf04f7de7db7edbe2aff"
SRC_URI[sha256sum] = "5f873c5184c897c8d9d1b05df1e3d01b14910ce69607a117bd3277098a5836ac"

PYPI_PACKAGE = "Flask"

RDEPENDS:${PN} += " \\\n    ${PYTHON_PN}-core \\\n"
