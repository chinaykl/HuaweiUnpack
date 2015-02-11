package com.chinaykl.androidtool.huaweiunpacker.huaweifile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.chinaykl.library.util.variable.Stream;

// a section comprises a imagehead and a imagedata
class UpdateSection {
	// the offset of Section in flash file
	private int mOffset;
	private UpdateImageHead mHead;
	private UpdateImageData mData;

	public UpdateSection(byte[] ins, int off) {
		mOffset = off;
		mHead = new UpdateImageHead(ins);
		mData = new UpdateImageData(mHead.getSize(), mHead.getDataSize());
	}

	public boolean isAvailable() {
		boolean val = false;
		if (mHead.isRealMagic() && mHead.isRealHead()) {
			val = true;
		}
		return val;
	}

	public int getOffset() {
		return mOffset;
	}

	public int getLen() {
		int len = mHead.getSize() + mData.getSize();
		// align at 4 byte
		if ((len % 4) != 0) {
			len = (len / 4 + 1) * 4;
		}
		return len;
	}

	public String getName() {
		return mHead.getInfo();
	}

	// get this the infomation of section
	public String getInfo() {
		String val = "";
		val += "NAME        :" + mHead.getInfo() + "\n";
		val += "OFFSET      :" + mOffset + "\n";
		val += "HEADSIZE    :" + mHead.getSize() + "\n";
		val += "DATASIZE    :" + mData.getSize() + "\n";
		val += "END         :" + (mOffset + mHead.getSize() + mData.getSize()) + "\n";
		return val;
	}

	// skip num every time
	private final int SKIPLIMIT = 4096;

	// export data from section
	public void exportData(FileInputStream fileIS, int exOff, FileOutputStream fileOS, int buffer) throws IOException {
		// skip useless data
		Stream.skip(fileIS, mOffset, SKIPLIMIT);

		// start to transfer
		mData.export(fileIS, exOff, fileOS, buffer);
	}
}
