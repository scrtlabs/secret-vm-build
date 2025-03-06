SUMMARY = "Ordered Multivalue Dictionary"
DESCRIPTION = "Ordered Multivalue Dictionary - omdict."
HOMEPAGE = "https://github.com/gruns/orderedmultidict"
LICENSE = "Unlicense"
LIC_FILES_CHKSUM = "file://LICENSE;md5=89aad37e72d8ecafe28b2b9ec16018b6"

inherit pypi setuptools3

SRC_URI[md5sum] = "8db4ef0caaf1884f2b056158005504da"
SRC_URI[sha256sum] = "04070bbb5e87291cc9bfa51df413677faf2141c73c61d2a5f7b26bea3cd882ad"

PYPI_PACKAGE = "orderedmultidict"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
    python3-six \
"

BBCLASSEXTEND = "native nativesdk"
