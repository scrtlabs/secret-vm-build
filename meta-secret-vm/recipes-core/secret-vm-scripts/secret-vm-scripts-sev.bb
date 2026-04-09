include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=feat/resize-amd;protocol=https"
SRCREV = "3fe674b3c9d6c213ce682a8cfc2facb3e4802532"

RDEPENDS:${PN} += "snpguest"
