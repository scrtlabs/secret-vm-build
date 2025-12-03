SECTION = "kernel"
SUMMARY = "AMD SEV-SNP Guest Linux Kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel

SRC_URI = "git://github.com/AMDESE/linux.git;protocol=https;branch=snp-guest-latest \
           file://defconfig \
           file://secret-vm-sev.cfg"

LINUX_VERSION ?= "6.16.0"
LINUX_VERSION_EXTENSION = "-sev-snp-guest"

SRCREV = "038d61fd642278bab63ee8ef722c50d10ab01e8f"
PV = "${LINUX_VERSION}+git${SRCPV}"
S = "${WORKDIR}/git"
DEPENDS += "openssl-native util-linux-native"

do_configure:prepend() {
    ${S}/scripts/kconfig/merge_config.sh -m -O ${B} ${B}/.config ${WORKDIR}/secret-vm-sev.cfg
    oe_runmake -C ${S} O=${B} olddefconfig
}
