SUMMARY = "GKMS utilities for SecretVM"
DESCRIPTION = "GKMS utilities for SecretVM"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/scrtlabs/gkms-utils.git;branch=master;protocol=https"
SRCREV = "4e0dd3d7be9ef99dcc9a4df4a6d364d1f2432d1c"
S = "${WORKDIR}/git"
GO_IMPORT = "${S}"

inherit go

do_compile[network] = "1"

do_compile() {
    cd ${S}/gkms-guest
    go build -o gkms-guest
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/gkms-guest/gkms-guest ${D}${bindir}/
}
