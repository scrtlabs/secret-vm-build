include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=ita-jwt;protocol=https"
SRCREV = "f4c0f09106262e1dc9fce07fc43d43bcde47fce7"

RDEPENDS:${PN} += "snpguest"
