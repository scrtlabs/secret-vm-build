SUMMARY = "Signing server for SecretVM"
DESCRIPTION = "Signing server for SecretVM"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=master;protocol=https"
SRCREV = "0688766e52cfd4512fc414bf43c200c5ca5b61f3"
S = "${WORKDIR}/git"
GO_IMPORT = "${S}"

inherit go

do_compile[network] = "1"

do_compile() {
    cd ${S}/signing_server
    go build -o signing-server
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/signing_server/signing-server ${D}${bindir}/
}
