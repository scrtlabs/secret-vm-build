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
                   docker-compose"

IMAGE_FSTYPES = "cpio"
