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

# Add custom configuration to enable Bluetooth at boot with SysVinit
ROOTFS_POSTPROCESS_COMMAND += "enable_bluetooth_sysvinit; "

enable_bluetooth_sysvinit() {
    # Create init script for Bluetooth
    install -d ${IMAGE_ROOTFS}${sysconfdir}/init.d
    cat > ${IMAGE_ROOTFS}${sysconfdir}/init.d/bluetooth << EOF
#!/bin/sh
### BEGIN INIT INFO
# Provides:          bluetooth
# Required-Start:    $local_fs $syslog $time
# Required-Stop:     $local_fs $syslog $time
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Bluetooth service
### END INIT INFO

DESC="Bluetooth service"
NAME=bluetoothd
DAEMON=/usr/libexec/bluetooth/bluetoothd
PIDFILE=/var/run/\$NAME.pid
SCRIPTNAME=/etc/init.d/bluetooth

case "\$1" in
  start)
    echo "Starting \$DESC"
    start-stop-daemon --start --quiet --exec \$DAEMON -- --noplugin=sap
    echo "OK"
    ;;
  stop)
    echo "Stopping \$DESC"
    start-stop-daemon --stop --quiet --exec \$DAEMON
    echo "OK"
    ;;
  restart|force-reload)
    \$0 stop
    sleep 1
    \$0 start
    ;;
  status)
    if [ -f \$PIDFILE ]; then
      echo "\$DESC is running"
      exit 0
    else
      echo "\$DESC is not running"
      exit 1
    fi
    ;;
  *)
    echo "Usage: \$SCRIPTNAME {start|stop|restart|force-reload|status}" >&2
    exit 1
    ;;
esac

exit 0
EOF

    # Make the script executable
    chmod 755 ${IMAGE_ROOTFS}${sysconfdir}/init.d/bluetooth
    
    # Create symlinks to enable on boot
    install -d ${IMAGE_ROOTFS}${sysconfdir}/rc5.d
    ln -sf ../init.d/bluetooth ${IMAGE_ROOTFS}${sysconfdir}/rc5.d/S90bluetooth
    ln -sf ../init.d/bluetooth ${IMAGE_ROOTFS}${sysconfdir}/rc0.d/K10bluetooth
}

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
