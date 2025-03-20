#!/bin/bash

set -e

SCRIPTS_DIR=$(realpath $(dirname ${BASH_SOURCE[0]}))
ROOT_DIR=$(realpath $SCRIPTS_DIR/..)
ARTIFACTS_DIR=$ROOT_DIR/artifacts
VM_NAME=${VM_NAME:-secretai-vm}
NVIDIA_DEV_ID=44:00.0

qemu-system-x86_64 \
                   -enable-kvm \
                   -cpu host \
                   -m 16G \
                   -smp cores=16,threads=2,sockets=2 \
                   -nographic \
                   -vga none \
                   -nodefaults \
                   -object '{"qom-type":"tdx-guest","id":"tdx","quote-generation-socket":{"type": "vsock", "cid":"2","port":"4050"}}' \
                   -machine q35,kernel_irqchip=split,confidential-guest-support=tdx,hpet=off \
                   -name $VM_NAME,process=$VM_NAME,debug-threads=on \
                   -device virtio-net-pci,netdev=nic0_td,mac=02:6a:df:9e:e2:ee \
                   -netdev user,id=nic0_td,hostfwd=tcp::2230-:22 \
                   -drive file=$ARTIFACTS_DIR/rootfs.qcow2,if=none,id=virtio-disk0 \
                   -device virtio-blk-pci,drive=virtio-disk0 \
                   -pidfile /tmp/$VM_NAME.pid \
                   -device vhost-vsock-pci,guest-cid=7 \
                   -bios /shared/custom/tdx-linux/edk2/OVMF-c4c99e41-574b-44b2-88f5-8ae904b6aa1b.fd \
                   -kernel $ARTIFACTS_DIR/bzImage \
                   -initrd $ARTIFACTS_DIR/initramfs.cpio.gz \
                   -append "console=ttyS0 loglevel=7 clearcpuid=mtrr,rtmr ro" \
                   -serial mon:stdio \
                   -object iommufd,id=iommufd0 \
                   -device pcie-root-port,id=pci.1,bus=pcie.0 \
                   -device vhost-vsock-pci,guest-cid=10 \
                   -device vfio-pci,host=${NVIDIA_DEV_ID},bus=pci.1,iommufd=iommufd0 \
                   -fw_cfg name=opt/ovmf/X-PciMmio64,string=262144 \
                   -virtfs local,path=$ROOT_DIR/config,security_model=mapped,readonly=on,mount_tag=guest_config
