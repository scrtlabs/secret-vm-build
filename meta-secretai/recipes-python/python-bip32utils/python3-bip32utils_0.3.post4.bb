SUMMARY = "Python bip32utils module"
HOMEPAGE = "https://pypi.org/project/bip32utils/"
# According to PyPI, license is MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6547e6ba273fb42648401421c6e7ffc3"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "ed9eadb2485adfefce34989165990f6a"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "5970f40fbb727a89d3cc06b6387b348252f7c8af6b3470df704276de728c48c2"

PYPI_PACKAGE = "bip32utils"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
    python3-ecdsa \
"
