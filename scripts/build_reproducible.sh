#!/bin/bash

set -x
SCRIPTS_DIR=$(realpath $(dirname ${BASH_SOURCE[0]}))
ROOT_DIR=$(realpath $SCRIPTS_DIR/..)
BUILD_DIR=$ROOT_DIR/build
POKY_DIR=$ROOT_DIR/poky
ARTIFACTS_DIR=$ROOT_DIR/artifacts

docker build --platform linux/amd64 -t secret-vm-build -f $SCRIPTS_DIR/docker/Dockerfile .

docker run --platform linux/amd64 --rm \
        --user $(id -u):$(id -g) \
        -v $ROOT_DIR:/secret-vm-build \
        -v $BUILD_DIR:/secret-vm-build/build \
        -v $ARTIFACTS_DIR:/secret-vm-build/artifacts \
        -w /secret-vm-build \
        secret-vm-build bash -e -c "scripts/build.sh"
