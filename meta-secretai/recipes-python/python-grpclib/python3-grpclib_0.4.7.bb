SUMMARY = "Python grpclib module"
HOMEPAGE = "https://pypi.org/project/grpclib/"
LICENSE = "CLOSED"  # Update this with the actual license

inherit pypi setuptools3

SRC_URI[md5sum] = "60ce203e5b9f95de3d7e5721dbce2bd9"
SRC_URI[sha256sum] = "2988ef57c02b22b7a2e8e961792c41ccf97efc2ace91ae7a5b0de03c363823c3"

PYPI_PACKAGE = "grpclib"

RDEPENDS:${PN} += " \\\n    ${PYTHON_PN}-core \\\n"
