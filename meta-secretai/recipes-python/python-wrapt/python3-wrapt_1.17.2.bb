SUMMARY = "Python wrapt module"
HOMEPAGE = "https://pypi.org/project/wrapt/"
# Update this with the actual license
LICENSE = "CLOSED"

inherit pypi setuptools3

SRC_URI[md5sum] = "f4db93e73e5c70a59955f0ec162d585d"
SRC_URI[sha256sum] = "41388e9d4d1522446fe79d3213196bd9e3b301a336965b9e27ca2788ebd122f3"

PYPI_PACKAGE = "wrapt"

RDEPENDS:${PN} += " \\\n    ${PYTHON_PN}-core \\\n"
