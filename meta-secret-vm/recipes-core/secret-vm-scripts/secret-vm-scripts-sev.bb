include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=amd;protocol=https"
SRCREV = "1022aff8e5fc5635498a6c61ce13cbaa6618baa6"

RDEPENDS:${PN} += "snpguest"
