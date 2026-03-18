include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=amd;protocol=https"
SRCREV = "4d85e0ec70ba1fc0eb920570e9dc67b2f43fe06b"

RDEPENDS:${PN} += "snpguest"
