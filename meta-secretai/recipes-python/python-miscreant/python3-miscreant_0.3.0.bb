SUMMARY = "Python miscreant module"
HOMEPAGE = "https://pypi.org/project/miscreant/"
# According to PyPI, license is MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3d46ecdc5e7c90d642f38106bbc56951"

inherit pypi setuptools3

# MD5 hash for the package
SRC_URI[md5sum] = "a768213b684391a960ea8af837661e18"
# SHA256 hash for the package
SRC_URI[sha256sum] = "0f7d6e9698a81f1b9f0e6181411b971b4620d6b829b47a8f714bc9d016ed0d35"

PYPI_PACKAGE = "miscreant"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"
