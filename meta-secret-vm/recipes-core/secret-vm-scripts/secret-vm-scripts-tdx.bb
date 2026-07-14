include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=master;protocol=https"
SRCREV = "c34d422651d4b60b65ae0889fbd3d468def99f06"

do_install:append() {
    install -d ${D}${sysconfdir}/
    install -m 0644 ${S}/configs/tdx-attest.conf ${D}${sysconfdir}/
}

FILES:${PN} += "${sysconfdir}/tdx-attest.conf"
