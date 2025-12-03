include secret-vm-ovmf-common.inc

SRC_URI = "gitsm://github.com/AMDESE/ovmf.git;branch=snp-latest;protocol=https \
           file://0001-ovmf-update-path-to-native-BaseTools.patch \
           file://0002-BaseTools-makefile-adjust-to-build-in-under-bitbake.patch \
           file://0003-debug-prefix-map.patch \
           file://0004-reproducible-sev.patch \
           file://0005-Declare-ProcessLibraryConstructorList.patch \
           "

PV = "edk2-stable202502"
SRCREV = "fbe0805b2091393406952e84724188f8c1941837"
CVE_VERSION = "${@d.getVar('PV').split('stable')[1]}"
