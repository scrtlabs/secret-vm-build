SUMMARY = "SecretAI initramfs files"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit allarch

SRC_URI = "file://init"
S = "${WORKDIR}"
FILES:${PN} = "*"

do_install() {
    install -m 0755 ${S}/init ${D}/init
}
