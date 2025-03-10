SUMMARY = "ECDSA cryptographic signature library (pure python)"
HOMEPAGE = "https://github.com/warner/python-ecdsa"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=66ffc5e30f76cbb5358fe54b645e5a1d"

inherit pypi setuptools3

PYPI_PACKAGE = "ecdsa"

SRC_URI[md5sum] = "98c0da4c046286e892fdba727f93edea"
SRC_URI[sha256sum] = "cfc046a2ddd425adbd1a78b3c46f0d1325c657811c0f45ecc3a0a6236c1e50ff"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-gmpy2 \
    ${PYTHON_PN}-six \
"

BBCLASSEXTEND = "native nativesdk"
