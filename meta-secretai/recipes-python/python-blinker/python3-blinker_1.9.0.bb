SUMMARY = "Fast, simple object-to-object and broadcast signaling"
HOMEPAGE = "https://pypi.org/project/blinker/"
# License is MIT according to classifiers
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=42cd19c88fc13d1307a4efd64ee90e4e"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "1ffce54aca3d568ab18ee921d479274f"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "b4ce2265a7abece45e7cc896e98dbebe6cead56bcf805a3d23136d145f5445bf"

PYPI_PACKAGE = "blinker"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"

# This package requires Python 3.9 or newer
PYTHON_BASEVERSION = "3.9"
PYTHON_MAJMIN = "3.9"
