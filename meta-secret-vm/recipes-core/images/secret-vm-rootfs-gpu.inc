include secret-vm-rootfs-common.inc

IMAGE_INSTALL += "secret-vm-scripts-tdx-gpu \
                  acpid \
                  nvidia \
                  nvidia-firmware \
                  nvidia-persistenced \
                  nvidia-container-toolkit \
                  kernel-module-video"
