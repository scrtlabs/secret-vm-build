include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=master;protocol=https"
SRCREV = "d1c70a88c78a2a77965c63709832046da0e71e76"

do_install:append() {
    install -d ${D}${sysconfdir}/
    install -m 0644 ${S}/configs/tdx-attest.conf ${D}${sysconfdir}/
}

FILES:${PN} += "${sysconfdir}/tdx-attest.conf"
