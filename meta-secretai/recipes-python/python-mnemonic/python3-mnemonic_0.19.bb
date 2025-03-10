SUMMARY = "Python mnemonic module"
HOMEPAGE = "https://pypi.org/project/mnemonic/"
# According to PyPI classifiers, license is MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=47284558328af55c15e2ee19ee17e912"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "7b86484dc606bf961c927ef3d98c8557"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "4e37eb02b2cbd56a0079cabe58a6da93e60e3e4d6e757a586d9f23d96abea931"

PYPI_PACKAGE = "mnemonic"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"
