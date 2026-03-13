SUMMARY = "TDX attest tool"
DESCRIPTION = "${SUMMARY}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=attest-tool-tsm;protocol=https"
SRCREV = "10bb0809a663270df296174df88d8003a4cefd40"
S = "${WORKDIR}/git/attest_tool"

inherit cargo_bin

do_compile[network] = "1"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/target/x86_64-unknown-linux-gnu/release/attest_tool ${D}${bindir}/attest-tool
}
