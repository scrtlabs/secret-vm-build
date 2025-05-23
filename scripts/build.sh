#!/bin/bash

set -ex

SCRIPTS_DIR=$(realpath $(dirname ${BASH_SOURCE[0]}))
ROOT_DIR=$(realpath $SCRIPTS_DIR/..)
BUILD_DIR=$ROOT_DIR/build
POKY_DIR=$ROOT_DIR/poky
ARTIFACTS_DIR=$ROOT_DIR/artifacts

setup() {
	#rm -rf $ARTIFACTS_DIR
	mkdir -p $ARTIFACTS_DIR

	source $POKY_DIR/oe-init-build-env $BUILD_DIR
	bitbake-layers add-layer $ROOT_DIR/meta-nvidia
	bitbake-layers add-layer $ROOT_DIR/meta-rust-bin
	bitbake-layers add-layer $ROOT_DIR/meta-openembedded/meta-oe
	bitbake-layers add-layer $ROOT_DIR/meta-openembedded/meta-python
	bitbake-layers add-layer $ROOT_DIR/meta-openembedded/meta-networking
	bitbake-layers add-layer $ROOT_DIR/meta-openembedded/meta-filesystems
	bitbake-layers add-layer $ROOT_DIR/meta-virtualization
	bitbake-layers add-layer $ROOT_DIR/meta-secret-vm
}

build() {
	DISTRO=secret-vm bitbake secret-vm-initramfs secret-vm-rootfs-dev secret-vm-rootfs-prod secret-vm-rootfs-gpu-dev secret-vm-rootfs-gpu-prod virtual/kernel secret-vm-ovmf
}

install() {
	DEPLOY_DIR=$(find $BUILD_DIR -maxdepth 2 -name deploy)
	IMAGES_DIR=$DEPLOY_DIR/images/qemux86-64
	cp -L $IMAGES_DIR/bzImage $ARTIFACTS_DIR/bzImage
	cp -L $IMAGES_DIR/secret-vm-rootfs-prod-qemux86-64.rootfs.cpio $ARTIFACTS_DIR/rootfs-prod.cpio
	cp -L $IMAGES_DIR/secret-vm-rootfs-dev-qemux86-64.rootfs.cpio $ARTIFACTS_DIR/rootfs-dev.cpio
	cp -L $IMAGES_DIR/secret-vm-rootfs-gpu-dev-qemux86-64.rootfs.cpio $ARTIFACTS_DIR/rootfs-gpu-dev.cpio
	cp -L $IMAGES_DIR/secret-vm-rootfs-gpu-prod-qemux86-64.rootfs.cpio $ARTIFACTS_DIR/rootfs-gpu-prod.cpio
	cp -L $IMAGES_DIR/secret-vm-initramfs-qemux86-64.rootfs.cpio.gz $ARTIFACTS_DIR/initramfs.cpio.gz
	cp -L $IMAGES_DIR/ovmf.fd $ARTIFACTS_DIR/ovmf.fd
	$SCRIPTS_DIR/cpio_to_iso.sh $ARTIFACTS_DIR/rootfs-prod.cpio $ARTIFACTS_DIR rootfs-prod.iso
	$SCRIPTS_DIR/cpio_to_iso.sh $ARTIFACTS_DIR/rootfs-dev.cpio $ARTIFACTS_DIR rootfs-dev.iso
	$SCRIPTS_DIR/cpio_to_iso.sh $ARTIFACTS_DIR/rootfs-gpu-prod.cpio $ARTIFACTS_DIR rootfs-gpu-prod.iso
	$SCRIPTS_DIR/cpio_to_iso.sh $ARTIFACTS_DIR/rootfs-gpu-dev.cpio $ARTIFACTS_DIR rootfs-gpu-dev.iso
	qemu-img create -f qcow2 $ARTIFACTS_DIR/encryptedfs.qcow2 300G
}

pushd .
setup
build
install
popd
