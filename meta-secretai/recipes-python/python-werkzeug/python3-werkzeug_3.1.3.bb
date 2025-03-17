SUMMARY = "The comprehensive WSGI web application library."
HOMEPAGE = "https://pypi.org/project/Werkzeug/"
# License is BSD according to classifiers
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=5dc88300786f1c214c1e9827a5229462"

inherit pypi python_flit_core

# MD5 hash for the wheel
SRC_URI[md5sum] = "b6005d403d01d08b9fe2330a0cfea05a"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "60723ce945c19328679790e3282cc758aa4a6040e4bb330f53d30fa546d44746"

PYPI_PACKAGE = "werkzeug"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
    python3-markupsafe \
"

# Include all installed files in the package
FILES:${PN} = "${PYTHON_SITEPACKAGES_DIR}/*"

# This package requires Python 3.9 or newer
PYTHON_BASEVERSION = "3.9"
PYTHON_MAJMIN = "3.9"
