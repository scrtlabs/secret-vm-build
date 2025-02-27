SUMMARY = "Minimal Root Filesystem"
LICENSE = "MIT"

inherit core-image


PACKAGE_INSTALL = "base-files \
                   busybox \
                   systemd \
                   openssh \
                   docker-compose"

IMAGE_FSTYPES = "cpio"
