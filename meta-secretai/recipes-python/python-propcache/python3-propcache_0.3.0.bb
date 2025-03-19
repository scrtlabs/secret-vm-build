SUMMARY = "Python property caching"
HOMEPAGE = "https://pypi.org/project/propcache/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI[sha256sum] = "a8fd93de4e1d278046345f49e2238cdb298589325849b2645d4a94c53faeffc5"

PYPI_PACKAGE = "propcache"

inherit pypi setuptools

RDEPENDS:${PN} = "python3-core python3-expandvars"

FILES:${PN} = "${PYTHON_SITEPACKAGES_DIR}"

INSANE_SKIP:${PN} += "already-stripped"
