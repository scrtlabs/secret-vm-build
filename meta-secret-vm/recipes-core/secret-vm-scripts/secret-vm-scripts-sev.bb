include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=amd;protocol=https"
SRCREV = "20f6183e772b0fbfb5ee2bd5f1a183a539237988"

RDEPENDS:${PN} += "snpguest"
