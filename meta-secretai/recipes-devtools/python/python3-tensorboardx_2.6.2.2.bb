SUMMARY = "TensorBoardX lets you watch Tensors Flow without Tensorflow"
DESCRIPTION = "TensorBoardX is a tensorboard writer for PyTorch and other libraries"
HOMEPAGE = "https://github.com/lanpa/tensorboardX"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI[md5sum] = "5bc8af1912dffdcfdd768e576e4d2e0e"
SRC_URI[sha256sum] = "c6476d7cd0d529b0b72f4acadb1269f9ed8b22f441e87a84f2a3b940bb87b666"

PYPI_PACKAGE = "tensorboardX"

inherit pypi setuptools3


RDEPENDS:${PN} = "python3-numpy python3-packaging python3-protobuf python3-six"

BBCLASSEXTEND = "native nativesdk"
