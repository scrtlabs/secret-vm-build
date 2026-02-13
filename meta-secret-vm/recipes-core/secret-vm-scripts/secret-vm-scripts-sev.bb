include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=amd;protocol=https"
SRCREV = "aeb3b9afa2122adfc711fa407b06e4e33244328e"

RDEPENDS:${PN} += "snpguest"
