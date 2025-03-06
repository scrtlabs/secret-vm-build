SUMMARY = "Python Flask-SSLify module"
HOMEPAGE = "https://pypi.org/project/Flask-SSLify/"
LICENSE = "CLOSED"  # Update this with the actual license

inherit pypi setuptools3

SRC_URI[md5sum] = "1282f5af7d621a32130296ad8dd6a70c"
SRC_URI[sha256sum] = "d33e1d3c09cd95154176aa8a7319418e52129fc482dd56d8a8ad7c24500d543e"

PYPI_PACKAGE = "Flask-SSLify"

RDEPENDS:${PN} += " \\\n    ${PYTHON_PN}-core \\\n"
