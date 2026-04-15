include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=ita-jwt;protocol=https"
SRCREV = "7f182a50e87b63b70fe90328849c4456deb37cf3"

RDEPENDS:${PN} += "snpguest"
