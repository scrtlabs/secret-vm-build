SUMMARY = "Fast, simple object-to-object and broadcast signaling"
HOMEPAGE = "https://github.com/jek/blinker"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=42cd19c88fc13d1307a4efd64ee90e4e"

SRC_URI = "gitsm://github.com/jek/blinker.git;branch=main;protocol=https"
SRCREV = "b757408f2c13121b52d215fe922f13fc17a69bea"

S = "${WORKDIR}/git"

inherit setuptools3

PV = "1.9.0"

RDEPENDS:${PN} += "python3-core python3-setuptools python3-multiprocessing"

BBCLASSEXTEND = "native nativesdk"
