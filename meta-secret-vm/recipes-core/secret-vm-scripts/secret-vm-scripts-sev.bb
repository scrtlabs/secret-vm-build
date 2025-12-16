include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=amd;protocol=https"
SRCREV = "4b130244fab2e1a713c30b64860e2473bc20c9b7"

RDEPENDS:${PN} += "snpguest"
