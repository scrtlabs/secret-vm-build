#!/bin/bash

set -e

SCRIPTS_DIR=$(realpath $(dirname ${BASH_SOURCE[0]}))
ROOT_DIR=$(realpath $SCRIPTS_DIR/..)
ARTIFACTS_DIR=$ROOT_DIR/artifacts
VM_NAME=${VM_NAME:-secretai-vm}
MEM_SIZE=2G
MAC_ADDRESS=54:52:00:91:55:50

qemu-system-x86_64 -D ${VM_NAME}.log \
                   -trace enable=tdx* -D tdx_trace.log \
                   -initrd $ARTIFACTS_DIR/initramfs.cpio.gz \
                   -kernel $ARTIFACTS_DIR/bzImage \
                   -append "console=ttyS0 loglevel=7 clearcpuid=mtrr,rtmr ro" \
                   -enable-kvm \
                   -name ${VM_NAME},process=${VM_NAME},debug-threads=on \
                   -bios $ARTIFACTS_DIR/ovmf.fd \
                   -cdrom $ARTIFACTS_DIR/rootfs-dev.iso \
                   -drive file=$ARTIFACTS_DIR/encryptedfs.qcow2,if=virtio \
                   -smp cores=1,threads=1,sockets=1 \
                   -m ${MEM_SIZE} \
                   -cpu host \
                   -object '{"qom-type":"tdx-guest","id":"tdx","quote-generation-socket":{"type": "vsock", "cid":"2","port":"4050"}}' \
                   -nographic \
                   -nodefaults \
                   -serial mon:stdio \
                   -object memory-backend-ram,id=mem0,size=${MEM_SIZE} \
                   -machine q35,kernel-irqchip=split,confidential-guest-support=tdx,hpet=off,memory-backend=mem0 \
                   -nodefaults \
                   -device pcie-root-port,id=pci.1,bus=pcie.0 \
                   -device vhost-vsock-pci,guest-cid=8 \
                   -fw_cfg name=opt/ovmf/X-PciMmio64,string=262144 \
                   -virtfs local,path=$ROOT_DIR/config,security_model=mapped,readonly=on,mount_tag=guest_config \
                   -device virtio-net-pci,netdev=nic1_td,mac=${MAC_ADDRESS} \
                   -netdev tap,id=nic1_td,ifname=tap_dev,script=no,downscript=no
