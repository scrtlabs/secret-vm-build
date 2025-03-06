SUMMARY = "Python aiohappyeyeballs module"
HOMEPAGE = "https://pypi.org/project/aiohappyeyeballs/"
# According to PyPI, license is PSF-2.0 (Python Software Foundation)
LICENSE = "PSF-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a484c8d0a0124bb795b57a93d16ed93d"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "3a4c5671b62b3bfc3d2d09970e13a3f0"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "9b05052f9042985d32ecbe4b59a77ae19c006a78f1344d7fdad69d28ded3d0b0"

PYPI_PACKAGE = "aiohappyeyeballs"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"

# Requires Python 3.9 or newer
PYTHON_BASEVERSION = "3.9"
PYTHON_MAJMIN = "3.9"
