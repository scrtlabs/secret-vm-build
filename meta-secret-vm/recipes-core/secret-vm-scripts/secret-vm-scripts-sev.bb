include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=ita-jwt;protocol=https"
SRCREV = "f9795c5237debc637bdeb1960bc999f910fb31f5"

RDEPENDS:${PN} += "snpguest"
