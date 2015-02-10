package com.chinaykl.androidtool.huaweiunpacker.huaweifile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.chinaykl.library.util.variable.Stream;

// a section comprises a imagehead and a imagedata
public class UpdateSection {
	// the offset of Section in flash file
	private int mOffset;
	private UpdateImageHead mHead;
	private UpdateImageData mData;

	public UpdateSection(byte[] ins, int off) {
		mOffset = off;
		mHead = new UpdateImageHead(ins);
		mData = new UpdateImageData(mHead.getHeadSize(), mHead.getDataSize());
	}

	public int getOffset() {
		return mOffset;
	}

	public UpdateImageHead getImageHead() {
		return mHead;
	}

	public void exportImageFromSection(FileInputStream fileIS, int exOff, FileOutputStream fileOS, int buffer) throws IOException {
		// skip num every time
		int skipNum = 4096;

		// skip useless data
		Stream.skip(fileIS, mOffset, skipNum);

		// start to transfer
		mData.exportImageFromData(fileIS, exOff, fileOS, buffer);
	}
}
