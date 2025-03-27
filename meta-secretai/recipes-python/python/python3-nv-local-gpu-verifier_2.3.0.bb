SUMMARY = "NVIDIA Local GPU Verifier"
DESCRIPTION = "A Python-based tool that validates GPU measurements by comparing GPU runtime measurements with authenticated golden measurements"
HOMEPAGE = "https://github.com/NVIDIA/nvtrust"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=a411985ae7288e79d526516ccb65f9ed"

SRCREV = "368b9f95cf6a33e161ca8c0aac8a4c9528ea6ae7"
SRC_URI = "git://github.com/NVIDIA/nvtrust.git;protocol=https;branch=main"

S = "${WORKDIR}/git/guest_tools/gpu_verifiers/local_gpu_verifier"

inherit python_poetry_core

RDEPENDS:${PN} += "\
    python3-ecdsa \
    python3-signxml \
    python3-pyopenssl \
    python3-nvidia-ml-py \
"
