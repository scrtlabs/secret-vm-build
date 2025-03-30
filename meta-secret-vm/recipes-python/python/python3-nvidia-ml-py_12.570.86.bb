SUMMARY = "Python Bindings for the NVIDIA Management Library"
DESCRIPTION = "Python bindings for the NVIDIA Management Library that allows monitoring and management of NVIDIA GPU devices"
HOMEPAGE = "https://forums.developer.nvidia.com"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "https://files.pythonhosted.org/packages/d8/a8/ec37169be4e2b7063b9076ed3fe0661e87335fbca665eed3f48c415cb234/nvidia_ml_py-12.570.86-py3-none-any.whl;downloadfilename=${BP}.whl"
SRC_URI[md5sum] = "5029752010557274faa28f87a0b1f8c7"
SRC_URI[sha256sum] = "58907de35a845abd13dcb227f18298f3b5dd94a72d04c9e594e77711e95c0b51"

inherit setuptools3

# This is a wheel package - need to setup the wheel installation
do_compile() {
    :
}

do_install() {
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}
    # Extract the wheel file directly
    cd ${D}${PYTHON_SITEPACKAGES_DIR}
    unzip -q ${WORKDIR}/${BP}.whl
    # Remove unneeded files from extraction
    find ${D}${PYTHON_SITEPACKAGES_DIR} -name "*.dist-info" -type d -prune -exec rm -rf {} \;
}
