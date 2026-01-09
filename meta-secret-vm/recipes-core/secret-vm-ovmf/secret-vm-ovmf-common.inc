SUMMARY = "OVMF - UEFI firmware for Qemu and KVM"
DESCRIPTION = "OVMF is an EDK II based project to enable UEFI support for \
Virtual Machines. OVMF contains sample UEFI firmware for QEMU and KVM"
HOMEPAGE = "https://github.com/tianocore/tianocore.github.io/wiki/OVMF"
LICENSE = "BSD-2-Clause-Patent"
LICENSE:class-target = "${@bb.utils.contains('PACKAGECONFIG', 'secureboot', 'BSD-2-Clause-Patent & OpenSSL', 'BSD-2-Clause-Patent', d)}"
LIC_FILES_CHKSUM = "file://OvmfPkg/License.txt;md5=06357ddc23f46577c2aeaeaf7b776d65"

PROVIDES += "virtual/ovmf"

# Enabling Secure Boot adds a dependency on OpenSSL and implies
# compiling OVMF twice, so it is disabled by default. Distros
# may change that default.
PACKAGECONFIG ??= ""
PACKAGECONFIG += "${@bb.utils.contains('MACHINE_FEATURES', 'tpm', 'tpm', '', d)}"
PACKAGECONFIG += "${@bb.utils.contains('MACHINE_FEATURES', 'tpm2', 'tpm', '', d)}"
PACKAGECONFIG[secureboot] = ",,,"
PACKAGECONFIG[tpm] = "-D TPM_ENABLE=TRUE,-D TPM_ENABLE=FALSE,,"

# GCC12 trips on it
#see https://src.fedoraproject.org/rpms/edk2/blob/rawhide/f/0032-Basetools-turn-off-gcc12-warning.patch
BUILD_CFLAGS += "-Wno-error=stringop-overflow"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>edk2-stable.*)"

CVE_PRODUCT = "edk2"

CVE_STATUS[CVE-2014-8271] = "fixed-version: Fixed in svn_16280, which is an unusual versioning breaking version comparison."
CVE_STATUS[CVE-2014-4859] = "fixed-version: The CPE in the NVD database doesn't reflect correctly the vulnerable versions."
CVE_STATUS[CVE-2014-4860] = "fixed-version: The CPE in the NVD database doesn't reflect correctly the vulnerable versions."
CVE_STATUS[CVE-2019-14553] = "fixed-version: The CPE in the NVD database doesn't reflect correctly the vulnerable versions."
CVE_STATUS[CVE-2019-14559] = "fixed-version: The CPE in the NVD database doesn't reflect correctly the vulnerable versions."
CVE_STATUS[CVE-2019-14562] = "fixed-version: The CPE in the NVD database doesn't reflect correctly the vulnerable versions."
CVE_STATUS[CVE-2019-14563] = "fixed-version: The CPE in the NVD database doesn't reflect correctly the vulnerable versions."
CVE_STATUS[CVE-2019-14575] = "fixed-version: The CPE in the NVD database doesn't reflect correctly the vulnerable versions."
CVE_STATUS[CVE-2019-14586] = "fixed-version: The CPE in the NVD database doesn't reflect correctly the vulnerable versions."
CVE_STATUS[CVE-2019-14587] = "fixed-version: The CPE in the NVD database doesn't reflect correctly the vulnerable versions."

inherit deploy

PARALLEL_MAKE = ""

S = "${WORKDIR}/git"

DEPENDS = "nasm-native acpica-native ovmf-native util-linux-native"

EDK_TOOLS_DIR="edk2_basetools"

# OVMF has trouble building with the default optimization of -O2.
BUILD_OPTIMIZATION="-pipe"

# OVMF supports IA only, although it could conceivably support ARM someday.
COMPATIBLE_HOST:class-target='(i.86|x86_64).*'

# Additional build flags for OVMF with Secure Boot.
# Fedora also uses "-D SMM_REQUIRE -D EXCLUDE_SHELL_FROM_FD".
OVMF_SECURE_BOOT_EXTRA_FLAGS ??= ""
OVMF_SECURE_BOOT_FLAGS = "-DSECURE_BOOT_ENABLE=TRUE ${OVMF_SECURE_BOOT_EXTRA_FLAGS}"

export PYTHON_COMMAND = "${HOSTTOOLS_DIR}/python3"

do_patch[postfuncs] += "fix_basetools_location"
fix_basetools_location () {
}
fix_basetools_location:class-target() {
    # Replaces the fake path inserted by 0002-ovmf-update-path-to-native-BaseTools.patch.
    # Necessary for finding the actual BaseTools from ovmf-native.
    sed -i -e 's#BBAKE_EDK_TOOLS_PATH#${STAGING_BINDIR_NATIVE}/${EDK_TOOLS_DIR}#' ${S}/OvmfPkg/build.sh
}

do_patch[postfuncs] += "fix_iasl"
fix_iasl() {
}
fix_iasl:class-native() {
    # iasl is not installed under /usr/bin when building with OE.
    sed -i -e 's#/usr/bin/iasl#${STAGING_BINDIR_NATIVE}/iasl#' ${S}/BaseTools/Conf/tools_def.template
}

