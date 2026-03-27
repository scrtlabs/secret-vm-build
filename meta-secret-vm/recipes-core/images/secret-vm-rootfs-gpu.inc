include secret-vm-rootfs-common.inc

IMAGE_INSTALL:append:secret-vm-sev = " secret-vm-scripts-sev-gpu nvidia"
IMAGE_INSTALL:append:secret-vm-tdx = " secret-vm-scripts-tdx-gpu nvidia-legacy"

IMAGE_INSTALL += "acpid \
                  nvidia-firmware \
                  nvidia-persistenced \
                  nvidia-container-toolkit \
                  kernel-module-video"
