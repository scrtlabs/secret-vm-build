SUMMARY = "Python idna module"
HOMEPAGE = "https://pypi.org/project/idna/"
# According to classifiers, license is BSD
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=204c0612e40a4dd46012a78d02c80fb1"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "28448b00665099117b6daa9887812cc4"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "12f65c9b470abda6dc35cf8e63cc574b1c52b11df2c86030af0ac09b01b13ea9"

PYPI_PACKAGE = "idna"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"

# This package requires Python 3.6 or newer
PYTHON_BASEVERSION = "3.6"
PYTHON_MAJMIN = "3.6"
