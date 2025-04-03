#!/bin/bash

set -ex

SCRIPTS_DIR=$(realpath $(dirname ${BASH_SOURCE[0]}))
TMP_PATH=$SCRIPTS_DIR/tmp
CPIO_IMG_ABSOLUTE_PATH=$(realpath $1)
DESTINATION_DIR=$(realpath $2)
FILENAME=$3
SOURCE_DATE_EPOCH="$(date -d20000101 -u +%s)"

pushd .

mkdir -p $TMP_PATH/rootfs
cd $TMP_PATH/rootfs
cpio -idmv < $CPIO_IMG_ABSOLUTE_PATH

xorriso \
    -preparer_id xorriso \
    -volume_date 'all_file_dates' "=$SOURCE_DATE_EPOCH" \
    -as mkisofs \
    -R \
    -uid 0 \
    -gid 0 \
    -output $DESTINATION_DIR/$FILENAME.iso \
    .

rm -rf $TMP_PATH

popd
