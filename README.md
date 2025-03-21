# Hover Craft Project

This is a Yocto-based build system for the Hover Craft embedded project, targeting Raspberry Pi 4.

## Prerequisites

- Linux development environment (Ubuntu 22.04 recommended)
- Git
- Yocto project dependencies (installed via `apt-get`)

## Quick Start

To build the hover-craft-basic image, run:

```bash
./build.sh
```

This script will:
1. Set up the build environment
2. Apply custom configurations and patches
3. Build the hover-craft-basic image

## Command Options

The build script supports various options:

```bash
./build.sh [options]
```

Options:
- `-c, --clean`: Perform a clean build
- `-t, --target <target>`: Specify build target (default: hover-craft-basic)
- `-h, --help`: Display help message

## Manual Build Process

If you prefer to run the build steps manually:

1. Source the environment setup script:
   ```bash
   source ./setup-environment.sh
   ```

2. Navigate to build directory:
   ```bash
   cd build
   ```

3. Run the hover setup script:
   ```bash
   ../layers/meta-hover/scripts/setup-hover-build.sh
   ```

4. Build the target:
   ```bash
   bitbake hover-craft-basic
   ```

## Troubleshooting

If you encounter build errors:

1. Check the error logs in `build/tmp/log/`
2. Run a clean build:
   ```bash
   ./build.sh --clean
   ```
3. For persistent errors with specific recipes, you may need to clean them individually:
   ```bash
   bitbake -c cleansstate <recipe-name>
   ```

## Project Structure

- `layers/meta-hover/`: Custom layer with project-specific recipes and patches
- `setup-environment.sh`: Environment setup script provided by Yocto
- `build.sh`: Main build script
- `layers/meta-hover/scripts/setup-hover-build.sh`: Custom setup script for this project 