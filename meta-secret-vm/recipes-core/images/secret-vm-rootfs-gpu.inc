include secret-vm-rootfs-common.inc

IMAGE_INSTALL:append:secret-vm-sev = " secret-vm-scripts-sev-gpu nvidia nvidia-firmware"
IMAGE_INSTALL:append:secret-vm-tdx = " secret-vm-scripts-tdx-gpu nvidia-legacy nvidia-legacy-firmware"

IMAGE_INSTALL += "acpid \
                  nvidia-persistenced \
                  nvidia-container-toolkit \
                  kernel-module-video"
