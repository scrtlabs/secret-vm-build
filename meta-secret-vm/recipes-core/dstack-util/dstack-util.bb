SUMMARY = "DStack util tool"
DESCRIPTION = "${SUMMARY}"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/LICENSE;md5=dc7f21ccff0f672f2a7cd6f412ae627d"

SRC_URI = "git://github.com/Dstack-TEE/dstack.git;branch=master;protocol=https"
SRCREV = "c8fc789bfad06ae3bcc94337d0d0a2d81c660ebe"
S = "${WORKDIR}/git/dstack/dstack-util"

inherit cargo_bin

do_compile[network] = "1"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/target/x86_64-unknown-linux-gnu/release/dstack-util ${D}${bindir}/dstack-util
}
