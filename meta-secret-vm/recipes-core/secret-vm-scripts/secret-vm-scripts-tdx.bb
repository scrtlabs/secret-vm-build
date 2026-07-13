include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=master;protocol=https"
SRCREV = "ef3e387f36bd2cc7b9997b97d52f036e37b874c8"

do_install:append() {
    install -d ${D}${sysconfdir}/
    install -m 0644 ${S}/configs/tdx-attest.conf ${D}${sysconfdir}/
}

FILES:${PN} += "${sysconfdir}/tdx-attest.conf"
