SUMMARY = "Python Jinja2 module"
HOMEPAGE = "https://pypi.org/project/Jinja2/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=5dc88300786f1c214c1e9827a5229462"

inherit pypi python_flit_core

SRC_URI[md5sum] = "083d64f070f6f1b5f75971ae60240785"
SRC_URI[sha256sum] = "8fefff8dc3034e27bb80d67c671eb8a9bc424c0ef4c0826edbff304cceff43bb"

PYPI_PACKAGE = "jinja2"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
    python3-markupsafe \
"

# Include all installed files in the package
FILES:${PN} = "${PYTHON_SITEPACKAGES_DIR}/*"
