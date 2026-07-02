SECTION = "kernel"
SUMMARY = "Upstream Linux Kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel

LINUX_VERSION ?= "6.16.12"

SRC_URI = "https://cdn.kernel.org/pub/linux/kernel/v6.x/linux-${LINUX_VERSION}.tar.xz \
           file://defconfig \
           file://secret-vm-gcp.cfg"

SRC_URI[sha256sum] = "7ca4debc5ca912ebb8a76944a5c118afd5d09e31ef43c494adb14273da29a26e"
LINUX_VERSION_EXTENSION = "-upstream"

PV = "${LINUX_VERSION}${SRCPV}"
S = "${WORKDIR}/linux-${LINUX_VERSION}"

DEPENDS += "openssl-native util-linux-native bison-native flex-native elfutils-native"

do_configure:prepend() {
    cp ${WORKDIR}/defconfig ${B}/.config
    ${S}/scripts/kconfig/merge_config.sh -m -O ${B} ${B}/.config ${WORKDIR}/secret-vm-gcp.cfg
}
