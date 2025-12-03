include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=amd;protocol=https"
SRCREV = "22c82bd6151e8b3cdb6cb7c41520f22c62a72c96"

RDEPENDS:${PN} += "snpguest"
