package com.chinaykl.androidtool.huaweiunpacker.huaweifile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.chinaykl.library.util.variable.Stream;

class UpdateImageData {
	// the offset of image data in section
	private int mOffset;
	// the len of image data
	private int mSize;

	public UpdateImageData(int offset, int size) {
		mOffset = offset;
		mSize = size;
	}

	public int getOffset() {
		return mOffset;
	}

	public int getSize() {
		return mSize;
	}

	// skip num every time
	private final int SKIPLIMIT = 4096;

	// Out this image
	// exOff:some info may in front of real data
	// buffer:the buffer of read and write
	public void export(FileInputStream fileIS, int exOff, FileOutputStream fileOS, int buffer) throws IOException {
		// skip useless data
		Stream.skip(fileIS, mOffset, SKIPLIMIT);
		Stream.skip(fileIS, exOff, SKIPLIMIT);

		// start to transfer
		Stream.copy(fileIS, fileOS, mSize, buffer);
	}
}
