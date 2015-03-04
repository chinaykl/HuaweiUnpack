#!/bin/bash

function help
{
	echo "help:"
	echo "	repackbootimg.sh <'boot'||'recovery'>"
}

function pack
{
	echo "start to pack $target.img"
	mkbootimg\
	 --kernel $KERNEL\
	 --ramdisk $RAMDISK\
	 --cmdline "$CMDLINE"\
	 --base $BASE\
	 --pagesize $PAGESIZE\
	 --dt $DT\
	 --ramdisk_offset $RAMDISKOFF\
	 --tags_offset $TAGSOFF\
	 -o $OUTPATH
	if [ $? -ne 0 ]
	then
		echo "pack $target.img fail"
	else
		echo "pack $target.img success"
	fi
}

function check
{
	if [ $1 -ne 1 ]
	then
		echo "Wrong Argument!"
		help
		exit 1
	fi

	if [[ !($2 = "boot" || $2 = "recovery") ]]
	then
		echo "Wrong Argument $2"
		help
		exit 1
	fi
	
	input=$2
}

check $# $1
target=$input

cd out

if [ -e "boot.img" ]
then
	rm boot.img
fi

INPATH="$target/$target.img"
OUTPATH="$target.img"
KERNEL="$INPATH-zImage"
RAMDISK="$INPATH-ramdisk.gz"
# androidboot.selinux=permissive
CMDLINE="androidboot.console=ttyHSL0 androidboot.hardware=qcom user_debug=31 msm_rtb.filter=0x3F ehci-hcd.park=3 androidboot.bootdevice=7824900.sdhci"
BASE="0x80000000"
PAGESIZE="2048"
DT="$INPATH-dt"
RAMDISKOFF="0x01000000"
TAGSOFF="0x00000100"

pack
