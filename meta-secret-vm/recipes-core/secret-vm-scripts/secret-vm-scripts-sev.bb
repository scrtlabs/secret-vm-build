include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=amd;protocol=https"
SRCREV = "6507bc544e97edee84e02dfab4d6a5d04915680f"

RDEPENDS:${PN} += "snpguest"
