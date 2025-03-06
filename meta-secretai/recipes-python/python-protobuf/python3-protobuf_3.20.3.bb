SUMMARY = "Python protobuf module"
HOMEPAGE = "https://pypi.org/project/protobuf/"
# According to PyPI, license is BSD-3-Clause
LICENSE = "BSD-3-Clause"

inherit pypi setuptools3

# MD5 hash for the wheel
SRC_URI[md5sum] = "d54a346e6bfbd0e599cc639e8b671b56"
# SHA256 hash for the wheel
SRC_URI[sha256sum] = "a7ca6d488aa8ff7f329d4c545b2dbad8ac31464f1d8b1c87ad1346717731e4db"

PYPI_PACKAGE = "protobuf"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"
