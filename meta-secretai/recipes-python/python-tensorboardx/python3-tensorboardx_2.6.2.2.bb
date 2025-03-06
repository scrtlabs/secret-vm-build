SUMMARY = "Python TensorBoardX module"
HOMEPAGE = "https://github.com/lanpa/tensorboardX"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a5f458e5b8c320bcb57e60308ad1ccb1"

inherit pypi setuptools3

SRC_URI[md5sum] = "5bc8af1912dffdcfdd768e576e4d2e0e"
SRC_URI[sha256sum] = "c6476d7cd0d529b0b72f4acadb1269f9ed8b22f441e87a84f2a3b940bb87b666"

PYPI_PACKAGE = "tensorboardX"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
    ${PYTHON_PN}-numpy \
    ${PYTHON_PN}-protobuf \
    ${PYTHON_PN}-six \
"
