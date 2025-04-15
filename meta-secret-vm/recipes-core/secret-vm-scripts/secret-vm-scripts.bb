SUMMARY = "SecretVM scripts"
DESCRIPTION = "SecretVM scripts"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://${THISDIR}/files"
S = "${WORKDIR}/${THISDIR}/files"

PACKAGES += "${PN}-gpu"

RDEPENDS:${PN} += "systemd \
                   bash \
                   curl \
                   openssl \
                   cryptsetup \
                   e2fsprogs \
                   attest-tool \
                   kms-query \
                   crypt-tool"

RDEPENDS:${PN}-gpu += "python3 \
                       python3-nv-attestation-sdk"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "secret-vm-attest-rest.service \
                         secret-vm-docker-start.service \
                         secret-vm-startup.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

do_install() {
    install -d ${D}${bindir}
    install -m 0744 ${S}/startup.sh ${D}${bindir}/startup.sh
    install -m 0744 ${S}/rtmr-ext.sh ${D}${bindir}/rtmr-ext.sh
    install -m 0744 ${S}/secret-vm-start.sh ${D}${bindir}/secret-vm-start.sh
    install -m 0744 ${S}/secret-vm-generate-cert.sh ${D}${bindir}/secret-vm-generate-cert.sh
    install -m 0744 ${S}/gpu-attest.py ${D}${bindir}/gpu-attest

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/secret-vm-attest-rest.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/secret-vm-docker-start.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/secret-vm-startup.service ${D}${systemd_unitdir}/system/

    install -d ${D}${systemd_unitdir}/network
    install -m 0644 ${S}/10-enp.network ${D}${systemd_unitdir}/network

    install -d ${D}${sysconfdir}/
    install -m 0644 ${S}/tdx-attest.conf ${D}${sysconfdir}/
}

FILES:${PN} = "${bindir}/startup.sh \
                ${bindir}/rtmr-ext.sh \
                ${bindir}/secret-vm-start.sh \
                ${bindir}/secret-vm-generate-cert.sh \
                ${systemd_unitdir}/system/secret-vm-attest-rest.service \
                ${systemd_unitdir}/system/secret-vm-docker-start.service \
                ${systemd_unitdir}/system/secret-vm-startup.service \
                ${systemd_unitdir}/network/10-enp.network \
                ${sysconfdir}/tdx-attest.conf"
FILES:${PN}-gpu = "${bindir}/gpu-attest"
