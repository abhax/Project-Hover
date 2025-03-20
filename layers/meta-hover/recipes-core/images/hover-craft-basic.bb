# Base this image on core-image-minimal
include recipes-core/images/core-image-minimal.bb

# Ensure Bluetooth is in DISTRO_FEATURES
DISTRO_FEATURES:append = " bluetooth"

# Explicitly use SysVinit instead of systemd
DISTRO_FEATURES:remove = "systemd"
DISTRO_FEATURES:append = " sysvinit"
VIRTUAL-RUNTIME_init_manager = "sysvinit"
VIRTUAL-RUNTIME_initscripts = "initscripts"

# Include modules in rootfs
IMAGE_INSTALL:append  = " \
            trace-cmd perf \
            v4l-utils \
            gdbserver freeglut \
            bluez5 \
            bluez5-noinst-tools \
            bluez5-obex \
            bluez5-testtools \
            linux-firmware-bcm43430 \
            kernel-modules \
            "

PACKAGE_EXCLUDE_COMPLIMENTARY = "openssh"
IMAGE_FEATURES += ""

# Define a function to copy SD card images to a dedicated folder
python do_copy_sdimages() {
    import os
    import shutil
    import glob
    
    deploy_dir = d.getVar('DEPLOY_DIR_IMAGE')
    sd_image_dir = os.path.join(deploy_dir, "../sd-image")
    
    # Create sd-image directory if it doesn't exist
    if not os.path.exists(sd_image_dir):
        os.makedirs(sd_image_dir)
    
    # Find all .sdimg files
    sdimg_files = glob.glob(os.path.join(deploy_dir, "*.rpi-sdimg"))
    
    # Copy each file that doesn't contain "rootfs" in its name
    for file_path in sdimg_files:
        file_name = os.path.basename(file_path)
        if "rootfs" not in file_name and os.path.isfile(file_path):
            dest_path = os.path.join(sd_image_dir, file_name)
            shutil.copy2(file_path, dest_path)
            bb.note("Copied %s to sd-image folder" % file_name)
}

# Add our custom task to the build process after all images are created
addtask copy_sdimages after do_image_complete before do_build
