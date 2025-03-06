SUMMARY = "Python hpack module"
HOMEPAGE = "https://pypi.org/project/hpack/"
# According to PyPI, license is MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5bf1c68e73fbaec2b1687b7e71514393"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "16e7423c5b5078c1997fa3eedd2e5935"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "ec5eca154f7056aa06f196a557655c5b009b382873ac8d1e66e79e87535f1dca"

PYPI_PACKAGE = "hpack"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"

# Requires Python 3.9 or newer
PYTHON_BASEVERSION = "3.9"
PYTHON_MAJMIN = "3.9"
