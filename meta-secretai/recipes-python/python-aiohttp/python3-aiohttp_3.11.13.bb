SUMMARY = "Python aiohttp module"
HOMEPAGE = "https://pypi.org/project/aiohttp/"
# According to PyPI, license is Apache-2.0
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=9873c5f5c22213f5ba8d63b1e1afa924"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "0a78a5683a0a7418988229344211a8e8"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "a4fe27dbbeec445e6e1291e61d61eb212ee9fed6e47998b27de71d70d3e8777d"

PYPI_PACKAGE = "aiohttp"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
    python3-attrs \
    python3-yarl \
    python3-multidict \
    python3-async-timeout \
    python3-frozenlist \
    python3-aiosignal \
    python3-propcache \
"

# Requires Python 3.9 or newer
PYTHON_BASEVERSION = "3.9"
PYTHON_MAJMIN = "3.9"
