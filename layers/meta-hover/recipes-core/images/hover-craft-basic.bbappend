# Append additional packages to the hover-craft-basic image
IMAGE_INSTALL:append = " \
    hover-app \
    bluez5 \
    bluez5-noinst-tools \
    bluez5-testtools \
    packagegroup-core-buildessential \
    wpa-supplicant \
    iw \
    linux-firmware-rpidistro-bcm43430 \
    wifi-init \
"

# Add bluetooth and Wi-Fi to DISTRO_FEATURES to ensure all needed components are available
DISTRO_FEATURES:append = " bluetooth sysvinit wifi"

# Add dependencies to ensure all necessary components are available
DEPENDS += "bluez5 hover-app wpa-supplicant wifi-init"

# Make sure the hover-app is included in the image
CORE_IMAGE_EXTRA_INSTALL += " hover-app"

# Fix RPATH-related packaging issues
INSANE_SKIP:hover-app += "rpaths already-stripped" 