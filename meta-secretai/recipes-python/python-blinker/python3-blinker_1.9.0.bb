SUMMARY = "Fast Python signaling library"
HOMEPAGE = "https://github.com/jek/blinker"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9fd5a9352e49ee34a6f8f31724dfdfbc"

SRC_URI = "gitsm://github.com/jek/blinker.git;branch=master;protocol=https"
SRCREV = "48db99a8f7c28d3883dcd573bba14ea73c04a240"

S = "${WORKDIR}/git"

inherit setuptools3 pypi

PV = "1.9.0"
SRC_URI[sha256sum] = "a2c6199365e01e7b2f5c3c5e992f7c4f06e6cf8b1b6bb3de786991c973bdff82"

RDEPENDS_${PN} += "python3-core python3-setuptools"

FILES_${PN} += "${PYTHON_SITEPACKAGES_DIR}/blinker"