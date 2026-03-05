SUMMARY = "TDX attest tool"
DESCRIPTION = "${SUMMARY}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=gcp;protocol=https"
SRCREV = "3e6df38fd5270a30f43eebddf80e3683bcc8f92b"
S = "${WORKDIR}/git/attest_tool"

inherit cargo_bin

do_compile[network] = "1"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/target/x86_64-unknown-linux-gnu/release/attest_tool ${D}${bindir}/attest-tool
}
