include secret-vm-scripts-common.inc

SRC_URI = "git://github.com/scrtlabs/secret-vm-ops.git;branch=amd;protocol=https"
SRCREV = "3f7d4616b4a773034a39bd3f55eeebda8ba675b8"

RDEPENDS:${PN} += "snpguest"
