SUMMARY = "Python hyperframe module"
HOMEPAGE = "https://pypi.org/project/hyperframe/"
# According to PyPI, license is MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5bf1c68e73fbaec2b1687b7e71514393"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "d4e6177f25f170bb0c84728181164ea0"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "b03380493a519fce58ea5af42e4a42317bf9bd425596f7a0835ffce80f1a42e5"

PYPI_PACKAGE = "hyperframe"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"

# Pure-Python HTTP/2 framing
PYTHON_BASEVERSION = "3.9"
PYTHON_MAJMIN = "3.9"
