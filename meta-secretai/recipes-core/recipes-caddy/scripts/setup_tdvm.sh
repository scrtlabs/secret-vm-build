#!/bin/bash
# ============================================================================
# Installation Script for SecretAI Services
# ============================================================================
#
# This script sets up and enables required systemd services for SecretAI:
#
# 1. Service Installation:
#    - Copies service definition files to /etc/systemd/system/
#      * secretai-first-boot.service - Handles first boot initialization
#      * secretai-attest-rest.service - Manages attestation REST service
#      * secretai-caddy.service - Manages Caddy web server
#
# 2. Script Installation: 
#    - Copies service scripts to /usr/local/bin/
#    - Sets executable permissions on:
#      * secretai_first_boot_service.sh
#      * secretai_attest_rest_service.sh
#
# 3. Service Activation:
#    - Reloads systemd daemon to recognize new services
#    - Enables services to start on system boot:
#      * secretai-first-boot
#      * secretai-attest-rest  
#      * secretai-caddy
#
# Note: This script requires sudo privileges to execute successfully
# ============================================================================

set -e

# Place systemd service on first boot
sudo cp secretai-first-boot.service /etc/systemd/system/
sudo cp secretai-attest-rest.service /etc/systemd/system/
sudo cp secretai-caddy.service /etc/systemd/system/

# Place installation scripts to /usr/local/bin
sudo cp secretai_build_install_attest.sh /usr/local/bin/
sudo chmod +x /usr/local/bin/secretai_build_install_attest.sh
sudo cp secretai_build_install_caddy.sh /usr/local/bin/
sudo chmod +x /usr/local/bin/secretai_build_install_caddy.sh
sudo cp secretai_caddy_mappings.sh /usr/local/bin/
sudo chmod +x /usr/local/bin/secretai_caddy_mappings.sh
sudo cp secretai_configure_caddy.sh /usr/local/bin/
sudo chmod +x /usr/local/bin/secretai_configure_caddy.sh
sudo cp secretai_configure_firewall.sh /usr/local/bin/
sudo chmod +x /usr/local/bin/secretai_configure_firewall.sh
sudo cp secretai_generate_cert.sh /usr/local/bin/
sudo chmod +x /usr/local/bin/secretai_generate_cert.sh
sudo cp secretai_install_dependencies.sh /usr/local/bin/
sudo chmod +x /usr/local/bin/secretai_install_dependencies.sh

#sudo cp secretai_caddy_service.sh /usr/local/bin/
#sudo chmod +x /usr/local/bin/secretai_caddy_service.sh
#sudo cp secretai_caddy_config.sh /usr/local/bin/
#sudo chmod +x /usr/local/bin/secretai_caddy_config.sh

sudo cp secretai_first_boot_service.sh /usr/local/bin/
sudo chmod +x /usr/local/bin/secretai_first_boot_service.sh
#sudo cp secretai_attest_rest_service.sh /usr/local/bin/
#sudo chmod +x /usr/local/bin/secretai_attest_rest_service.sh

# Reload systemd
sudo systemctl daemon-reload
# Enable the services
sudo systemctl enable secretai-first-boot
sudo systemctl enable secretai-attest-rest
sudo systemctl enable secretai-caddy
