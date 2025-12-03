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

	mkdir -p $BUILD_DIR/conf/multiconfig
    
	echo 'MACHINE = "secret-vm-tdx"' > $BUILD_DIR/conf/multiconfig/tdx.conf
	echo 'TMPDIR = "${TOPDIR}/tmp-tdx"' >> $BUILD_DIR/conf/multiconfig/tdx.conf

	echo 'MACHINE = "secret-vm-sev"' > $BUILD_DIR/conf/multiconfig/sev.conf
	echo 'TMPDIR = "${TOPDIR}/tmp-sev"' >> $BUILD_DIR/conf/multiconfig/sev.conf

	if ! grep -q "BBMULTICONFIG" $BUILD_DIR/conf/local.conf; then
		echo 'BBMULTICONFIG = "tdx sev"' >> $BUILD_DIR/conf/local.conf
	fi
}

build() {
	DISTRO=secret-vm bitbake \
		mc:tdx:secret-vm-initramfs mc:tdx:secret-vm-rootfs-dev mc:tdx:secret-vm-rootfs-prod mc:tdx:secret-vm-rootfs-gpu-dev mc:tdx:secret-vm-rootfs-gpu-prod mc:tdx:virtual/kernel mc:tdx:virtual/ovmf \
		mc:sev:secret-vm-initramfs mc:sev:secret-vm-rootfs-dev mc:sev:secret-vm-rootfs-prod mc:sev:virtual/kernel mc:sev:virtual/ovmf
}

install() {
	TDX_DEPLOY=$BUILD_DIR/tmp-tdx-glibc/deploy/images/secret-vm-tdx
	mkdir -p $ARTIFACTS_DIR/tdx
	cp -L $TDX_DEPLOY/bzImage $ARTIFACTS_DIR/tdx/bzImage
	cp -L $TDX_DEPLOY/secret-vm-rootfs-prod-secret-vm-tdx.rootfs.cpio $ARTIFACTS_DIR/tdx/rootfs-prod.cpio
	cp -L $TDX_DEPLOY/secret-vm-rootfs-dev-secret-vm-tdx.rootfs.cpio $ARTIFACTS_DIR/tdx/rootfs-dev.cpio
	cp -L $TDX_DEPLOY/secret-vm-rootfs-gpu-dev-secret-vm-tdx.rootfs.cpio $ARTIFACTS_DIR/tdx/rootfs-gpu-dev.cpio
	cp -L $TDX_DEPLOY/secret-vm-rootfs-gpu-prod-secret-vm-tdx.rootfs.cpio $ARTIFACTS_DIR/tdx/rootfs-gpu-prod.cpio
	cp -L $TDX_DEPLOY/secret-vm-initramfs-secret-vm-tdx.rootfs.cpio.gz $ARTIFACTS_DIR/tdx/initramfs.cpio.gz
	cp -L $TDX_DEPLOY/ovmf.fd $ARTIFACTS_DIR/tdx/ovmf.fd

	SEV_DEPLOY=$BUILD_DIR/tmp-sev-glibc/deploy/images/secret-vm-sev
	mkdir -p $ARTIFACTS_DIR/sev
	cp -L $SEV_DEPLOY/bzImage $ARTIFACTS_DIR/sev/bzImage
	cp -L $SEV_DEPLOY/secret-vm-rootfs-prod-secret-vm-sev.rootfs.cpio $ARTIFACTS_DIR/sev/rootfs-prod.cpio
	cp -L $SEV_DEPLOY/secret-vm-rootfs-dev-secret-vm-sev.rootfs.cpio $ARTIFACTS_DIR/sev/rootfs-dev.cpio
	cp -L $SEV_DEPLOY/secret-vm-initramfs-secret-vm-sev.rootfs.cpio.gz $ARTIFACTS_DIR/sev/initramfs.cpio.gz
	cp -L $SEV_DEPLOY/ovmf.fd $ARTIFACTS_DIR/sev/ovmf.fd

	$SCRIPTS_DIR/cpio_to_iso.sh $ARTIFACTS_DIR/tdx/rootfs-prod.cpio $ARTIFACTS_DIR/tdx rootfs-prod.iso
	$SCRIPTS_DIR/cpio_to_iso.sh $ARTIFACTS_DIR/tdx/rootfs-dev.cpio $ARTIFACTS_DIR/tdx rootfs-dev.iso
	$SCRIPTS_DIR/cpio_to_iso.sh $ARTIFACTS_DIR/tdx/rootfs-gpu-prod.cpio $ARTIFACTS_DIR/tdx rootfs-gpu-prod.iso
	$SCRIPTS_DIR/cpio_to_iso.sh $ARTIFACTS_DIR/tdx/rootfs-gpu-dev.cpio $ARTIFACTS_DIR/tdx rootfs-gpu-dev.iso
	$SCRIPTS_DIR/cpio_to_iso.sh $ARTIFACTS_DIR/sev/rootfs-prod.cpio $ARTIFACTS_DIR/sev rootfs-prod.iso
	$SCRIPTS_DIR/cpio_to_iso.sh $ARTIFACTS_DIR/sev/rootfs-dev.cpio $ARTIFACTS_DIR/sev rootfs-dev.iso
	qemu-img create -f qcow2 $ARTIFACTS_DIR/encryptedfs.qcow2 300G
}

pushd .
setup
build
install
popd