# Inject CC and friends into the build. LINKER already is in GNUmakefile.
# Must be idempotent and thus remove old assignments that were inserted
# earlier.
do_patch[postfuncs] += "fix_toolchain"
fix_toolchain() {
    sed -i \
        -e '/^\(CC\|CXX\|AS\|AR\|LD\|LINKER\) =/d' \
        -e '/^APPLICATION/a CC = ${CC}\nCXX = ${CXX}\nAS = ${AS}\nAR = ${AR}\nLD = ${LD}\nLINKER = $(CC)' \
        ${S}/BaseTools/Source/C/Makefiles/app.makefile
    sed -i \
        -e '/^\(CC\|CXX\|AS\|AR\|LD\)/d' \
        -e '/^VFR_CPPFLAGS/a CC = ${CC}\nCXX = ${CXX}\nAS = ${AS}\nAR = ${AR}\nLD = ${LD}' \
        ${S}/BaseTools/Source/C/VfrCompile/GNUmakefile
}
fix_toolchain:append:class-native() {
    # This tools_def.template is going to be used by the target ovmf and
    # defines which compilers to use. For the GCC toolchain definitions,
    # that will be ${HOST_PREFIX}gcc. However, "make" doesn't need that
    # prefix.
    #
    # Injecting ENV(HOST_PREFIX) matches exporting that value as env
    # variable in do_compile:class-target.
    sed -i \
        -e 's#\(ENV\|DEF\)(GCC.*_PREFIX)#ENV(HOST_PREFIX)#' \
        -e 's#ENV(HOST_PREFIX)make#make#' \
        ${S}/BaseTools/Conf/tools_def.template
    sed -i \
        -e '/^\(LFLAGS\|CFLAGS\) +=/d' \
        -e '/^LINKER/a LFLAGS += ${BUILD_LDFLAGS}\nCFLAGS += ${BUILD_CFLAGS}' \
        ${S}/BaseTools/Source/C/Makefiles/app.makefile \
        ${S}/BaseTools/Source/C/VfrCompile/GNUmakefile
    # Linking with gold fails:
    # internal error in do_layout, at ../../gold/object.cc:1821
    # make: *** [.../OUTPUT/Facs.acpi] Error 1
    # We intentionally hard-code the use of ld.bfd regardless of DISTRO_FEATURES
    # to make ovmf-native reusable across distros.
    sed -i \
        -e 's#^\(DEFINE GCC.*DLINK.*FLAGS  *=\)#\1 -fuse-ld=bfd#' \
        -e 's#-flto#-fno-lto#g' \
        -e 's#-DUSING_LTO##g' \
        ${S}/BaseTools/Conf/tools_def.template
}

# We disable lto above since the results are not reproducible and make it hard to compare
# binary build aretfacts to debug reproducibility problems.
# Surprisingly, if you disable lto, you see compiler warnings which are fatal. We therefore
# have to hack warnings overrides into GCC_PREFIX_MAP to allow it to build.

# We want to pass ${DEBUG_PREFIX_MAP} to gcc commands and also pass in
# --debug-prefix-map to nasm (we carry a patch to nasm for this). The
# tools definitions are built by ovmf-native so we need to pass this in
# at target build time when we know the right values.
export NASM_PREFIX_MAP = "--debug-prefix-map=${WORKDIR}=${TARGET_DBGSRC_DIR}"
export GCC_PREFIX_MAP = "${DEBUG_PREFIX_MAP} -Wno-stringop-overflow -Wno-maybe-uninitialized"

GCC_VER="$(${CC} -v 2>&1 | tail -n1 | awk '{print $3}')"

fixup_target_tools() {
    case ${1} in
      4.4.*)
        FIXED_GCCVER=GCC44
        ;;
      4.5.*)
        FIXED_GCCVER=GCC45
        ;;
      4.6.*)
        FIXED_GCCVER=GCC46
        ;;
      4.7.*)
        FIXED_GCCVER=GCC47
        ;;
      4.8.*)
        FIXED_GCCVER=GCC48
        ;;
      4.9.*)
        FIXED_GCCVER=GCC49
        ;;
      *)
        FIXED_GCCVER=GCC5
        ;;
    esac
    echo ${FIXED_GCCVER}
}

do_compile:class-native() {
    oe_runmake -C ${S}/BaseTools
}

do_install:class-native() {
    install -d ${D}/${bindir}/edk2_basetools
    find ${S}/BaseTools -name \*.pyc -exec rm -rf \{\} \;
    cp -r ${S}/BaseTools ${D}/${bindir}/${EDK_TOOLS_DIR}
}

# This always gets packaged because ovmf-shell-image depends on it.
# This allows testing that recipe in all configurations because it
# can always be part of a world build.
#
# However, EnrollDefaultKeys.efi is only included when Secure Boot is enabled.
PACKAGES =+ "ovmf-shell-efi"
FILES:ovmf-shell-efi = " \
    EnrollDefaultKeys.efi \
    efi/ \
"

DEPLOYDEP = ""
DEPLOYDEP:class-target = "qemu-system-native:do_populate_sysroot"
DEPLOYDEP:class-target += " ${@bb.utils.contains('PACKAGECONFIG', 'secureboot', 'openssl-native:do_populate_sysroot', '', d)}"
do_deploy[depends] += "${DEPLOYDEP}"

do_deploy() {
}
addtask do_deploy after do_compile before do_build

BBCLASSEXTEND = "native"
TOOLCHAIN = "gcc"
