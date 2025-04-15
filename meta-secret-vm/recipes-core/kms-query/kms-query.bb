SUMMARY = "KMS query tool for SecretVM"
DESCRIPTION = "KMS query tool for SecretVM"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0d6d1641f41b686243d013605f2fd8d3"

SRC_URI = "file://${THISDIR}/files/"
S = "${WORKDIR}/${THISDIR}/files"

inherit go

do_compile[network] = "1"

do_compile() {
    cd ${S}
    go build -o kms-query
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/kms-query ${D}${bindir}/
}
