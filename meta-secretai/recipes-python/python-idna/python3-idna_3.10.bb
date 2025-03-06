SUMMARY = "Python idna module"
HOMEPAGE = "https://pypi.org/project/idna/"
# According to classifiers, license is BSD
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=239668a7c6066d9e0c5382e9c8c6c0e1"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "ce22685f1b296fb33e5fda362870685d"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "946d195a0d259cbba61165e88e65941f16e9b36ea6ddb97f00452bae8b1287d3"

PYPI_PACKAGE = "idna"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"

# This package requires Python 3.6 or newer
PYTHON_BASEVERSION = "3.6"
PYTHON_MAJMIN = "3.6"
