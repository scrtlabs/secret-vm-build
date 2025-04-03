#!/bin/bash

set -x

# Startup script
ATTEST_TOOL=attest-tool
#COLLATERAL_TOOL=./dcap_collateral_tool
CRYPT_TOOL=crypt-tool

#KMS_SERVICE_ID=0
SECURE_MNT=/mnt/secure
SECURE_FS_SIZE_MB=200480 # 200 GB
#SECURE_FS_SIZE_MB=50 

PATH_ATTESTATION_TDX=$SECURE_MNT/tdx_attestation.txt
PATH_ATTESTATION_GPU_1=$SECURE_MNT/gpu_attestation.txt
PATH_ATTESTATION_GPU_2=$SECURE_MNT/gpu_attestation_token.txt

# helper function, tests if a variable is a valid hex-encoded data
test_valid_hex_data()
{
    local var_name="$1"
    local var_value="${!var_name}"

    if [[ -n "$var_value" && "$var_value" =~ ^[[:xdigit:]]+$ ]]; then
        return 0
        # Valid hex
    else
        # echo "invalid value for " $var_name ": " $var_value
        g_Error=$(echo "invalid value for " $var_name ": " $var_value)
        return 1
    fi
}

# Get the master secret from kms contract, based on our attestation
get_master_secret()
{
    echo "----- Getting master secret -----"

    # get random 32 bytes
    #local seed=$(head -c 32 /dev/random | xxd -p -c 32)
    local seed=$($CRYPT_TOOL rand)
    if ! test_valid_hex_data "seed"; then
        return 1
    fi

    # use it to derive initial pubkey
    local pubkey=$($CRYPT_TOOL generate-key -s $seed)
    if ! test_valid_hex_data "pubkey"; then
        return 1
    fi

    # get attestation with this pubkey as report data
    echo "Getting initial attestation..."

    local quote=$($ATTEST_TOOL attest $pubkey)
    if ! test_valid_hex_data "quote"; then
        return 1
    fi

    #local collateral=$($COLLATERAL_TOOL $quote |sed -n '3p')
    local collateral=$(curl -s -X POST https://pccs.scrtlabs.com/dcap-tools/quote-parse -H "Content-Type: application/json" -d "{\"quote\": \"$quote\"}" |jq '.collateral')
    collateral=$(echo $collateral | sed 's/"//g') # remove quotes
    if ! test_valid_hex_data "collateral"; then
        return 1
    fi

    # Query kms contract
    echo "Querying KMS..."

    echo "Setting up Go dependencies..."
    go mod tidy

    local kms_res=$(go run kms_query.go $KMS_SERVICE_ID $quote $collateral)

    # the result must consist of 2 lines, which are encrypted master secret and the export pubkey respectively. Parse it.
    kms_res=$(echo "$kms_res" | xargs) # strip possible leading and trailing spaces

    read encrypted_secret export_pubkey <<< "$kms_res"
    if ! test_valid_hex_data "encrypted_secret"; then
        return 1
    fi

    if ! test_valid_hex_data "export_pubkey"; then
        return 1
    fi

    # finally decrypt the result
    master_secret=$($CRYPT_TOOL decrypt -s $seed -d $encrypted_secret -p $export_pubkey)
    if ! test_valid_hex_data "master_secret"; then
        return 1
    fi

    return 0
}

mount_secret_fs()
{
    local fs_passwd="$1"
    local fs_container_path="$2"
    local size_mbs="$3"

    # Check the type of the path of the container image
    if [ -b "$fs_container_path" ]; then
        echo "FS container is a block device."
    elif [ -f "$fs_container_path" ]; then
        echo "FS container is a file."
    else
        echo "Creating an FS container file."
        dd if=/dev/zero of=$fs_container_path bs=1M count=$size_mbs
    fi

    echo "Opening existing encrypted file system..."
    echo -n $fs_passwd | cryptsetup luksOpen $fs_container_path encrypted_volume2
    if [ $? -ne 0 ]; then
        echo "Creating encrypted file system..."
        echo -n $fs_passwd | cryptsetup luksFormat --pbkdf pbkdf2 $fs_container_path
        echo -n $fs_passwd | cryptsetup luksOpen $fs_container_path encrypted_volume2
        mkfs.ext4 /dev/mapper/encrypted_volume2
    fi

    echo "Mounting encrypted file system..."
    mkdir -p $SECURE_MNT
    mount /dev/mapper/encrypted_volume2 $SECURE_MNT

    # setting docker working dir to the mounted fs
    systemctl stop docker
    mkdir -p /etc/docker
    echo '{ "data-root": "/mnt/secure" }' > /etc/docker/daemon.json
    systemctl start docker

    chown $USER $SECURE_MNT
}

safe_remove_outdated()
{
    rm -f $PATH_ATTESTATION_GPU_1
    rm -f $PATH_ATTESTATION_GPU_2
    rm -f $PATH_ATTESTATION_TDX
}

finalize()
{
    local ssl_cert_path="$1"

    echo "Fetching fingerptint from SSL certificate..."
    local ssl_fingerprint=$(openssl x509 -in $ssl_cert_path -noout -fingerprint -sha256 | awk -F= '{gsub(":", "", $2); print $2}')

    if ! test_valid_hex_data "ssl_fingerprint"; then
        return 1
    fi

    safe_remove_outdated

    echo "SSL certificate fingerprint: $ssl_fingerprint"
    local report_data="${ssl_fingerprint}"

    if [ -n "$GPU_MODE" ]; then
        # get random 32 bytes
        local gpu_nonce=$($CRYPT_TOOL rand)
        if ! test_valid_hex_data "gpu_nonce"; then
            return 1
        fi

        gpu-attest secret-vm $gpu_nonce $PATH_ATTESTATION_GPU_1 $PATH_ATTESTATION_GPU_2

        if [ ! -e $PATH_ATTESTATION_GPU_1 ] || [ ! -e $PATH_ATTESTATION_GPU_2 ]; then
            echo "GPU attestation not created"
            return 1
        fi
        echo "GPU attestation nonce: $gpu_nonce"
	report_data="${report_data}${gpu_nonce}"
    fi

    if [ ${#report_data} -gt 128 ]; then
        g_Error=$(echo "reportdata length: ${#report_data}")
        return 1
    fi

    local quote=$($ATTEST_TOOL attest $report_data)
    if ! test_valid_hex_data "quote"; then
        return 1
    fi

    echo $quote > $PATH_ATTESTATION_TDX
    echo "TDX attestation done"

    return 0
}

g_Error=""

if [ -n "$1" ]; then
    
    if [ $1 = "finalize" ]; then

        if finalize $2; then
            echo "All done"
        else
            echo "Couldn't finalize startup: $g_Error"
        fi

    else

        if [ $1 = "clear" ]; then
            umount $SECURE_MNT
            rmdir $SECURE_MNT
            cryptsetup luksClose encrypted_volume2

        else
            echo "Invalid argument"
        fi
    fi

else

    echo "Performing startup sequence..."

    if [ -z "${KMS_SERVICE_ID}" ]; then

        echo "KMS service ID not set, using non-persistent encrypted fs"

        nonce=$($CRYPT_TOOL rand)
        if ! test_valid_hex_data "nonce"; then
            return 1
        fi

        mount_secret_fs $nonce /dev/vda $SECURE_FS_SIZE_MB

    else

        if get_master_secret; then
            mount_secret_fs $master_secret /dev/vda $SECURE_FS_SIZE_MB
            echo "$master_secret" > $SECURE_MNT/master_secret.txt
        else
            echo "Couldn't get master secret: $g_Error"
            mount_secret_fs "12345" "./encrypted_dummy.img" 2
        fi

    fi

    safe_remove_outdated

    $ATTEST_TOOL report > $SECURE_MNT/self_report.txt
fi

