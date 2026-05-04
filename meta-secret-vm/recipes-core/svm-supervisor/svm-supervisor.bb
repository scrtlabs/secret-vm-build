SUMMARY = "SecretVM Supervisor"
DESCRIPTION = "${SUMMARY}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=master;protocol=https"
SRCREV = "3bbb653c6b7569149ca5b8e16084383ad91d3ecf"
S = "${WORKDIR}/git"

DEPENDS += "libsodium pkgconfig-native openssl"
export SODIUM_USE_PKG_CONFIG = "1"

inherit cargo_bin

do_compile[network] = "1"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/target/x86_64-unknown-linux-gnu/release/svm-supervisor ${D}${bindir}/svm-supervisor
}
