SUMMARY = "GKMS utilities for SecretVM"
DESCRIPTION = "GKMS utilities for SecretVM"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/scrtlabs/gkms-utils.git;branch=master;protocol=https"
SRCREV = "88a2edc4f4619b744eb1f678fb3460bb9e5dd289"
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
