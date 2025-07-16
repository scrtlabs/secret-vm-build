SUMMARY = "OPA policies for SecretVM"
DESCRIPTION = "${SUMMARY}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=master;protocol=https"
SRCREV = "70b81c1f6511050824e2892ecef286ec362e08db"
S = "${WORKDIR}/git/opa"

do_install() {
    install -d ${D}${sysconfdir}/docker/config
    install -m 0644 ${S}/opa-config.yaml ${D}${sysconfdir}/docker/config/opa-config.yaml
    install -d ${D}/var/www/html
    install -m 0644 ${S}/bundle.tar.gz ${D}/var/www/html/bundle.tar.gz
}
