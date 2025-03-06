SUMMARY = "Python hpack module"
HOMEPAGE = "https://pypi.org/project/hpack/"
# According to PyPI, license is MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5bf1c68e73fbaec2b1687b7e71514393"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "fbd10efbd10112bfe63d12ddd74cb250"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "157ac792668d995c657d93111f46b4535ed114f0c9c8d672271bbec7eae1b496"

PYPI_PACKAGE = "hpack"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"

# Requires Python 3.9 or newer
PYTHON_BASEVERSION = "3.9"
PYTHON_MAJMIN = "3.9"
