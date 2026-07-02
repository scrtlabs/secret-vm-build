SUMMARY = "SecretVM scripts"
DESCRIPTION = "SecretVM scripts"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

S = "${WORKDIR}/git"

PACKAGES += "${PN}-gpu"

RDEPENDS:${PN}-gpu += "python3 \
                       python3-nv-attestation-sdk"

RDEPENDS:${PN} += "systemd \
                   bash \
                   curl \
                   jq \
                   openssl \
                   cryptsetup \
                   e2fsprogs \
                   signing-server \
                   kms-query \
                   gkms-utils \
                   svm-supervisor \
                   crypt-tool"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "secret-vm-attest-rest.service \
                         secret-vm-supervisor.service"
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
    install -m 0744 ${S}/scripts/utils.sh ${D}${bindir}/utils.sh
    install -m 0744 ${S}/scripts/get_poc_token.sh ${D}${bindir}/get_poc_token.sh
    install -m 0744 ${S}/scripts/gpu-attest.py ${D}${bindir}/gpu-attest

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/services/secret-vm-attest-rest.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/services/secret-vm-supervisor.service ${D}${systemd_unitdir}/system/

    install -d ${D}${systemd_unitdir}/network
    install -m 0644 ${S}/configs/10-enp.network ${D}${systemd_unitdir}/network
}

FILES:${PN} = "${bindir}/secret-vm-start.sh \
               ${bindir}/secret-vm-network-setup.sh \
               ${bindir}/secret-vm-generate-cert.sh \
               ${bindir}/secret-vm-keygen.sh \
               ${bindir}/secret-vm-global-env.sh \
               ${bindir}/secret-vm-functions.sh \
               ${bindir}/renew-certificates.sh \
               ${bindir}/utils.sh \
               ${bindir}/get_poc_token.sh \
               ${systemd_unitdir}/system/secret-vm-attest-rest.service \
               ${systemd_unitdir}/system/secret-vm-supervisor.service \
               ${systemd_unitdir}/network/10-enp.network"

FILES:${PN}-gpu = "${bindir}/gpu-attest"
