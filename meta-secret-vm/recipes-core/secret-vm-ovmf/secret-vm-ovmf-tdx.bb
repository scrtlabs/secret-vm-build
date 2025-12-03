include secret-vm-ovmf-common.inc

SRC_URI = "gitsm://github.com/tianocore/edk2.git;branch=master;protocol=https \
           file://0001-ovmf-update-path-to-native-BaseTools.patch \
           file://0002-BaseTools-makefile-adjust-to-build-in-under-bitbake.patch \
           file://0003-debug-prefix-map.patch \
           file://0004-reproducible-tdx.patch \
           file://0001-MdePkg-Fix-overflow-issue-in-BasePeCoffLib.patch \
           file://0005-Declare-ProcessLibraryConstructorList.patch \
           "

PV = "edk2-3a3b12cb"
SRCREV = "3a3b12cbdae2e89b0e365eb01c378891d0d9037c"
CVE_VERSION = "${@d.getVar('PV').split('-')[1]}"
