include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=amd;protocol=https"
SRCREV = "2b6ff31d5df56cedf6a6bba89ba214e52353c6f1"

RDEPENDS:${PN} += "snpguest"
