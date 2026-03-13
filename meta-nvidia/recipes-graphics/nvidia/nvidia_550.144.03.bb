SUMMARY = "NVidia Graphics Driver"
LICENSE = "NVIDIA-Proprietary"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=01c5e23f445259a6d1b4867efec45d22"

NVIDIA_ARCHIVE_NAME = "NVIDIA-Linux-${TARGET_ARCH}-${PV}"
NVIDIA_SRC = "${WORKDIR}/${NVIDIA_ARCHIVE_NAME}"
SRC_URI = " \
	https://us.download.nvidia.com/tesla/${PV}/${NVIDIA_ARCHIVE_NAME}.run \
"
SRC_URI[md5sum] = "474f5fe69676f588634b856dd34705ef"
SRC_URI[sha256sum] = "6a4838e2cdb26e4c0e07367ac0d3bcf799d56b5286f68fa201be3d3ddb88aac4"

do_unpack() {
	chmod +x ${DL_DIR}/${NVIDIA_ARCHIVE_NAME}.run
	rm -rf ${NVIDIA_SRC}
	${DL_DIR}/${NVIDIA_ARCHIVE_NAME}.run -x --target ${NVIDIA_SRC}
}

do_make_scripts[noexec] = "1"

include nvidia-kernel-module.inc
include nvidia-libs.inc
