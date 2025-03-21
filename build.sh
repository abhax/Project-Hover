#!/bin/bash

# Project build script for hover-craft-basic
# This script handles environment setup and builds the hover-craft image

set -e

# Directory where this script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cd "$SCRIPT_DIR"

# Parse command line arguments
CLEAN_BUILD=0
BUILD_TARGET="hover-craft-basic"
RUN_SETUP=1

print_usage() {
    echo "Usage: $0 [options]"
    echo "Options:"
    echo "  -c, --clean     Perform a clean build"
    echo "  -t, --target    Specify build target (default: hover-craft-basic)"
    echo "  -h, --help      Display this help message"
    exit 1
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    key="$1"
    case $key in
        -c|--clean)
            CLEAN_BUILD=1
            shift
            ;;
        -t|--target)
            BUILD_TARGET="$2"
            shift 2
            ;;
        -h|--help)
            print_usage
            ;;
        *)
            echo "Unknown option: $1"
            print_usage
            ;;
    esac
done

echo "========================================="
echo "Building $BUILD_TARGET"
echo "========================================="

# Source the environment setup script
if [ -f "./init-build-env.sh" ]; then
    echo "Setting up build environment..."
    source ./init-build-env.sh
elif [ -f "./setup-environment.sh" ]; then
    echo "Using standard setup script..."
    mkdir -p build
    source ./setup-environment.sh build
else
    echo "Error: setup scripts not found!"
    exit 1
fi

# Clean if requested
if [ $CLEAN_BUILD -eq 1 ]; then
    echo "Cleaning previous build artifacts..."
    bitbake -c cleansstate $BUILD_TARGET
    # Also clean problematic recipes
    bitbake -c cleansstate nativesdk-perl || true
fi

# Build the target
echo "Building $BUILD_TARGET..."
bitbake $BUILD_TARGET

echo "========================================="
echo "Build completed successfully!"
echo "=========================================" 