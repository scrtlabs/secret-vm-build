SUMMARY = "Fast, simple object-to-object and broadcast signaling"
HOMEPAGE = "https://github.com/jek/blinker"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=42cd19c88fc13d1307a4efd64ee90e4e"

SRC_URI = "gitsm://github.com/jek/blinker.git;branch=main;protocol=https"
SRCREV = "b757408f2c13121b52d215fe922f13fc17a69bea"

SRC_URI[sha256sum] = "b4ce2265a7abece45e7cc896e98dbebe6cead56bcf805a3d23136d145f5445bf"

S = "${WORKDIR}/git"

inherit pypi python_poetry_core

PV = "1.9.0"

RDEPENDS_${PN} += "python3-core python3-poetry-core"

FILES_${PN} += "${PYTHON_SITEPACKAGES_DIR}/blinker"
