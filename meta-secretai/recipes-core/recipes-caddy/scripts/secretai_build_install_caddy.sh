#!/bin/bash

# Exit on any error
build_install_caddy() {
    set -e

    echo "Starting Caddy reverse proxy setup..."

    # Check if Go is installed
    if ! command -v go &> /dev/null; then
        echo "Error: Go is not installed. Please install Go first."
        return 1
    fi

    # Install xcaddy
    echo "Installing xcaddy..."
    go install github.com/caddyserver/xcaddy/cmd/xcaddy@latest

    # Create working directory
    WORK_DIR="caddy-setup"
    mkdir -p $WORK_DIR
    cd $WORK_DIR

    # Build custom Caddy with the Claive module
    echo "Building custom Caddy with Claive module..."
    if [ ! -d "claive-reverse-proxy-module" ]; then
        echo "Error: claive-reverse-proxy-module directory not found."
        echo "Please ensure the module source code is available in ./claive-reverse-proxy-module"
        return 1
    fi

    xcaddy build --with claive-reverse-proxy-module=./claive-reverse-proxy-module

    # Verify the build
    echo "Verifying Caddy build..."
    ./caddy list-modules | grep -q "http.handlers.claive_reverse_proxy" || {
        echo "Error: Module verification failed"
        return 1
    }

    # Create caddy user and group
    echo "Creating caddy user and group..."
    sudo useradd -r -s /usr/sbin/nologin caddy || true

    # Set up directory structure
    echo "Setting up directory structure..."
    sudo mkdir -p /etc/caddy/cert
    # sudo chown -R caddy:caddy /etc/caddy

    # Install Caddy binary and configuration
    echo "Installing Caddy..."
    sudo cp ./caddy /usr/local/bin/
    sudo cp ./Caddyfile /etc/caddy/

    # Set proper permissions
    echo "Setting permissions..."
    sudo chown -R caddy:caddy /etc/caddy
    # sudo chmod 600 /etc/caddy/*.pem
    sudo chmod 755 /usr/local/bin/caddy

    cd ..
}
