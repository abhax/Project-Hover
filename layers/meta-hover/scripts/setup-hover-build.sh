#!/bin/bash

# Setup script to fix common build issues and prepare environment
# Run this after sourcing setup-environment.sh

# Get directory where this script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
META_HOVER_DIR="$(dirname "$SCRIPT_DIR")"
BUILD_DIR="$PWD"

echo "Setting up hover build environment..."

# Check if we're in the build directory
if [[ ! -d "$BUILD_DIR/conf" ]]; then
    echo "Error: This script must be run from the build directory after sourcing setup-environment.sh"
    echo "Please run: cd <project-hover-dir> && source ./setup-environment.sh && cd build && ../layers/meta-hover/scripts/setup-hover-build.sh"
    exit 1
fi

# Copy or append configuration files
echo "Applying build configurations..."

# Append to local.conf
if ! grep -q "# hover-craft-build configuration" "$BUILD_DIR/conf/local.conf"; then
    echo -e "\n# hover-craft-build configuration - added by setup script" >> "$BUILD_DIR/conf/local.conf"
    cat "$META_HOVER_DIR/conf/local.conf.append" >> "$BUILD_DIR/conf/local.conf"
    echo "Added configuration to local.conf"
fi

# Clean problematic recipes to ensure clean rebuild
echo "Cleaning problematic recipes..."
bitbake -c cleansstate nativesdk-perl || true

# Apply specific workarounds
mkdir -p "$BUILD_DIR/tmp-glibc/work-shared/"
touch "$BUILD_DIR/tmp-glibc/work-shared/perl-native-fixup"

echo "Setup complete! You can now build hover-craft-basic with:"
echo "bitbake hover-craft-basic" 