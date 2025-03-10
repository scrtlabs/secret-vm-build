SUMMARY = "The ultimate Python library in building OAuth and OpenID Connect servers and clients."
SECTION = "devel/python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=79282424084a4099a6c4a241e7f8c566"

SRC_URI[sha256sum] = "8408861cbd9b4ea2ff759b00b6f02fd7d81ac5a56d0b2b22c08606c6049aae11"

PYPI_PACKAGE = "authlib"

inherit pypi setuptools3

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-cryptography \
    ${PYTHON_PN}-requests \
"

BBCLASSEXTEND = "native nativesdk"
