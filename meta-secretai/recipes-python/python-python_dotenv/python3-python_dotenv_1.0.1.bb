SUMMARY = "Python python-dotenv module"
HOMEPAGE = "https://pypi.org/project/python-dotenv/"
# According to PyPI, license is BSD-3-Clause
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d3bb4778812eda6fbe92a4318145cf74"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "be2ee069abf0a751940ae8674a7bd91e"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "f7b63ef50f1b690dddf550d03497b66d609393b40b564ed0d674909a68ebf16a"

PYPI_PACKAGE = "python-dotenv"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"
