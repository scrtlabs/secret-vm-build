SUMMARY = "Python ecdsa module"
HOMEPAGE = "https://pypi.org/project/ecdsa/"
# According to PyPI, license is MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=66ffc5e30f76cbb5358fe54b645e5a1d"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "cedd271195e1876b6725944a95fba39a"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "881fa5e12bb992972d3d1b3d4dfbe149ab76a89f13da02daa5ea1ec7dea6e747"

PYPI_PACKAGE = "ecdsa"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
    python3-six \
"
