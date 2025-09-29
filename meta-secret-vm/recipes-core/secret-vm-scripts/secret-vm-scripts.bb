SUMMARY = "SecretVM scripts"
DESCRIPTION = "SecretVM scripts"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=master;protocol=https"
SRCREV = "0f9e7699b7a9868db5c3c2473828545182c5f09e"
S = "${WORKDIR}/git"

PACKAGES += "${PN}-gpu"

RDEPENDS:${PN} += "systemd \
                   bash \
                   curl \
                   jq \
                   openssl \
                   cryptsetup \
                   e2fsprogs \
                   attest-tool \
                   signing-server \
                   kms-query \
                   crypt-tool"

RDEPENDS:${PN}-gpu += "python3 \
                       python3-nv-attestation-sdk"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "secret-vm-attest-rest.service \
                         secret-vm-docker-start.service \
                         secret-vm-startup.service \
                         secret-vm-network-setup.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

do_install() {
    install -d ${D}${bindir}
    install -m 0744 ${S}/scripts/secret-vm-functions.sh ${D}${bindir}/secret-vm-functions.sh
    install -m 0744 ${S}/scripts/secret-vm-global-env.sh ${D}${bindir}/secret-vm-global-env.sh
    install -m 0744 ${S}/scripts/secret-vm-start.sh ${D}${bindir}/secret-vm-start.sh
    install -m 0744 ${S}/scripts/secret-vm-network-setup.sh ${D}${bindir}/secret-vm-network-setup.sh
    install -m 0744 ${S}/scripts/secret-vm-generate-cert.sh ${D}${bindir}/secret-vm-generate-cert.sh
    install -m 0744 ${S}/scripts/secret-vm-keygen.sh ${D}${bindir}/secret-vm-keygen.sh
    install -m 0744 ${S}/scripts/renew-certificates.sh ${D}${bindir}/renew-certificates.sh
    install -m 0744 ${S}/scripts/gpu-attest.py ${D}${bindir}/gpu-attest
    install -m 0744 ${S}/scripts/utils.sh ${D}${bindir}/utils.sh

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/services/secret-vm-attest-rest.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/services/secret-vm-docker-start.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/services/secret-vm-startup.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/services/secret-vm-network-setup.service ${D}${systemd_unitdir}/system/

    install -d ${D}${systemd_unitdir}/network
    install -m 0644 ${S}/configs/10-enp.network ${D}${systemd_unitdir}/network

    install -d ${D}${sysconfdir}/
    install -m 0644 ${S}/configs/tdx-attest.conf ${D}${sysconfdir}/
}

FILES:${PN} = "${bindir}/secret-vm-start.sh \
               ${bindir}/secret-vm-network-setup.sh \
               ${bindir}/secret-vm-generate-cert.sh \
               ${bindir}/secret-vm-keygen.sh \
               ${bindir}/secret-vm-global-env.sh \
               ${bindir}/secret-vm-functions.sh \
               ${bindir}/renew-certificates.sh \
               ${bindir}/utils.sh \
               ${systemd_unitdir}/system/secret-vm-attest-rest.service \
               ${systemd_unitdir}/system/secret-vm-docker-start.service \
               ${systemd_unitdir}/system/secret-vm-startup.service \
               ${systemd_unitdir}/system/secret-vm-network-setup.service \
               ${systemd_unitdir}/network/10-enp.network \
               ${sysconfdir}/tdx-attest.conf"
FILES:${PN}-gpu = "${bindir}/gpu-attest"
