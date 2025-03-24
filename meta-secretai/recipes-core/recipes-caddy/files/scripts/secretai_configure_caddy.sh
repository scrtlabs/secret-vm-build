#!/bin/bash

source secretai_caddy_mappings.sh


# Configures and generates a Caddyfile for reverse proxy setup
# Args:
#   $1 - private_key_location: Path to the TLS private key file
#   $2 - certificate_location: Path to the TLS certificate file  
#   $3 - caddyfile_location: Output path where Caddyfile will be written
function configure_caddy() {
    # get the location of the private key and the certificate file
    local private_key_location=$1
    local certificate_location=$2
    local caddyfile_location=$3

    local caddy_rules = process_mappings "$certificate_location" "$private_key_location"

    # generate the Caddyfile
    echo "{
        debug
        order claive_reverse_proxy first
    }

    (cors) {
        header {
            Access-Control-Allow-Origin \"{header.origin}\"
            Access-Control-Allow-Headers \"Content-Type, Authorization, Referrer-Policy, priority, sec-ch-ua, sec-ch-ua-mobile, sec-ch-ua-platform\"
            Vary Origin
            Access-Control-Allow-Credentials \"true\"
            defer
        }
    }

    $caddy_rules" > $caddyfile_location
}
