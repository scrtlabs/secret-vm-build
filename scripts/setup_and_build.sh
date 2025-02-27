#!/bin/bash

set -ex

pushd .

SCRIPTS_DIR=$(realpath $(dirname ${BASH_SOURCE[0]}))

source $SCRIPTS_DIR/../poky/oe-init-build-env $SCRIPTS_DIR/../build 
bitbake-layers add-layer ../meta-secretai
bitbake-layers add-layer ../meta-rust-bin
bitbake-layers add-layer ../meta-openembedded/meta-oe
bitbake-layers add-layer ../meta-openembedded/meta-python
bitbake-layers add-layer ../meta-openembedded/meta-networking
bitbake-layers add-layer ../meta-openembedded/meta-filesystems
bitbake-layers add-layer ../meta-virtualization

DISTRO=secretai bitbake secretai-initramfs secretai-rootfs virtual/kernel
popd
