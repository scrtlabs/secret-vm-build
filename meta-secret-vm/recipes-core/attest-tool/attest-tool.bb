SUMMARY = "TDX attest tool"
DESCRIPTION = "${SUMMARY}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://${THISDIR}/files"
S = "${WORKDIR}/${THISDIR}/files"

DEPENDS += "intel-sgx-primitives"
RDEPENDS:${PN} += "intel-sgx-primitives"

do_compile() {
    ${CC} ${LDFLAGS} -O2 attest_tool.cpp -ltdx_attest -o attest-tool
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/attest-tool ${D}${bindir}
}
