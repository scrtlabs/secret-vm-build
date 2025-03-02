#!/bin/bash

set -ex

SCRIPTS_DIR=$(realpath $(dirname ${BASH_SOURCE[0]}))
TMP_PATH=$SCRIPTS_DIR/tmp
CPIO_IMG_ABSOLUTE_PATH=$(realpath $1)
DESTINATION_DIR=$(realpath $2)

mkdir $TMP_PATH
mkdir $TMP_PATH/rootfs
mkdir $TMP_PATH/mnt

cd $TMP_PATH/rootfs
cpio -idmv < $CPIO_IMG_ABSOLUTE_PATH
cd ../../

qemu-img create -f qcow2 $TMP_PATH/rootfs.qcow2 2G
qemu-img convert -f qcow2 -O raw $TMP_PATH/rootfs.qcow2 $TMP_PATH/rootfs.raw

LOOP_DEV=$(sudo losetup -f --show $TMP_PATH/rootfs.raw)
sudo parted $LOOP_DEV mklabel msdos
sudo parted $LOOP_DEV mkpart primary ext4 1MiB 100%
sudo mkfs.ext4 ${LOOP_DEV}p1

sudo mount ${LOOP_DEV}p1 $TMP_PATH/mnt
sudo cp -r $TMP_PATH/rootfs/* $TMP_PATH/mnt/
sync

sudo umount $TMP_PATH/mnt
sudo losetup -d $LOOP_DEV

qemu-img convert -f raw -O qcow2 $TMP_PATH/rootfs.raw $TMP_PATH/rootfs.qcow2
cp $TMP_PATH/rootfs.qcow2 $DESTINATION_DIR

rm -rf $TMP_PATH
