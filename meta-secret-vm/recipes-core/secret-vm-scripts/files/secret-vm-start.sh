#!/bin/bash

set -ex

# ssl cert creation routines
source secret-vm-generate-cert.sh

# set nvidia system ready flag for confidential computing
if command -v nvidia-smi; then
    export GPU_MODE=1
fi

test -n "$GPU_MODE" && nvidia-smi conf-compute -srs 1

CERT_DIR=/mnt/secure/cert
CERT_NAME=secret_vm
CERT_PATH=$CERT_DIR/"$CERT_NAME"_cert.pem
DOMAIN_NAME=$(cat /mnt/config/secret-vm.json | jq -r '.domain_name')
DOMAIN_EMAIL=info@scrtlabs.com

startup.sh

if [ ! -e $CERT_PATH ]; then
    echo "SSL certificate not ready yet. Attempting to generate..."
    generate_cert $CERT_NAME $CERT_DIR $DOMAIN_NAME $DOMAIN_EMAIL
fi

startup.sh finalize $CERT_PATH

mkdir -p /mnt/secure/docker_wd
cp /mnt/config/docker-compose.yaml /mnt/secure/docker_wd

pushd .
cd /mnt/secure/docker_wd
# this file is optional
cp /mnt/config/docker-files.tar . && tar xvf ./docker-files.tar || true 
popd

if [ -n "$GPU_MODE" ]; then 
    nvidia-ctk runtime configure --runtime=docker
    nvidia-ctk config --set nvidia-container-cli.no-cgroups --in-place
    systemctl restart docker
fi
