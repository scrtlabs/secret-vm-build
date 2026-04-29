include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=amd;protocol=https"
SRCREV = "4c9926a6b58239ebb800c0fb814aed4b07edc891"

RDEPENDS:${PN} += "snpguest"
