SUMMARY = "URL manipulation made simple."
DESCRIPTION = "furl is a small Python library that makes parsing and manipulating URLs easy."
HOMEPAGE = "https://github.com/gruns/furl"
LICENSE = "Unlicense"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=e2ebb70c96633646130ad9c38cf1092f"

inherit pypi setuptools3

SRC_URI[md5sum] = "4045284108a6fe7a330ba672cdf24b81"
SRC_URI[sha256sum] = "5a6188fe2666c484a12159c18be97a1977a71d632ef5bb867ef15f54af39cc4e"

PYPI_PACKAGE = "furl"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
    python3-six \
    python3-orderedmultidict \
"

BBCLASSEXTEND = "native nativesdk"
