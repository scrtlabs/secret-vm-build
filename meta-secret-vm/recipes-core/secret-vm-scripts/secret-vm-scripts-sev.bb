include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=amd;protocol=https"
SRCREV = "5584d600130725528f57966ceb4b000b37e24559"

RDEPENDS:${PN} += "snpguest"
