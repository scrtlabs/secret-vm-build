#!/bin/bash

# Caddy Reverse Proxy Configuration Generator
# =========================================
#
# This script generates Caddy server configurations for reverse proxy setups with
# TLS termination and client certificate authentication. It processes a set of
# IP/port mappings to create secure reverse proxy rules.
#
# Key features:
# - Generates Caddy reverse proxy configurations from a template
# - Handles TLS termination with client certificate authentication
# - Configures CORS preflight request handling
# - Supports multiple proxy mappings with different IP/port combinations
# - Validates certificate and private key files
#
# Usage:
#   ./script.sh <certificate_path> <private_key_path>
#
# The script expects two arguments:
# 1. Path to TLS certificate file
# 2. Path to TLS private key file
#
# The generated configuration will create reverse proxy rules based on the
# defined MAPPINGS array, where each entry maps an external IP and port
# to an internal port.

# Caddy rule template for reverse proxy configuration
# Handles TLS termination with client certificate authentication
# and CORS preflight requests
#
# Placeholders:
# <EXTERNAL_IP>      - External IP address to listen on
# <EXTERNAL_PORT>    - External port to listen on
# <CERTIFICATE>      - Path to TLS certificate file
# <PRIVATE_KEY>      - Path to TLS private key file
# <INTERNAL_PORT>    - Internal port to proxy requests to
#
# Features:
# - TLS with client certificate authentication
# - CORS preflight handling with configurable origin
# - Reverse proxy to local service with header modifications
RULE_TEMPLATE="
<EXTERNAL_IP>:<EXTERNAL_PORT> {
    tls <CERTIFICATE> <PRIVATE_KEY> {
        client_auth {
            mode require
            trusted_ca_cert_file <CERTIFICATE>
        }
    }

    @cors_preflight method OPTIONS
    handle @cors_preflight {
        header {
            Access-Control-Allow-Origin \"{header.origin}\"
            Access-Control-Allow-Methods \"GET, POST, PUT, PATCH, DELETE, OPTIONS\"
            Access-Control-Allow-Headers \"Content-Type, Authorization, Referrer-Policy, priority, sec-ch-ua, sec-ch-ua-mobile, sec-ch-ua-platform\"
            Vary Origin
            Access-Control-Max-Age \"3600\"
        }
        respond \"\" 204
    }

    handle {
        import cors {header.origin}
        claive_reverse_proxy
        reverse_proxy localhost:<INTERNAL_PORT> {
            header_up -Origin
            header_up -Referer
            header_up Host ai1.myclaive.com
        }
    }
}"

# Development IP:Port Mappings
# ---------------------------
# Maps external IP addresses and ports to internal ports for reverse proxy
# Format: ["index"]="<external_ip> <external_port> <internal_port>"
#
# Current mappings:
#  ["0"]="<auto-detect> 25343 11343" # Secret Agent API
#  ["1"]="<auto-detect> 25434 11434" # Secret LLM
#  ["2"]="<auto-detect> 26343 29343" # Secret Attest
MAPPINGS=(
  ["0"]="<auto-detect> 25343 11343" # Secret Agent API
  ["1"]="<auto-detect> 25434 11434" # Secret LLM
  ["2"]="<auto-detect> 26343 29343" # Secret Attest
)

# Development Firewall Ports
# -------------------------
# List of ports to be opened in the firewall
# 22     - SSH access
# 25343  - Secret Agent API external port
# 25434  - Secret LLM external port  
# 26343  - Secret Attest external port
FIREWALL_PORTS=(22 25343 25434 26343)

# Processes IP/port mappings to generate Caddy reverse proxy configuration rules
# 
# Arguments:
#   $1 - certificate: Path to TLS certificate file
#   $2 - private_key: Path to TLS private key file
#
# Returns:
#   String containing concatenated Caddy configuration rules for all mappings
#   Each rule includes TLS termination, client auth, CORS handling and reverse proxy
process_mappings() {

    local certificate="$1"
    local private_key="$2"
    
    if [[ -z "${certificate}" || -z "${private_key}" ]]; then
        echo "Certificate and private key must be provided."
        exit 1
    fi
    
    if [[ ! -f "${certificate}" || ! -f "${private_key}" ]]; then
        echo "Certificate or private key file does not exist."
        exit 1
    fi

    local mappings=("${!MAPPINGS[@]}")
    local rules=""

    # Process each mapping entry
    for index in "${mappings[@]}"; do
        # Split the mapping string into array
        read -r ip_addr ext_port int_port <<< "${MAPPINGS[$index]}"
        
        # Auto-detect IP if placeholder is used
        if [[ "${ip_addr}" == "<auto-detect>" ]]; then
            ip_addr=$(hostname -I | awk '{print $1}')
        fi

        # Create rule by replacing placeholders
        local rule="${RULE_TEMPLATE}"
        rule="${rule//<CERTIFICATE>/${certificate}}"
        rule="${rule//<PRIVATE_KEY>/${private_key}}"
        rule="${rule//<EXTERNAL_IP>/${ip_addr}}"
        rule="${rule//<EXTERNAL_PORT>/${ext_port}}"
        rule="${rule//<INTERNAL_PORT>/${int_port}}"
        
        # Append to rules string
        rules="${rules}${rule}"
    done        

    echo "${rules}"
}



