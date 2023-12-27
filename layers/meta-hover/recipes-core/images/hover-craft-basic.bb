# Base this image on core-image-minimal
include recipes-graphics/images/core-image-weston.bb

# Include modules in rootfs
IMAGE_INSTALL:append  = " \
            trace-cmd perf\
            v4l-utils libcamera\
            "

PACKAGE_EXCLUDE_COMPLIMENTARY = "openssh"
IMAGE_FEATURES += ""

do_image_complete:append(){

#if -d ${DEPLOYDIR}/SDImage;
#then
#    echo "Exists !"
#else
#    mkdir ${DEPLOYDIR}/SDImage
#fi
#for i in ${DEPLOYDIR}/*
#do
#    if echo $i | grep ".rpi-sdimg" > /dev/null
#    then
#        if echo $i | grep "rootfs" > /dev/null
#        then
#            echo "Ignore!"
#        else
#            cp ${DEPLOYDIR}/$i ${DEPLOYDIR}/SDImage/ 
#        fi
#    fi
#done

}
