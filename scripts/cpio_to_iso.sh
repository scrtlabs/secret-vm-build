#!/bin/bash

set -ex

SCRIPTS_DIR=$(realpath $(dirname ${BASH_SOURCE[0]}))
TMP_PATH=$SCRIPTS_DIR/tmp
CPIO_IMG_ABSOLUTE_PATH=$(realpath $1)
DESTINATION_DIR=$(realpath $2)
FILENAME=$3
export SOURCE_DATE_EPOCH="$(date -d20000101 -u +%s)"

pushd .

mkdir -p $TMP_PATH/rootfs
cd $TMP_PATH/rootfs
cpio -idmv < $CPIO_IMG_ABSOLUTE_PATH

xorriso \
    -preparer_id xorriso \
    -volume_date 'all' "=$SOURCE_DATE_EPOCH" \
    -volume_date 'all_file_dates' "=$SOURCE_DATE_EPOCH" \
    -padding 0 \
    -as mkisofs \
    -iso-level 3 \
    -graft-points \
    -full-iso9660-filenames \
    -uid 0 \
    -gid 0 \
    -output $DESTINATION_DIR/$FILENAME \
    .

rm -rf $TMP_PATH

popd
