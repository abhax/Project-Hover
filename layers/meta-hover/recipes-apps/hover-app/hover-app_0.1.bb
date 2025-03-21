SUMMARY = "HoverApp - Hovercraft Control Application"
DESCRIPTION = "Application for controlling the hovercraft platform"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=35b175e9fce62d6876e205057dcfd9d6"

# External source directory at the same level as Project-Hover
HOVER_APP_SRC_DIR = "${@os.path.abspath(os.path.join('${TOPDIR}', '..', '..', 'hoverApp'))}"

# Set up SRC_URI to include files from the recipe directory and the external source
SRC_URI = "file://LICENSE \
           file://${HOVER_APP_SRC_DIR}/ \
          "

# Set S to use the workdir for simplicity
S = "${WORKDIR}"

inherit cmake

# Add runtime dependencies
DEPENDS += "bluez5"
RDEPENDS:${PN} += "bluez5"

# Add build flags and disable RPATH
EXTRA_OECMAKE += " -DCMAKE_BUILD_TYPE=Release \
                   -DCMAKE_VERBOSE_MAKEFILE=ON \
                   -DCMAKE_SKIP_RPATH=TRUE \
                   -DCMAKE_SKIP_BUILD_RPATH=TRUE \
                   -DCMAKE_SKIP_INSTALL_RPATH=TRUE \
                 "

# Fix source directory by pointing to the correct location
OECMAKE_SOURCEPATH = "${S}/${HOVER_APP_SRC_DIR}"

# Ensure proper sysroot paths
TARGET_CC_ARCH += "${LDFLAGS}"

# Remove external source path prefix from compile command to avoid hardcoding paths
do_configure:prepend() {
    # Create a local CMake toolchain file to ensure proper cross-compilation settings
    cat > ${WORKDIR}/toolchain.cmake << EOF
# CMake toolchain file for cross-compilation
set(CMAKE_SYSTEM_NAME Linux)
set(CMAKE_SKIP_RPATH TRUE)
set(CMAKE_SKIP_BUILD_RPATH TRUE)
set(CMAKE_SKIP_INSTALL_RPATH TRUE)
EOF
    
    # Add the custom toolchain file to CMake arguments
    export EXTRA_OECMAKE="${EXTRA_OECMAKE} -DCMAKE_TOOLCHAIN_FILE=${WORKDIR}/toolchain.cmake"
}

# Install the binary
do_install() {
    install -d ${D}${bindir}
    
    # Check if binary exists in the expected location
    if [ -f ${B}/hoverApp ]; then
        install -m 0755 ${B}/hoverApp ${D}${bindir}
    elif [ -f ${B}/Test/hoverApp ]; then
        install -m 0755 ${B}/Test/hoverApp ${D}${bindir}
    else
        bbfatal "Cannot find hoverApp binary in build directory"
    fi
}

# Preserve debug symbols - let Yocto handle stripping
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

# Skip QA checks that we can't fix due to the external source code
INSANE_SKIP:${PN} += "rpaths already-stripped"

FILES:${PN} += "${bindir}/hoverApp" 