SUMMARY = "TDX crypt tool"
DESCRIPTION = "${SUMMARY}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=master;protocol=https"
SRCREV = "82587c36c0c8bd1141a059b45af3b1b5ffa139fa"
S = "${WORKDIR}/git/crypt_tool"

DEPENDS += "libsodium pkgconfig-native"
export SODIUM_USE_PKG_CONFIG = "1"

inherit cargo_bin

do_compile[network] = "1"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/target/x86_64-unknown-linux-gnu/release/crypt-tool ${D}${bindir}/crypt-tool
}
