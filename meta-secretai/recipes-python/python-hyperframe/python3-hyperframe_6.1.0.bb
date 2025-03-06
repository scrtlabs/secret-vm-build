SUMMARY = "Python hyperframe module"
HOMEPAGE = "https://pypi.org/project/hyperframe/"
# According to PyPI, license is MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5bf1c68e73fbaec2b1687b7e71514393"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "fda90f638a3db3e8f1837c2e0ade8f08"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "f630908a00854a7adeabd6382b43923a4c4cd4b821fcb527e6ab9e15382a3b08"

PYPI_PACKAGE = "hyperframe"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"

# Pure-Python HTTP/2 framing
PYTHON_BASEVERSION = "3.9"
PYTHON_MAJMIN = "3.9"
