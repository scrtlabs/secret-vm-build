SUMMARY = "Python six module"
HOMEPAGE = "https://pypi.org/project/six/"
# According to PyPI, license is MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e50fab7e9501f3dfc920eafeb24b46ea"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "a0387fe15662c71057b4fb2b7aa9056a"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "ff70335d468e7eb6ec65b95b99d3a2836546063f63acc5171de367e834932a81"

PYPI_PACKAGE = "six"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"

# Python 2.7 and 3.3+ compatibility library
BBCLASSEXTEND = "native nativesdk"
