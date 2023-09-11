# Base this image on core-image-minimal
include recipes-graphics/images/core-image-weston.bb

# Include modules in rootfs
IMAGE_INSTALL:append  = " \
			trace-cmd \
			"

PACKAGE_EXCLUDE_COMPLIMENTARY = "openssh"
IMAGE_FEATURES += ""
