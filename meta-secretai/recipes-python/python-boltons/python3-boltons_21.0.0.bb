SUMMARY = "Functionality that should be in the standard library"
DESCRIPTION = "Boltons is a set of over 160 BSD-licensed, pure-Python utilities in the same spirit as the standard library, and yet somehow not in the standard library"
HOMEPAGE = "https://boltons.readthedocs.io/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=aef5566ac4fede9815eccf124c281317"

inherit pypi setuptools3

SRC_URI[md5sum] = "c7a17577f80a5c3316b8cb61b79d09c9"
SRC_URI[sha256sum] = "65e70a79a731a7fe6e98592ecfb5ccf2115873d01dbc576079874629e5c90f13"

PYPI_PACKAGE = "boltons"

RDEPENDS:${PN} = "\
    ${PYTHON_PN}-core \
"

BBCLASSEXTEND = "native nativesdk"
