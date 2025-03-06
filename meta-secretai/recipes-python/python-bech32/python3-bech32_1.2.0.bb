SUMMARY = "Reference implementation for Bech32 and segwit addresses."
HOMEPAGE = "https://pypi.org/project/bech32/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a9adf20934ffdc3b3e86fe7e0a1b39b7"

inherit pypi setuptools3

SRC_URI[md5sum] = "410353aa23912ce07158955187f1bfcc"
SRC_URI[sha256sum] = "7d6db8214603bd7871fcfa6c0826ef68b85b0abd90fa21c285a9c5e21d2bd899"

PYPI_PACKAGE = "bech32"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"

BBCLASSEXTEND = "native nativesdk"
