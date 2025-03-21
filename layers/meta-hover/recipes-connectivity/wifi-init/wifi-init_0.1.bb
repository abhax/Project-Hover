SUMMARY = "Wi-Fi Initialization for hover-craft-basic"
DESCRIPTION = "Start Wi-Fi services on boot for hover-craft-basic"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://wifi-init"

S = "${WORKDIR}"

INITSCRIPT_NAME = "wifi-init"
INITSCRIPT_PARAMS = "defaults 90 10"

inherit update-rc.d

do_install() {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/wifi-init ${D}${sysconfdir}/init.d/wifi-init
}

FILES:${PN} += "${sysconfdir}/init.d/wifi-init"

RDEPENDS:${PN} += "wpa-supplicant iw bash" 