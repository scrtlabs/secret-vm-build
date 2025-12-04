#!/bin/bash

set -e

SCRIPTS_DIR=$(realpath $(dirname ${BASH_SOURCE[0]}))
ROOT_DIR=$(realpath $SCRIPTS_DIR/..)
ARTIFACTS_DIR=$ROOT_DIR/artifacts
VM_NAME=${VM_NAME:-secretai-vm}
MEM_SIZE=2G
MAC_ADDRESS=54:52:00:91:55:50

qemu-system-x86_64 -D ${VM_NAME}.log \
                   -initrd $ARTIFACTS_DIR/sev/initramfs.cpio.gz \
                   -kernel $ARTIFACTS_DIR/sev/bzImage \
                   -append "console=ttyS0 loglevel=7 docker_compose_hash=11b588e50cb03b268af536297738861ffa32ddac42af9da29be1e527982ce0fe rootfs_hash=5ab0be75c391265d603b819f2b5e86a034991ed984773c45552f165803c5d9f1" \
                   -enable-kvm \
                   -name ${VM_NAME},process=${VM_NAME},debug-threads=on \
                   -bios $ARTIFACTS_DIR/sev/ovmf.fd \
                   -drive file=$ARTIFACTS_DIR/encryptedfs.qcow2,if=none,id=disk0,format=qcow2 \
                   -smp cores=1,threads=1,sockets=1 \
                   -m ${MEM_SIZE} \
                   -cpu EPYC \
                   -object sev-snp-guest,id=sev0,cbitpos=51,reduced-phys-bits=1 \
                   -nographic \
                   -nodefaults \
                   -serial mon:stdio \
                   -object memory-backend-ram,id=mem0,size=${MEM_SIZE} \
                   -machine q35,memory-encryption=sev0,vmport=off \
                   -virtfs local,path=$ROOT_DIR/config,security_model=mapped,readonly=on,mount_tag=guest_config \
                   -drive file=$ARTIFACTS_DIR/sev/rootfs-prod.iso,if=none,id=cdrom0,format=raw,media=cdrom,readonly=on \
                   -device virtio-scsi-pci,id=scsi0,disable-legacy=on,iommu_platform=on \
                   -device scsi-hd,drive=disk0 \
                   -device scsi-cd,drive=cdrom0 \
                   -device virtio-net-pci,netdev=net0,mac=${MAC_ADDRESS} \
                   -netdev tap,id=net0,ifname=tap_dev,script=no,downscript=no
