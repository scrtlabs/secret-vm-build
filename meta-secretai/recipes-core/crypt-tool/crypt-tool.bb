SUMMARY = "TDX crypt tool"
DESCRIPTION = "${SUMMARY}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://${THISDIR}/files"
S = "${WORKDIR}/${THISDIR}/files"

inherit cargo_bin

do_compile[network] = "1"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/target/x86_64-unknown-linux-gnu/release/crypt_tool ${D}${bindir}/crypt_tool
}

FILES:${PN} += "${bindir}/crypt_tool"
