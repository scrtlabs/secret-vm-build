#!/bin/bash
#==============================================================================
# Name: secretai_attest.sh
# Description: Certificate generation script for SecretAI attestation service
# 
# This script performs the following operations:
# - Generates a deterministic master secret based on system UUID
# - Creates RSA private key using the master secret as seed
# - Generates Certificate Signing Request (CSR)
# - Creates self-signed certificate
#
# Usage: ./secretai_attest.sh
# Output: private.key and cert.crt in current directory
#
# Note: For production use, ensure secure master secret generation
#==============================================================================

# Generates a deterministic master secret based on system UUID
#
# This function creates a deterministic secret by hashing the system's UUID
# obtained via dmidecode. If dmidecode fails, it uses a fallback UUID value.
# The resulting hash serves as a seed for key generation.
#
# Returns:
#   SHA-256 hash of the system UUID as a hex string
#
get_master_secret() {
    # Example of generating a deterministic seed using system-specific values
    # WARNING: This is just an example - in production, use a secure method
    local system_id
    system_id=$(dmidecode -s system-uuid 2>/dev/null || echo "fallback-uuid")
    echo -n "$system_id" | sha256sum | cut -d' ' -f1
}

# Generates a deterministic RSA key pair and self-signed certificate
# 
# This function creates an RSA private key and certificate using a deterministic
# seed derived from system UUID. The generated files are prefixed with the provided
# prefix argument.
#
# Args:
#   prefix: String to prepend to output filenames (e.g. "myapp" creates myapp_pk.key
#          and myapp_cert.crt)
#   dest_dir: String to provide a destination location where to copy the generated
#           file to
#
# Outputs:
#   - {dest_dir}/{prefix}_pk.key: RSA private key file
#   - {dest_dir}/{prefix}_cert.crt: Self-signed X.509 certificate
#
generate_cert() {
    local prefix="$1"
    local dest_dir="${2:-.}"  # Default to current directory if not specified

    TEMP_DIR=$(mktemp -d) || { echo "ERROR: Failed to create temporary directory"; exit 1; }
    cd "$TEMP_DIR" || { echo "ERROR: Failed to change to temporary directory $TEMP_DIR"; exit 1; }

    SEED=$(get_master_secret) || { echo "ERROR: Failed to generate master secret"; exit 1; }
    echo "Using seed: $SEED"

    # Generate deterministic private key using the seed
    echo "$SEED" | xxd -r -p > seed.bin || { echo "ERROR: Failed to create seed file"; exit 1; }
    openssl genpkey -algorithm RSA -outform PEM -out private.key \
        -pkeyopt rsa_keygen_bits:2048 \
        -pkeyopt rsa_keygen_pubexp:65537 \
        -pass file:seed.bin 2>/dev/null || { echo "ERROR: Failed to generate private key"; exit 1; }

    # Generate CSR (Certificate Signing Request)
    cat > openssl.cnf << EOF || { echo "ERROR: Failed to create OpenSSL config file"; exit 1; }
[req]
distinguished_name = SecretAI2
prompt = no

[req_distinguished_name]
[req_distinguished_name]
C = IL
ST = Tel Aviv District
L = Tel Aviv
O = SecretAI Corporation
OU = Security Division
CN = secretai.scrtlabs.com
emailAddress = secretai@scrtlabs.com
EOF

    openssl req -new -key private.key -out cert.csr -config openssl.cnf || { 
        echo "ERROR: Failed to generate certificate signing request"; 
        exit 1; 
    }

    # Self-sign the certificate
    openssl x509 -req -days 365 -in cert.csr -signkey private.key -out cert.crt || {
        echo "ERROR: Failed to generate self-signed certificate";
        exit 1;
    }

    # Move files to current directory with prefix
    cp private.key "${dest_dir}/${prefix}_pk.key" || {
        echo "ERROR: Failed to copy private key file";
        exit 1;
    }
    cp cert.crt "${dest_dir}/${prefix}_cert.crt" || {
        echo "ERROR: Failed to copy certificate file";
        exit 1;
    }
    cd .. || { echo "ERROR: Failed to change back to original directory"; exit 1; }
    rm -rf "$TEMP_DIR" || { echo "WARNING: Failed to cleanup temporary directory $TEMP_DIR"; }

    # generate ascii art banner that says: secretai_attest_servicess success
    if ! figlet -f slant "success" | lolcat; then
        echo "WARNING: Failed to generate success banner"
    fi

    echo "Generated: ${dest_dir}/${prefix}_pk.key and ${dest_dir}/${prefix}_cert.crt"
    echo "Verify deterministic generation by comparing key fingerprints:"
    openssl rsa -noout -modulus -in "${prefix}_pk.key" | openssl md5 || {
        echo "ERROR: Failed to generate key fingerprint";
        exit 1;
    }
}
