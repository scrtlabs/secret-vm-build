SUMMARY = "Protocol Buffers"
HOMEPAGE = "https://pypi.org/project/protobuf/"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://PKG-INFO;md5=89ac20b61f442d8ce13e120c8315e50b"

inherit pypi setuptools3

SRC_URI[md5sum] = "4bea84984e1c30f6470b32ebd3d67ddb"
SRC_URI[sha256sum] = "2e3427429c9cffebf259491be0af70189607f365c2f41c7c3764af6f337105f2"

UPSTREAM_CHECK_REGEX = "protobuf/(?P<pver>\d+(\.\d+)+)/"

DEPENDS += "${PYTHON_PN}-setuptools-native"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-datetime \
    ${PYTHON_PN}-json \
    ${PYTHON_PN}-logging \
    ${PYTHON_PN}-netclient \
    ${PYTHON_PN}-numbers \
    ${PYTHON_PN}-pkgutil \
    ${PYTHON_PN}-six \
    ${PYTHON_PN}-unittest \
"

# For usage in other recipies when protoc is needed
BBCLASSEXTEND = "native nativesdk"
