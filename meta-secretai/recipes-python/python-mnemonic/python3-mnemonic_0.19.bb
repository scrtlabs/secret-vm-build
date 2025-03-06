SUMMARY = "Python mnemonic module"
HOMEPAGE = "https://pypi.org/project/mnemonic/"
# According to PyPI classifiers, license is MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9dd462d0e7c6d8f3cb431ab8c60e2f84"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "447ca40b6411fef27c799da3c293da99"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "a8d78c5100acfa7df9bab6b9db7390831b0e54490934b718ff9efd68f0d731a6"

PYPI_PACKAGE = "mnemonic"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"
