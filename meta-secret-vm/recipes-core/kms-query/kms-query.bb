SUMMARY = "KMS query tool for SecretVM"
DESCRIPTION = "KMS query tool for SecretVM"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=master;protocol=https"
SRCREV = "f255e9cb9399f0f13cc2e434920700c5c8dc2b6d"
S = "${WORKDIR}/git"
GO_IMPORT = "${S}"

inherit go

do_compile[network] = "1"

do_compile() {
    cd ${S}/kms_query
    go build -o kms-query
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/kms_query/kms-query ${D}${bindir}/
}
