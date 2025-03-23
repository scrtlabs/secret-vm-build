SUMMARY = "Minimal Root Filesystem"
LICENSE = "MIT"

inherit core-image


PACKAGE_INSTALL = "base-files \
                   attest-tool \
                   crypt-tool \
                   busybox \
                   systemd \
                   openssh \
                   caddy \
                   user-config \
                   python3 \
                   python3-nv-attestation-sdk \
                   python3-nv-local-gpu-verifier \
                   python3-nvidia-ml-py \
                   python3-signxml \
                   docker-compose"

IMAGE_FSTYPES = "cpio"
