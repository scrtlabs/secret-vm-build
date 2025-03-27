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

RDEPENDS:${PN} += "python3-pyjwt \
                   python3-nv-local-gpu-verifier \
                   python3-requests"
