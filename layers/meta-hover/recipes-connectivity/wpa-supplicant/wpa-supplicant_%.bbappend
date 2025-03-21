# Add Wi-Fi support for Project Hover hover-craft-basic

# Make sure the necessary firmware is included
RDEPENDS:${PN} += "linux-firmware-rpidistro-bcm43430"

# Add configuration files
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://wpa_supplicant.conf-sane"

do_install:append() {
    # Install a default wpa_supplicant configuration file
    install -d ${D}${sysconfdir}/wpa_supplicant
    install -m 0600 ${WORKDIR}/wpa_supplicant.conf-sane ${D}${sysconfdir}/wpa_supplicant/wpa_supplicant.conf
}

FILES:${PN} += "${sysconfdir}/wpa_supplicant/wpa_supplicant.conf" 