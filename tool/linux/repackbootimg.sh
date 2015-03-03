#!/bin/bash

cd out

if [ -e "boot.img" ]
then
	rm boot.img
fi

# androidboot.selinux=permissive
mkbootimg\
 --kernel boot/boot.img-zImage\
 --ramdisk boot/boot.img-ramdisk.gz\
 --cmdline "androidboot.console=ttyHSL0 androidboot.hardware=qcom user_debug=31 msm_rtb.filter=0x3F ehci-hcd.park=3 androidboot.bootdevice=7824900.sdhci"\
 --base 0x80000000\
 --pagesize 2048\
 --dt boot/boot.img-dt\
 --ramdisk_offset 0x01000000\
 --tags_offset 0x00000100\
 -o boot.img		
