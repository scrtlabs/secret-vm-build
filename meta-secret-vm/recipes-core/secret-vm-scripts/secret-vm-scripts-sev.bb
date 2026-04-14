include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=ita-jwt;protocol=https"
SRCREV = "08e04dbb8b369e9a504341750784f13c3d2d3267"

RDEPENDS:${PN} += "snpguest"
