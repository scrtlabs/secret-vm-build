SUMMARY = "SecretVM scripts"
DESCRIPTION = "SecretVM scripts"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://${THISDIR}/files"
S = "${WORKDIR}/${THISDIR}/files"

RDEPENDS:${PN} += "systemd \
                   bash \
                   python3 \
                   python3-nv-attestation-sdk \
                   curl \
                   openssl \
                   cryptsetup \
                   e2fsprogs \
                   attest-tool \
                   crypt-tool"

inherit systemd

do_install() {
    install -d ${D}${bindir}
    install -m 0700 ${S}/startup.sh ${D}${bindir}/
    install -m 0700 ${S}/rtmr-ext.sh ${D}${bindir}/
    install -m 0700 ${S}/secret-vm-start.sh ${D}${bindir}/
    install -m 0700 ${S}/secret-vm-generate-cert.sh ${D}${bindir}/
    install -m 0700 ${S}/gpu-attest.py ${D}${bindir}/gpu-attest

    install -d ${D}${systemd_unitdir}/system
    install -m 0600 ${S}/secret-vm-attest-rest.service ${D}${systemd_unitdir}/system/
    install -m 0600 ${S}/secret-vm-docker-start.service ${D}${systemd_unitdir}/system/
    install -m 0600 ${S}/secret-vm-startup.service ${D}${systemd_unitdir}/system/

    install -d ${D}${systemd_unitdir}/network
    install -m 0644 ${S}/10-enp.network ${D}${systemd_unitdir}/network
}

FILES:${PN} += "${bindir} \
                ${systemd_unitdir}/system/secret-vm-attest-rest.service \
                ${systemd_unitdir}/system/secret-vm-docker-start.service \
                ${systemd_unitdir}/system/secret-vm-startup.service \
                ${systemd_unitdir}/network/10-enp.network"
