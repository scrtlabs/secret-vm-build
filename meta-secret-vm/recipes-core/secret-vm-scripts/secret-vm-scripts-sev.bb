include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=amd;protocol=https"
SRCREV = "c78035d93368a7a2e47ef7b2ed2ef857da289f7d"

RDEPENDS:${PN} += "snpguest"
