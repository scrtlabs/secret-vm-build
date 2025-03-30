#!/bin/bash

set -ex

# ssl cert creation routines
source secret-vm-generate-cert.sh

# set nvidia system ready flag for confidential computing
nvidia-smi conf-compute -srs 1
nvidia-ctk runtime configure --runtime=docker
nvidia-ctk config --set nvidia-container-cli.no-cgroups --in-place

CERT_DIR=/mnt/secure/cert
CERT_NAME=secret_vm
CERT_PATH=$CERT_DIR/"$CERT_NAME"_cert.pem
DOMAIN_NAME=tee-demo2.scrtlabs.com
DOMAIN_EMAIL=info@scrtlabs.com

startup.sh

if [ ! -e $CERT_PATH ]; then
    echo "SSL certificate not ready yet. Attempting to generate..."
    generate_cert $CERT_NAME $CERT_DIR $DOMAIN_NAME $DOMAIN_EMAIL
fi

startup.sh finalize $CERT_PATH
