BBCLASSEXTEND = "native nativesdk"
SUMMARY = "NVIDIA Attestation SDK"
DESCRIPTION = "Python-based SDK that interacts with the NV Attestation Platform"
HOMEPAGE = "https://github.com/NVIDIA/nvtrust"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6aeb846e84938ab20e7310fedd9a1217"

SRC_URI = "git://github.com/NVIDIA/nvtrust.git;protocol=https;branch=main"
SRCREV = "368b9f95cf6a33e161ca8c0aac8a4c9528ea6ae7"

S = "${WORKDIR}/git/guest_tools/attestation_sdk"

inherit python_poetry_core

DEPENDS += "\
    python3-cryptography-native \
    python3-ecdsa-native \
    python3-lxml-native \
    python3-signxml-native \
    python3-xmlschema-native \
    python3-pyopenssl-native \
    python3-pyjwt-native \
    python3-requests-native \
    python3-aiohttp-native \
"

RDEPENDS:${PN} += "\
    python3-cryptography \
    python3-ecdsa \
    python3-lxml \
    python3-signxml \
    python3-xmlschema \
    python3-pyopenssl \
    python3-pyjwt \
    python3-requests \
    python3-aiohttp \
    python3-nv-local-gpu-verifier \
"

# Skip sanity checks to avoid errors related to dependencies
INSANE_SKIP:${PN} += "installed-vs-shipped"

do_install:append() {
    # Install license file
    install -d ${D}${datadir}/licenses/${BPN}
    install -m 0644 ${S}/LICENSE ${D}${datadir}/licenses/${BPN}/
}

# The package is zip-safe but might require specific file handling
FILES:${PN} += "\
    ${datadir}/licenses/${BPN} \
"
