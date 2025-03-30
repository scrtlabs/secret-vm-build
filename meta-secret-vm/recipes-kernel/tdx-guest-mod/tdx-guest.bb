SUMMARY = "TDX guest kernel module for Intel Trust Domain Extensions"
DESCRIPTION = "${SUMMARY}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit module

SRC_URI = "file://${THISDIR}/files"
S = "${WORKDIR}/${THISDIR}/files"

RPROVIDES:${PN} += "kernel-module-tdx-guest"
