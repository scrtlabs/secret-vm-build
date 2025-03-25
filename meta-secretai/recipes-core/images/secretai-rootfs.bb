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
                   python3-bs4 \
                   python3-flask \
                   python3-flask-sslify \
                   python3-tensorboardx \
                   rest-server \
                   docker-compose"

IMAGE_FSTYPES = "cpio"
