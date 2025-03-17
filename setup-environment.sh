#!/bin/bash
# Ensure this script is sourced, not executed
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    echo "Error: This script must be sourced, not executed."
    echo "Please run: source $0 or . $0"
    exit 1
fi

# Source the Yocto environment setup script
# The '.' command is the same as 'source' but more portable
. ./layers/meta-poky-honister/oe-init-build-env build

# Add custom message with additional targets
cat << EOF

Additional Hover Project targets:
    hover-craft-basic

EOF

# Keep the shell open by not exiting
echo "Yocto build environment initialized in $(pwd)"
echo "Current user: $(whoami)"
