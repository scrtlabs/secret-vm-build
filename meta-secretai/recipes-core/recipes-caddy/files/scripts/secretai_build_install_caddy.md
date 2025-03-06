# SecretAI Caddy Reverse Proxy Build and Configuration

This document describes the build, installation, and configuration process for the Caddy reverse proxy server, which is a critical component of the SecretAI TD Confidential VM system.

## Overview

The Caddy reverse proxy setup consists of two main components:
1. Build and installation process (`build_install_caddy`)
2. Configuration generation (`configure_caddy`)

The system provides secure reverse proxy capabilities with TLS termination, client certificate authentication, and CORS handling.

## Build and Installation

### Function: `build_install_caddy`

Builds and installs a custom Caddy server with the Claive reverse proxy module.

#### Prerequisites
- Go programming language
- System with sudo privileges
- Claive reverse proxy module source code

#### Process Steps
1. **Environment Validation**
   - Checks for Go installation
   - Verifies required module source code presence

2. **xcaddy Installation**
   - Installs xcaddy tool for custom Caddy builds
   - Uses Go package manager

3. **Custom Build**
   - Creates working directory
   - Builds Caddy with Claive module
   - Verifies module installation

4. **System Setup**
   - Creates dedicated `caddy` user and group
   - Establishes directory structure:
     ```
     /etc/caddy/
     └── cert/    # Certificate directory
     ```
   - Sets appropriate permissions

5. **Binary Installation**
   - Installs Caddy binary to `/usr/local/bin/`
   - Sets up initial configuration

## Configuration Generation

### Function: `configure_caddy`

Generates Caddy server configuration with reverse proxy rules, TLS settings, and CORS policies.

#### Parameters
- `private_key_location`: Path to TLS private key file
- `certificate_location`: Path to TLS certificate file
- `caddyfile_location`: Output path for generated Caddyfile

#### Configuration Components

1. **Global Settings**
```caddyfile
{
    debug
    order claive_reverse_proxy first
}
```

2. **CORS Snippet**
```caddyfile
(cors) {
    header {
        Access-Control-Allow-Origin "{header.origin}"
        Access-Control-Allow-Headers "Content-Type, Authorization, Referrer-Policy, priority, sec-ch-ua, sec-ch-ua-mobile, sec-ch-ua-platform"
        Vary Origin
        Access-Control-Allow-Credentials "true"
        defer
    }
}
```

3. **Service Mappings**
The configuration supports multiple service mappings with the following features:
- TLS termination with client certificate authentication
- CORS preflight request handling
- Header modification for proxied requests

#### Default Service Mappings

| Service | External Port | Internal Port |
|---------|--------------|---------------|
| Secret Agent API | 25343 | 11343 |
| Secret LLM | 25434 | 11434 |
| Secret Attest | 26343 | 29343 |

#### Firewall Configuration
Default open ports:
- 22 (SSH)
- 25343 (Secret Agent API)
- 25434 (Secret LLM)
- 26343 (Secret Attest)

## Configuration Template

Each service mapping follows this template structure:

```caddyfile
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
            Access-Control-Allow-Origin "{header.origin}"
            Access-Control-Allow-Methods "GET, POST, PUT, PATCH, DELETE, OPTIONS"
            Access-Control-Allow-Headers "Content-Type, Authorization, Referrer-Policy, priority, sec-ch-ua, sec-ch-ua-mobile, sec-ch-ua-platform"
            Vary Origin
            Access-Control-Max-Age "3600"
        }
        respond "" 204
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
}
```

## Security Considerations

1. **TLS Configuration**
   - Requires client certificate authentication
   - Uses trusted CA certificate validation
   - Implements secure CORS policies

2. **Access Control**
   - Dedicated service user (`caddy`)
   - Restricted file permissions
   - Firewall port limitations

3. **Header Management**
   - Strips sensitive headers
   - Modifies host headers for backend services
   - Implements secure CORS policies

## Maintenance and Operations

### Service Management
```bash
# Start Caddy service
sudo systemctl start secretai-caddy

# Check service status
sudo systemctl status secretai-caddy

# View logs
sudo journalctl -u secretai-caddy
```

### Configuration Verification
```bash
# Test configuration
sudo caddy validate --config /etc/caddy/Caddyfile

# List installed modules
sudo caddy list-modules
```