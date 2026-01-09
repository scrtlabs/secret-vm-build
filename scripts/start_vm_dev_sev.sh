#!/bin/bash

set -e

SCRIPTS_DIR=$(realpath $(dirname ${BASH_SOURCE[0]}))
ROOT_DIR=$(realpath $SCRIPTS_DIR/..)
ARTIFACTS_DIR=$ROOT_DIR/artifacts
VM_NAME=${VM_NAME:-secretai-vm}
MEM_SIZE=2G
MAC_ADDRESS=54:52:00:91:55:50

DOCKER_COMPOSE_HASH=$(sha256sum $ROOT_DIR/config/docker-compose.yaml)
ROOTFS_HASH=$(sha256sum $ARTIFACTS_DIR/sev/rootfs-dev.iso)

qemu-system-x86_64 -D ${VM_NAME}.log \
                   -initrd $ARTIFACTS_DIR/sev/initramfs.cpio.gz \
                   -kernel $ARTIFACTS_DIR/sev/bzImage \
                   -append "console=ttyS0 loglevel=7 docker_compose_hash=$DOCKER_COMPOSE_HASH rootfs_hash=$ROOTFS_HASH" \
                   -enable-kvm \
                   -name ${VM_NAME},process=${VM_NAME},debug-threads=on \
                   -bios $ARTIFACTS_DIR/sev/ovmf.fd \
                   -drive file=$ARTIFACTS_DIR/encryptedfs.qcow2,if=none,id=disk0,format=qcow2 \
                   -smp cores=1,threads=1,sockets=1 \
                   -m ${MEM_SIZE} \
                   -cpu EPYC \
                   -object sev-snp-guest,id=sev0,cbitpos=51,reduced-phys-bits=1,kernel-hashes=on \
                   -nographic \
                   -nodefaults \
                   -serial mon:stdio \
                   -object memory-backend-ram,id=mem0,size=${MEM_SIZE} \
                   -machine q35,memory-encryption=sev0,vmport=off \
                   -virtfs local,path=$ROOT_DIR/config,security_model=mapped,readonly=on,mount_tag=guest_config \
                   -drive file=$ARTIFACTS_DIR/sev/rootfs-dev.iso,if=none,id=cdrom0,format=raw,media=cdrom,readonly=on \
                   -device virtio-scsi-pci,id=scsi0,disable-legacy=on,iommu_platform=on \
                   -device scsi-hd,drive=disk0 \
                   -device scsi-cd,drive=cdrom0 \
                   -device virtio-net-pci,netdev=net0,mac=${MAC_ADDRESS} \
                   -netdev tap,id=net0,ifname=tap_dev,script=no,downscript=no
