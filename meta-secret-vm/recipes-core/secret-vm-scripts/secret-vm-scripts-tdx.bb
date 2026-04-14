include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=ita-jwt;protocol=https"
SRCREV = "08e04dbb8b369e9a504341750784f13c3d2d3267"

PACKAGES += "${PN}-gpu"

RDEPENDS:${PN}-gpu += "python3 \
                       python3-nv-attestation-sdk"

do_install:append() {
    install -d ${D}${sysconfdir}/
    install -m 0644 ${S}/configs/tdx-attest.conf ${D}${sysconfdir}/

    install -d ${D}${bindir}
    install -m 0744 ${S}/scripts/gpu-attest.py ${D}${bindir}/gpu-attest
}

FILES:${PN} += "${sysconfdir}/tdx-attest.conf"
FILES:${PN}-gpu = "${bindir}/gpu-attest"
