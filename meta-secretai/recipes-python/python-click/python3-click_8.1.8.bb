SUMMARY = "Python click module"
HOMEPAGE = "https://pypi.org/project/click/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.rst;md5=1fa98232fd645608937a0fdc82e999b8"

inherit pypi python_flit_core

SRC_URI[md5sum] = "b52ee8e6c33d88a2b4626e6a6002245d"
SRC_URI[sha256sum] = "ed53c9d8990d83c2a27deae68e4ee337473f6330c040a31d4225c9574d16096a"

PYPI_PACKAGE = "click"

RDEPENDS:${PN} += "${PYTHON_PN}-core"

# Include all installed files in the package
FILES:${PN} = "${PYTHON_SITEPACKAGES_DIR}/*"
