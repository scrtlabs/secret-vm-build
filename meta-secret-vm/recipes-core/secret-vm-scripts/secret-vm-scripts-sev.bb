include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=amd;protocol=https"
SRCREV = "8c53daea14dc7301e3e6239d2329f3e325d0fd26"

RDEPENDS:${PN} += "snpguest"
