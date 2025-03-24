SUMMARY = "TDX crypt tool"
DESCRIPTION = "${SUMMARY}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://Cargo.toml \
          file://Cargo.lock \
          file://src/"
S = "${WORKDIR}"

inherit cargo_bin

do_compile[network] = "1"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/target/x86_64-unknown-linux-gnu/release/crypt-tool ${D}${bindir}/crypt-tool
}

FILES:${PN} += "${bindir}/crypt-tool"
