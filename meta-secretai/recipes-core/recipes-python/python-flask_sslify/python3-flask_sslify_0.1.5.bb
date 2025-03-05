SUMMARY = "Python Flask-SSLify module"
HOMEPAGE = "https://pypi.org/project/Flask-SSLify/"
LICENSE = "CLOSED"  # Update this with the actual license

inherit pypi setuptools3

SRC_URI[md5sum] = "md5sum_placeholder"  # Replace with actual md5sum
SRC_URI[sha256sum] = "sha256sum_placeholder"  # Replace with actual sha256sum

PYPI_PACKAGE = "Flask-SSLify"

RDEPENDS:${PN} += " \\\n    ${PYTHON_PN}-core \\\n"
