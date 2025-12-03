SUMMARY = "AMDSEV snpguest tool"
DESCRIPTION = "${SUMMARY}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "git://github.com/virtee/snpguest.git;branch=main;protocol=https"
SRCREV = "255496e2d5d7521ed664be2615f03d56e124321f"
S = "${WORKDIR}/git"

inherit cargo_bin

do_compile[network] = "1"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/target/x86_64-unknown-linux-gnu/release/snpguest ${D}${bindir}/snpguest
}
