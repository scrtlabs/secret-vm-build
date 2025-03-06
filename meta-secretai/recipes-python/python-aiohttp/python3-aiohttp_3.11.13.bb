SUMMARY = "Python aiohttp module"
HOMEPAGE = "https://pypi.org/project/aiohttp/"
# According to PyPI, license is Apache-2.0
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=9873c5f5c22213f5ba8d63b1e1afa924"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "f4ba6290d7a4d5ccb4cfe22730743879"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "8ce789231404ca8fff7f693cdce398abf6d90fd5dae2b1847477196c243b1fbb"

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
