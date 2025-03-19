SUMMARY = "Python itsdangerous module"
HOMEPAGE = "https://pypi.org/project/itsdangerous/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=4dae3272b2679dabe5d375ab3eb59089"

inherit pypi python_flit_core

SRC_URI[md5sum] = "a901babde35694c3577f7655010cd380"
SRC_URI[sha256sum] = "e0050c0b7da1eea53ffaf149c0cfbb5c6e2e2b69c4bef22c81fa6eb73e5f6173"

PYPI_PACKAGE = "itsdangerous"

RDEPENDS:${PN} += "${PYTHON_PN}-core"

# Include all installed files in the package
FILES:${PN} = "${PYTHON_SITEPACKAGES_DIR}/*"
