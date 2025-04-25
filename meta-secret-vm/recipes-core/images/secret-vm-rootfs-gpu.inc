include secret-vm-rootfs-common.inc

IMAGE_INSTALL += "secret-vm-scripts-gpu \
                  acpid \
                  nvidia \
                  nvidia-firmware \
                  nvidia-persistenced \
                  nvidia-container-toolkit \
                  kernel-module-video"
