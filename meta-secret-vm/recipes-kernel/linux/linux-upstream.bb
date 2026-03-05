SECTION = "kernel"
SUMMARY = "Intel TDX Linux Kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel

LINUX_VERSION ?= "6.16.12"

SRC_URI = "https://cdn.kernel.org/pub/linux/kernel/v6.x/linux-${LINUX_VERSION}.tar.gz \
           file://defconfig \
           file://secret-vm.cfg"

SRC_URI[sha256sum] = "ffc6af80b014ddebd55e116aa29a9f7a5256c87a29a8a9dd97270b6d49625109"
LINUX_VERSION_EXTENSION = "-upstream"

PV = "${LINUX_VERSION}${SRCPV}"
S = "${WORKDIR}/linux-${LINUX_VERSION}"

DEPENDS += "openssl-native util-linux-native bison-native flex-native elfutils-native"

do_configure:prepend() {
    cp ${WORKDIR}/defconfig ${B}/.config
    ${S}/scripts/kconfig/merge_config.sh -m -O ${B} ${B}/.config ${WORKDIR}/secret-vm.cfg
}
