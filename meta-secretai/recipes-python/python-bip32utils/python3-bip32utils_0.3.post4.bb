SUMMARY = "Python bip32utils module"
HOMEPAGE = "https://pypi.org/project/bip32utils/"
# According to PyPI, license is MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6547e6ba273fb42648401421c6e7ffc3"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "8af03c95ce309451290b05bb0293ca32"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "86125e16732101f17dbf4307ca0311a06ded0aca94220ac961e5bce0a444e972"

PYPI_PACKAGE = "bip32utils"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
    python3-ecdsa \
"
