SUMMARY = "The comprehensive WSGI web application library."
HOMEPAGE = "https://pypi.org/project/Werkzeug/"
# License is BSD according to classifiers
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE.rst;md5=5dc88300786f1c214c1e9827a5229462"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "ff3a2b0d0953eadee90c945d879c5aac"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "54b78bf3716d19a65be4fceccc0d1d7b89e608834989dfae50ea87564639213e"

PYPI_PACKAGE = "werkzeug"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
    python3-markupsafe \
"

# This package requires Python 3.9 or newer
PYTHON_BASEVERSION = "3.9"
PYTHON_MAJMIN = "3.9"
