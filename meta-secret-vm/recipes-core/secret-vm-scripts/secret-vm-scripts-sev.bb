include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=amd;protocol=https"
SRCREV = "418d5b201c1fa5befdc2701117b79cfaf9e289ba"

RDEPENDS:${PN} += "snpguest"
