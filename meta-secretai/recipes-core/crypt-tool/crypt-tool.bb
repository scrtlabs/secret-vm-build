SUMMARY = "TDX crypt tool"
DESCRIPTION = "${SUMMARY}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://${THISDIR}/files"
S = "${WORKDIR}/${THISDIR}/files"

inherit cargo_bin

do_compile[network] = "1"
