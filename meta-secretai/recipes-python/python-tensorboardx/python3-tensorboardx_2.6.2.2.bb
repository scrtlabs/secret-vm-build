SUMMARY = "Python TensorBoardX module"
HOMEPAGE = "https://github.com/lanpa/tensorboardX"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a5f458e5b8c320bcb57e60308ad1ccb1"

inherit pypi setuptools3

SRC_URI[md5sum] = "b3386ea992d41b0718649736aedbe2d2"
SRC_URI[sha256sum] = "160025acbf759ede23fd3526ae9d9bfbfd8b68eb16c38a010ebe326dc6395db8"

PYPI_PACKAGE = "tensorboardX"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
    ${PYTHON_PN}-numpy \
    ${PYTHON_PN}-protobuf \
    ${PYTHON_PN}-six \
"
