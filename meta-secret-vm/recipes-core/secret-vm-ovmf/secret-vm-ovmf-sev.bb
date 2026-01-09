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

do_compile:class-target() {
    export LFLAGS="${LDFLAGS}"
    PARALLEL_JOBS="${@oe.utils.parallel_make_argument(d, '-n %d')}"
    OVMF_ARCH="X64"
    if [ "${TARGET_ARCH}" != "x86_64" ] ; then
        OVMF_ARCH="IA32"
    fi

    export HOST_PREFIX="${HOST_PREFIX}"
    rm -f `ls ${S}/Conf/*.txt | grep -v ReadMe.txt`

    rm -rf ${WORKDIR}/ovmf
    mkdir ${WORKDIR}/ovmf
    
    FIXED_GCCVER=$(fixup_target_tools ${GCC_VER})
    bbnote FIXED_GCCVER is ${FIXED_GCCVER}
    build_dir="${S}/Build/AmdSev/RELEASE_${FIXED_GCCVER}"

    mkdir -p ${S}/OvmfPkg/AmdSev/Grub
    touch ${S}/OvmfPkg/AmdSev/Grub/grub.efi

    bbnote "Building AMD SEV Firmware (Monolithic)."
    rm -rf ${S}/Build/AmdSev

    ${S}/OvmfPkg/build.sh $PARALLEL_JOBS -a $OVMF_ARCH -b RELEASE -t ${FIXED_GCCVER} ${PACKAGECONFIG_CONFARGS} -p OvmfPkg/AmdSev/AmdSevX64.dsc

    ln ${build_dir}/FV/OVMF.fd ${WORKDIR}/ovmf/ovmf.fd
}

do_deploy:class-target() {
    # For use with "runqemu ovmf".
    for i in \
        ovmf \
        ; do
        cp ${WORKDIR}/ovmf/$i.fd ${DEPLOYDIR}/
        qemu-img convert -f raw -O qcow2 ${WORKDIR}/ovmf/$i.fd ${DEPLOYDIR}/$i.qcow2
    done
}
