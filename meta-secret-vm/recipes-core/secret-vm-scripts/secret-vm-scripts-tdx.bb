include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=master;protocol=https"
SRCREV = "175a15d78e6d11342618d81a6fe3581e8cbc2f3e"

do_install:append() {
    install -d ${D}${sysconfdir}/
    install -m 0644 ${S}/configs/tdx-attest.conf ${D}${sysconfdir}/
}

FILES:${PN} += "${sysconfdir}/tdx-attest.conf"
