#!/bin/bash

cd out

# rename file
if [ -e "temp" ]
then
	rm -R temp
	mkdir temp
else
	mkdir temp
fi
cp ramdisk.img temp/ramdisk.img.gz
cd temp
gunzip ramdisk.img.gz

# unpack file
cd ..
if [ -e "ramdisk" ]
then
	rm -R ramdisk
	mkdir ramdisk
else
	mkdir ramdisk
fi
cd ramdisk
cpio -i -F ../temp/ramdisk.img

# clean temp file
cd ..
rm -R temp
