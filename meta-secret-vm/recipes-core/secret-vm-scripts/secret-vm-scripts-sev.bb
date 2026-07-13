include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=amd;protocol=https"
SRCREV = "37bb4f4287156451e846ae7864372b16414d3a3c"

RDEPENDS:${PN} += "snpguest"
