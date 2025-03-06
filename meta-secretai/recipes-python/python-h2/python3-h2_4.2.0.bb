SUMMARY = "Python h2 module"
HOMEPAGE = "https://pypi.org/project/h2/"
# Update this with the actual license
LICENSE = "CLOSED"

inherit pypi setuptools3

SRC_URI[md5sum] = "172e73653fc2785937541afe40363246"
SRC_URI[sha256sum] = "c8a52129695e88b1a0578d8d2cc6842bbd79128ac685463b887ee278126ad01f"

PYPI_PACKAGE = "h2"

RDEPENDS:${PN} += " \\\n    ${PYTHON_PN}-core \\\n"
