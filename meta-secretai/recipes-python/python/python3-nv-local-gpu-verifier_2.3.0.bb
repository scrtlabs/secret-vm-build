SUMMARY = "NVIDIA Local GPU Verifier"
DESCRIPTION = "A Python-based tool that validates GPU measurements by comparing GPU runtime measurements with authenticated golden measurements"
HOMEPAGE = "https://github.com/NVIDIA/nvtrust"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=a411985ae7288e79d526516ccb65f9ed"

SRCREV = "368b9f95cf6a33e161ca8c0aac8a4c9528ea6ae7"
SRC_URI = "git://github.com/NVIDIA/nvtrust.git;protocol=https;branch=main"

S = "${WORKDIR}/git/guest_tools/gpu_verifiers/local_gpu_verifier"

inherit python_poetry_core

DEPENDS += "\
    python3-cryptography-native \
    python3-ecdsa-native \
    python3-lxml-native \
    python3-signxml-native \
    python3-xmlschema-native \
    python3-pyopenssl-native \
    python3-pyjwt-native \
"

RDEPENDS:${PN} += "\
    python3-cryptography \
    python3-ecdsa \
    python3-lxml \
    python3-signxml \
    python3-xmlschema \
    python3-pyopenssl \
    python3-pyjwt \
    python3-nvidia-ml-py \
"

# Skip sanity checks to avoid errors related to dependencies
INSANE_SKIP:${PN} += "installed-vs-shipped src-uri-bad"

do_install:append() {
    # Install license file
    install -d ${D}${datadir}/licenses/${BPN}
    install -m 0644 ${S}/LICENSE.txt ${D}${datadir}/licenses/${BPN}/
}

# The package is zip-safe but might require specific file handling
FILES:${PN} += "\
    ${datadir}/licenses/${BPN} \
"

BBCLASSEXTEND = "native nativesdk"
