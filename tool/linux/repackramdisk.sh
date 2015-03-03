#!/bin/bash

cd out/boot
# check out path
if [ -e "boot.img-ramdisk.gz" ]
then
	rm -R boot.img-ramdisk.gz
fi

# pack ramdisk image
cd ../ramdisk
find . | cpio -H newc -o | gzip -9 -n > ../boot/boot.img-ramdisk.gz
