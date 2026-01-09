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

do_compile:class-target() {
    export LFLAGS="${LDFLAGS}"
    PARALLEL_JOBS="${@oe.utils.parallel_make_argument(d, '-n %d')}"
    OVMF_ARCH="X64"
    if [ "${TARGET_ARCH}" != "x86_64" ] ; then
        OVMF_ARCH="IA32"
    fi

    # The build for the target uses BaseTools/Conf/tools_def.template
    # from ovmf-native to find the compiler, which depends on
    # exporting HOST_PREFIX.
    export HOST_PREFIX="${HOST_PREFIX}"

    # BaseTools/Conf gets copied to Conf, but only if that does not
    # exist yet. To ensure that an updated template gets used during
    # incremental builds, we need to remove the copy before we start.
    rm -f `ls ${S}/Conf/*.txt | grep -v ReadMe.txt`

    # ${WORKDIR}/ovmf is a well-known location where do_install and
    # do_deploy will be able to find the files.
    rm -rf ${WORKDIR}/ovmf
    mkdir ${WORKDIR}/ovmf
    OVMF_DIR_SUFFIX="X64"
    if [ "${TARGET_ARCH}" != "x86_64" ] ; then
        OVMF_DIR_SUFFIX="Ia32" # Note the different capitalization
    fi
    FIXED_GCCVER=$(fixup_target_tools ${GCC_VER})
    bbnote FIXED_GCCVER is ${FIXED_GCCVER}
    build_dir="${S}/Build/Ovmf$OVMF_DIR_SUFFIX/RELEASE_${FIXED_GCCVER}"

    bbnote "Building without Secure Boot."
    rm -rf ${S}/Build/Ovmf$OVMF_DIR_SUFFIX
    ${S}/OvmfPkg/build.sh $PARALLEL_JOBS -a $OVMF_ARCH -b RELEASE -t ${FIXED_GCCVER} ${PACKAGECONFIG_CONFARGS}
    ln ${build_dir}/FV/OVMF.fd ${WORKDIR}/ovmf/ovmf.fd
    ln ${build_dir}/FV/OVMF_CODE.fd ${WORKDIR}/ovmf/ovmf.code.fd
    ln ${build_dir}/FV/OVMF_VARS.fd ${WORKDIR}/ovmf/ovmf.vars.fd
    ln ${build_dir}/${OVMF_ARCH}/Shell.efi ${WORKDIR}/ovmf/

    if ${@bb.utils.contains('PACKAGECONFIG', 'secureboot', 'true', 'false', d)}; then
        # Repeat build with the Secure Boot flags.
        bbnote "Building with Secure Boot."
        rm -rf ${S}/Build/Ovmf$OVMF_DIR_SUFFIX
        ${S}/OvmfPkg/build.sh $PARALLEL_JOBS -a $OVMF_ARCH -b RELEASE -t ${FIXED_GCCVER} ${PACKAGECONFIG_CONFARGS} ${OVMF_SECURE_BOOT_FLAGS}
        ln ${build_dir}/FV/OVMF.fd ${WORKDIR}/ovmf/ovmf.secboot.fd
        ln ${build_dir}/FV/OVMF_CODE.fd ${WORKDIR}/ovmf/ovmf.secboot.code.fd
        ln ${build_dir}/${OVMF_ARCH}/EnrollDefaultKeys.efi ${WORKDIR}/ovmf/
    fi
}

do_deploy:class-target() {
    # For use with "runqemu ovmf".
    for i in \
        ovmf \
        ovmf.code \
        ovmf.vars \
        ${@bb.utils.contains('PACKAGECONFIG', 'secureboot', 'ovmf.secboot ovmf.secboot.code', '', d)} \
        ; do
        cp ${WORKDIR}/ovmf/$i.fd ${DEPLOYDIR}/
        qemu-img convert -f raw -O qcow2 ${WORKDIR}/ovmf/$i.fd ${DEPLOYDIR}/$i.qcow2
    done

    if ${@bb.utils.contains('PACKAGECONFIG', 'secureboot', 'true', 'false', d)}; then
        # Create a test Platform Key and first Key Exchange Key to use with EnrollDefaultKeys
        openssl req -new -x509 -newkey rsa:2048 -keyout ${DEPLOYDIR}/OvmfPkKek1.key \
                -out ${DEPLOYDIR}/OvmfPkKek1.crt -nodes -days 20 -subj "/CN=OVMFSecBootTest"
        openssl x509 -in ${DEPLOYDIR}/OvmfPkKek1.crt -out ${DEPLOYDIR}/OvmfPkKek1.pem -outform PEM
    fi
}
