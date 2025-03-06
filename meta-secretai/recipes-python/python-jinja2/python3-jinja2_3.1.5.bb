SUMMARY = "Python Jinja2 module"
HOMEPAGE = "https://pypi.org/project/Jinja2/"
# Update this with the actual license
LICENSE = "CLOSED"

inherit pypi setuptools3

SRC_URI[md5sum] = "083d64f070f6f1b5f75971ae60240785"
SRC_URI[sha256sum] = "8fefff8dc3034e27bb80d67c671eb8a9bc424c0ef4c0826edbff304cceff43bb"

PYPI_PACKAGE = "Jinja2"

RDEPENDS:${PN} += " \\\n    ${PYTHON_PN}-core \\\n"
