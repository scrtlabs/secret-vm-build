SUMMARY = "Python aiohappyeyeballs module"
HOMEPAGE = "https://pypi.org/project/aiohappyeyeballs/"
# According to PyPI, license is PSF-2.0 (Python Software Foundation)
LICENSE = "PSF-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a484c8d0a0124bb795b57a93d16ed93d"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "70d93af365bce12d78207097e873ba6d"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "147ec992cf873d74f5062644332c539fcd42956dc69453fe5204195e560517e1"

PYPI_PACKAGE = "aiohappyeyeballs"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"

# Requires Python 3.9 or newer
PYTHON_BASEVERSION = "3.9"
PYTHON_MAJMIN = "3.9"
