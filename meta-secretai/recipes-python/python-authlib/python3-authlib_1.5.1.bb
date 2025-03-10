SUMMARY = "The ultimate Python library in building OAuth and OpenID Connect servers and clients."
SECTION = "devel/python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ec053ca028e37ec79d3da3af34976d48"

SRC_URI[sha256sum] = "5cbc85ecb0667312c1cdc2f9095680bb735883b123fb509fde1e65b1c5df972e"

PYPI_PACKAGE = "authlib"

inherit pypi setuptools3

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-cryptography \
    ${PYTHON_PN}-requests \
"

BBCLASSEXTEND = "native nativesdk"
