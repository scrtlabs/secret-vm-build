# SecretAI First Boot Service

This document describes the first boot service script (`secretai_first_boot_service.sh`) that performs the initial system setup and configuration for SecretAI services.

## Overview

The first boot service is a comprehensive initialization script that prepares the TD Confidential VM environment. It executes exactly once during the first system boot and sets up all necessary components for the SecretAI attestation and proxy services.

## Execution Sequence

The script follows a structured initialization process:

1. **Dependencies Installation**
   - Sources from `secretai_install_dependencies.sh`
   - Installs required system packages and dependencies
   - Verifies successful installation before proceeding

2. **Attestation REST Service Setup**
   - Sources from `secretai_build_install_attest.sh`
   - Builds and installs the service in `/opt/secretai-attest-rest`
   - Sets up the service environment

3. **Caddy Web Server Installation**
   - Sources from `secretai_build_install_caddy.sh`
   - Builds Caddy with required custom modules
   - Installs the configured Caddy server

4. **SSL Certificate Generation**
   - Sources from `secretai_generate_cert.sh`
   - Generates SSL certificates for secure communication
   - Stores certificates in `/etc/caddy/cert`

5. **Caddy Configuration**
   - Sources from `secretai_configure_caddy.sh` and `secretai_caddy_mappings.sh`
   - Sets up Caddy with SSL certificates
   - Configures URL mappings and routing

6. **Firewall Configuration**
   - Sources from `secretai_configure_firewall.sh`
   - Configures UFW (Uncomplicated Firewall)
   - Opens required ports for service operation

## Configuration Paths

The script uses several predefined paths for installation and configuration:

- Attestation Service: `/opt/secretai-attest-rest`
- SSL Certificates: `/etc/caddy/cert`
- Caddy Configuration: `/etc/caddy/Caddyfile`

## Dependencies

The script sources multiple dependency files that must be present in the same directory:

- `secretai_install_dependencies.sh`: System package installation
- `secretai_build_install_attest.sh`: Attestation service installation
- `secretai_build_install_caddy.sh`: Caddy server installation
- `secretai_generate_cert.sh`: SSL certificate management
- `secretai_configure_firewall.sh`: Firewall configuration
- `secretai_configure_caddy.sh`: Caddy server configuration
- `secretai_caddy_mappings.sh`: URL routing definitions

## Error Handling

The script implements comprehensive error checking:
- Each major operation is verified for successful completion
- Script exits with appropriate error messages if any step fails
- Prevents partial installations that could lead to system inconsistency

## Security Considerations

The script handles several security-critical aspects:
- SSL certificate generation and configuration
- Firewall rules setup
- Secure service installation paths
- Proper permission settings

## System Requirements

The script expects:
- Ubuntu-based system with UFW firewall
- Root/sudo access for installation
- All dependency scripts present in the same directory
- Sufficient disk space in `/opt` and `/etc` directories