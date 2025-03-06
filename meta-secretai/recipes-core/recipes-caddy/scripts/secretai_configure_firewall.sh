#!/bin/bash

# generate a function that will check if ufw is installed and if not, install it
check_and_install_ufw() {
    if ! command -v ufw &> /dev/null; then
        echo "UFW is not installed. Installing..."
        apt-get update
        apt-get install -y ufw
    else
        echo "UFW is already installed."
    fi
}

# generate a function that will check if ufw is enabled and if not, enable it
check_and_enable_ufw() {
    if ! ufw status | grep -q "Status: active"; then
        echo "UFW is not enabled. Enabling..."
        ufw enable
    else
        echo "UFW is already enabled."
    fi
}


# Function: check_and_configure_ufw
# Description: Configures the Uncomplicated Firewall (UFW) with specified TCP ports
# 
# Parameters:
#   $@ - Array of TCP port numbers to allow through the firewall
#
# This function performs the following:
# 1. Checks if UFW is enabled and enables it if not active
# 2. Iterates through the provided port numbers and:
#    - Allows TCP traffic on each specified port
#    - Common ports to allow include:
#      * 22/tcp for SSH access
#      * 80/tcp for HTTP traffic
#      * 443/tcp for HTTPS traffic
# 3. Reloads the firewall to apply all changes
#
# Returns: None
# Side effects: Modifies system firewall rules by adding allow rules for specified ports
#
check_and_configure_ufw() {
    local ports=("$@")
    echo "Configuring UFW firewall..."
    
    # Enable UFW if not already enabled
    ufw status | grep -q "Status: active" || ufw enable
    
    # Allow each port in the array
    for port in "${ports[@]}"; do
        echo "Allowing port $port/tcp..."
        ufw allow "$port/tcp"
    done
    
    # Reload UFW
    ufw reload
    
    echo "Firewall configuration completed"
}

