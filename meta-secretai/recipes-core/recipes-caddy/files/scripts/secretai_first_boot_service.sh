#!/bin/bash
# SecretAI System Installation and Configuration Script
# ==================================================
#
# This script performs initial system setup and configuration for SecretAI services.
# It should only be run once during first boot/installation.
#
# Execution sequence:
# 1. Installs required system dependencies and packages
# 2. Builds and installs SecretAI attestation REST service
# 3. Builds and installs Caddy web server with required modules
# 4. Generates SSL certificates for secure communication
# 5. Configures Caddy web server with SSL and settings
# 6. Configures firewall rules and port access
#
# Installation paths:
# - Attestation service: /opt/secretai-attest-rest
# - Certificates: /etc/caddy/cert
# - Caddy config: /etc/caddy/Caddyfile
#
# Note: This script sources and executes functions from multiple dependency scripts
# that must be present in the same directory.


# Import dependencies installation functions and package requirements
source secretai_install_dependencies.sh

# Import attestation REST service build and installation functions
source secretai_build_install_attest.sh

# Import Caddy web server build and installation functions
source secretai_build_install_caddy.sh

# Import SSL certificate generation and management functions
source secretai_generate_cert.sh

# Import firewall configuration and port management functions
source secretai_configure_firewall.sh

# Import Caddy web server configuration functions
source secretai_configure_caddy.sh

# Import Caddy URL mapping and routing definitions
source secretai_cadd_mappings.sh

# Name of the certificate used for SSL/TLS encryption
CERT_NAME="secretai"

# Directory path where SSL certificates will be stored
CERT_DIR="/etc/caddy/cert"

# Installation directory for the SecretAI attestation REST service
ATTEST_REST_INSNTALL_DIR="/opt/secretai-attest-rest"

# Full path to the SSL private key file
PK_LOC="${CERT_DIR}/${CERT_NAME}_pk.key"

# Full path to the SSL certificate file
CERT_LOC="${CERT_DIR}/${CERT_NAME}_cert.crt"

# Path to the Caddy web server configuration file
CADDY_LOC="/etc/caddy/Caddyfile"

# check_and_install() - Defined in secretai_install_dependencies.sh
# Installs required system packages and dependencies for SecretAI
# Returns: 0 on success, 1 on failure
if ! check_and_install; then
    echo "Error: Installation failed"
    exit 1
fi

# Builds and installs the SecretAI attestation REST service
# Args:
#   $1: Installation directory path for the attestation service
# Returns: 0 on success, 1 on failure
if ! build_install_attest_rest "${ATTEST_REST_INSTALL_DIR}"; then
    echo "ERROR: Failed to build and install attest rest"
    exit 1
fi    echo "ERROR: Failed to build and install attest rest"
    exit 1
fi

# Builds and installs the Caddy web server with required modules
# Defined in: secretai_build_install_caddy.sh
# Returns: 0 on success, 1 on failure
if ! build_install_caddy; then
    echo "ERROR: Failed to build and install caddy"
    exit 1
fi

# Generates SSL certificates for secure communication
# Defined in: secretai_generate_cert.sh
# Args:
#   $1: Certificate name (e.g. "secretai")
#   $2: Directory path to store certificates
# Returns: 0 on success, 1 on failure
if ! generate_cert "$CERT_NAME" "$CERT_DIR" ; then
    echo "ERROR: Failed to generate certificates"
    exit 1
fi

# Configures the Caddy web server with SSL certificates and settings
# Defined in: secretai_configure_caddy.sh
# Args:
#   $1: Path to private key file
#   $2: Path to certificate file 
#   $3: Path to Caddyfile configuration
# Returns: 0 on success, 1 on failure
if ! configure_caddy "$PK_LOC" "$CERT_LOC" "$CADDY_LOC"; then
    echo "ERROR: Failed to configure caddy"
    exit 1
fi

# Configures UFW (Uncomplicated Firewall) with required port settings
# Defined in: secretai_configure_firewall.sh
# Args:
#   $1: Array of port numbers to open in firewall
# FIREWALL_PORTS defined in secretai_caddy_mappings.sh
# Returns: 0 on success, 1 on failure
if ! check_and_configure_ufw "${FIREWALL_PORTS[@]}"; then
    echo "ERROR: Failed to configure firewall"
    exit 1
fi
