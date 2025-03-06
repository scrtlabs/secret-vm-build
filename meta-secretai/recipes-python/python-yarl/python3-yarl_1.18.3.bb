SUMMARY = "Python yarl module"
HOMEPAGE = "https://pypi.org/project/yarl/"
# According to PyPI, license is Apache-2.0
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "2403204e201177fe32447d71f7a618fc"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "7df647e8edd71f000a5208fe6ff8c382a1de8edfbccdbbfe649d263de07d8c34"

PYPI_PACKAGE = "yarl"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
    python3-multidict \
    python3-idna \
    python3-propcache \
"

# Requires Python 3.9 or newer
PYTHON_BASEVERSION = "3.9"
PYTHON_MAJMIN = "3.9"
