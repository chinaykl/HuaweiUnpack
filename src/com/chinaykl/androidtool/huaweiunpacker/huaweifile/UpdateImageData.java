package com.chinaykl.androidtool.huaweiunpacker.huaweifile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.chinaykl.library.util.variable.Stream;

class UpdateImageData {
	// the offset of image data in section
	private int mStart;
	// the len of image data
	private int mLen;

	public UpdateImageData(int offset, int len) {
		mStart = offset;
		mLen = len;
	}

	// Out this image
	// exOff:some info may in front of real data
	// buffer:the buffer of read and write
	public void exportImageFromData(FileInputStream fileIS, int exOff, FileOutputStream fileOS, int buffer) throws IOException {
		// skip num every time
		int skipNum = 4096;

		// skip useless data
		Stream.skip(fileIS, mStart, skipNum);
		Stream.skip(fileIS, exOff, skipNum);

		// start to transfer
		Stream.copy(fileIS, fileOS, mLen, buffer);
	}
}
