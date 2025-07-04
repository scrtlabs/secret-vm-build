SUMMARY = "OPA policies for SecretVM"
DESCRIPTION = "${SUMMARY}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=master;protocol=https"
SRCREV = "30dd0b7d1a8d55f2c2873cee80a49f69cc3bdf86"
S = "${WORKDIR}/git/opa"

do_install() {
    install -d ${D}${sysconfdir}/docker/config
    install -m 0644 ${S}/opa-config.yaml ${D}${sysconfdir}/docker/config/opa-config.yaml
    install -d ${D}/var/www/html
    install -m 0644 ${S}/bundle.tar.gz ${D}/var/www/html/bundle.tar.gz
}
