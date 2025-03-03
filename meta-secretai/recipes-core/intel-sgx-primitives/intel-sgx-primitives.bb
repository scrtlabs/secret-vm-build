SUMMARY = "Intel SGX QuoteGeneration libs"
DESCRIPTION = "${SUMMARY}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "git://github.com/intel/SGXDataCenterAttestationPrimitives.git;protocol=https;branch=main \
           file://gnu-hash.patch"
SRCREV = "2562057f6a3149c03f5985826ffaba978ece58c2"
S = "${WORKDIR}/git"

do_patch() {
    cd ${S}
    patch -p1 < ${WORKDIR}/gnu-hash.patch
}

do_compile() {
    oe_runmake -C QuoteGeneration tdx_attest
}

do_install() {
    install -d ${D}${libdir}
    install -m 0755 ${S}/QuoteGeneration/build/linux/libtdx_attest.so ${D}${libdir}/libtdx_attest.so.1.22.100.3
    ln -sf libtdx_attest.so.1.22.100.3 ${D}${libdir}/libtdx_attest.so.1
    ln -sf libtdx_attest.so.1 ${D}${libdir}/libtdx_attest.so
    install -d ${D}${includedir}
    install -m 0755 ${S}/QuoteGeneration/quote_wrapper/tdx_attest/tdx_attest.h ${D}${includedir}/
}
