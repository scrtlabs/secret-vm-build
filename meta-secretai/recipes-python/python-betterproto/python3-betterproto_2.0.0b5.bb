SUMMARY = "Python betterproto module"
HOMEPAGE = "https://pypi.org/project/betterproto/"
# According to PyPI, license is MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE.md;md5=393474300131412f76a83934dd8a3830"
SRC_URI += "https://raw.githubusercontent.com/danielgtaylor/python-betterproto/master/LICENSE.md;name=license"
SRC_URI[license.md5sum] = "393474300131412f76a83934dd8a3830"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "a9e60c75447667718ecc560de3de20ad"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "00a301c70a2db4d3cdd2b261522ae1d34972fb04b655a154d67daaaf4131102e"

PYPI_PACKAGE = "betterproto"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
    python3-grpclib \
    python3-dateutil \
"

# This package requires Python 3.6.2 or newer
PYTHON_BASEVERSION = "3.6"
PYTHON_MAJMIN = "3.6"
