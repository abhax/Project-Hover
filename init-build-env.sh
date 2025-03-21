#!/bin/bash

# Custom initialization script for Hover Project
# This ensures the meta-hover layer is properly set up

# Get the directory where this script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cd "$SCRIPT_DIR"

# Create the build directory
mkdir -p build

# Source the original environment setup
source ./setup-environment.sh build

# Add our custom layer if not already included
if ! grep -q "meta-hover" conf/bblayers.conf; then
    echo "Adding meta-hover layer to bblayers.conf"
    # Backup the original file
    cp conf/bblayers.conf conf/bblayers.conf.bak
    
    # Add our layer
    sed -i '/meta-raspberrypi/a \  '"${SCRIPT_DIR}"'\/layers\/meta-hover \\' conf/bblayers.conf
fi

# Make sure MACHINE is set to raspberrypi4-64
if ! grep -q "MACHINE.*raspberrypi4-64" conf/local.conf; then
    echo "Setting MACHINE to raspberrypi4-64 in local.conf"
    # Backup the original file
    cp conf/local.conf conf/local.conf.bak
    
    # Set MACHINE
    sed -i 's/^MACHINE.*/MACHINE = "raspberrypi4-64"/' conf/local.conf
fi

# Apply hover-specific configuration
if [ -f "${SCRIPT_DIR}/layers/meta-hover/scripts/setup-hover-build.sh" ]; then
    echo "Running hover setup script..."
    ${SCRIPT_DIR}/layers/meta-hover/scripts/setup-hover-build.sh
fi

echo "Hover build environment initialized. You can now run:"
echo "bitbake hover-craft-basic" 