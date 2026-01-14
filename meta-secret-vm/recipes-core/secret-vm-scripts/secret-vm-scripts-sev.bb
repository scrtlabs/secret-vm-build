include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=amd;protocol=https"
SRCREV = "24ef72daa4ace174078ab2486b540b61e8c03cef"

RDEPENDS:${PN} += "snpguest"
