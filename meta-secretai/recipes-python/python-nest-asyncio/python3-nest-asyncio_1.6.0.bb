SUMMARY = "Patch asyncio to allow nested event loops"
HOMEPAGE = "https://pypi.org/project/nest-asyncio/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c70be50007498ec0189239a999594a99"

inherit pypi setuptools3

SRC_URI[sha256sum] = "6f172d5449aca15afd6c646851f4e31e02c598d553a667e38cafa997cfec55fe"

PYPI_PACKAGE = "nest-asyncio"
PYPI_PACKAGE_EXT = "tar.gz"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"

RDEPENDS:${PN}-dev = ""

# This package requires Python 3.7 or newer
PYTHON_BASEVERSION = "3.9"
PYTHON_MAJMIN = "3.9"
