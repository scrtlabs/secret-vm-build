SUMMARY = "SecretVM initramfs files"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit allarch

SRC_URI = "file://init-tdx \
           file://init-sev"
S = "${WORKDIR}"
FILES:${PN} = "*"

do_install() {
    if [ "${MACHINE}" = "secret-vm-sev" ]; then
        install -m 0755 ${S}/init-sev ${D}/init
    else
        install -m 0755 ${S}/init-tdx ${D}/init
    fi
}
