SUMMARY = "Minimal Root Filesystem"
LICENSE = "MIT"

inherit core-image


PACKAGE_INSTALL = "base-files \
                   attest-tool \
                   crypt-tool \
                   busybox \
                   systemd \
                   openssh \
                   kms-tool \
                   caddy \
                   rest-server \
                   docker-compose"

IMAGE_FSTYPES = "cpio"
