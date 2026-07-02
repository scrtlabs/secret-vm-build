SECTION = "kernel"
SUMMARY = "Upstream Linux Kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel

LINUX_VERSION ?= "6.16.12"

SRC_URI = "https://git.kernel.org/pub/scm/linux/kernel/git/stable/linux.git/snapshot/linux-${LINUX_VERSION}.tar.gz \
           file://defconfig \
           file://secret-vm-gcp.cfg"

SRC_URI[sha256sum] = "2f23c309011f9ec71351fa386966a69bd97e6366ed917013d7f5471e6443f54b"
LINUX_VERSION_EXTENSION = "-upstream"

PV = "${LINUX_VERSION}${SRCPV}"
S = "${WORKDIR}/linux-${LINUX_VERSION}"

DEPENDS += "openssl-native util-linux-native bison-native flex-native elfutils-native"

do_configure:prepend() {
    cp ${WORKDIR}/defconfig ${B}/.config
    ${S}/scripts/kconfig/merge_config.sh -m -O ${B} ${B}/.config ${WORKDIR}/secret-vm-gcp.cfg
}
