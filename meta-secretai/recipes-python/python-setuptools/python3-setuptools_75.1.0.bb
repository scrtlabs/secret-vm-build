SUMMARY = "Python setuptools module"
HOMEPAGE = "https://pypi.org/project/setuptools/"
# Based on the PyPI classifiers: License :: OSI Approved :: MIT License
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=141643e11c48898150daa83802dbc65f"


inherit pypi setuptools3

# MD5 hash for the source package
SRC_URI[md5sum] = "8e8aed1625afae37b59272ff981d6e1c"
# SHA256 hash for the source package
SRC_URI[sha256sum] = "d59a21b17a275fb872a9c3dae73963160ae079f1049ed956880cd7c09b120538"

PYPI_PACKAGE = "setuptools"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"
