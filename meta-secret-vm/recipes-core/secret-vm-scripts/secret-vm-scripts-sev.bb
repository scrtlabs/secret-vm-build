include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=amd;protocol=https"
SRCREV = "960f13a8b13a71cc2fa068a26ad11f66405ab95c"

RDEPENDS:${PN} += "snpguest"
