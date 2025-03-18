SUMMARY = "PyTorch Tensorboard Visualization Support"
HOMEPAGE = "https://github.com/lanpa/tensorboardX"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c9a8aeaaaa12b3df847595a0a6fb1dfb"

inherit pypi setuptools3

SRC_URI[md5sum] = "5bc8af1912dffdcfdd768e576e4d2e0e"
SRC_URI[sha256sum] = "c6476d7cd0d529b0b72f4acadb1269f9ed8b22f441e87a84f2a3b940bb87b666"

PYPI_PACKAGE = "tensorboardX"

# Add dependencies
DEPENDS += " \
    python3-setuptools-scm-native \
    python3-pip-native \
"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-numpy \
    ${PYTHON_PN}-protobuf \
    ${PYTHON_PN}-six \
    ${PYTHON_PN}-packaging \
    ${PYTHON_PN}-core \
"

# Include all installed files in the package
FILES:${PN} = "${PYTHON_SITEPACKAGES_DIR}/*"

# Allow network access during build
do_compile[network] = "1"
