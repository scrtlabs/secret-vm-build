SUMMARY = "Python propcache module"
HOMEPAGE = "https://pypi.org/project/propcache/"
# According to PyPI, license is Apache-2.0
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

inherit pypi python_setuptools_build_meta

# MD5 hash for the package
SRC_URI[md5sum] = "e21cefaa1f64bade3837e33eb6b9ff6d"
# SHA256 hash for the package
SRC_URI[sha256sum] = "a8fd93de4e1d278046345f49e2238cdb298589325849b2645d4a94c53faeffc5"

PYPI_PACKAGE = "propcache"

DEPENDS += "python3-expandvars-native"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
    python3-expandvars \
"

# Include all installed files in the package
FILES:${PN} = "${PYTHON_SITEPACKAGES_DIR}/*"
