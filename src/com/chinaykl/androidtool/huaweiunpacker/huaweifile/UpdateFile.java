package com.chinaykl.androidtool.huaweiunpacker.huaweifile;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.chinaykl.library.util.variable.Stream;

public class UpdateFile {
	// section magic
	private final byte[] SECTIONMAGIC = { (byte) 0x55, (byte) 0xAA, (byte) 0x5A, (byte) 0xA5 };
	// the path of this file
	private String mInPath;
	// the list of sections in file
	private ArrayList<UpdateSection> mSections = new ArrayList<UpdateSection>();

	public UpdateFile(String path) throws IOException {
		mInPath = path;

		FileInputStream fis = new FileInputStream(mInPath);
		BufferedInputStream bif = new BufferedInputStream(fis);
		seachSection(bif);

		bif.close();
		fis.close();
	}

	public String getPath() {
		return mInPath;
	}

	public String getImageNameByIndex(int index) {
		String val = "";
		if (index < mSections.size()) {
			val = mSections.get(index).getName();
		}
		return val;
	}

	// check is a section start or not
	private boolean isSectionStart(byte[] data) {
		return Arrays.equals(SECTIONMAGIC, data);
	}

	private final int READLIMIT = 4096;

	// find all the sections and keep them in list
	private int seachSection(BufferedInputStream is) throws IOException {
		int len = 0;
		int off = 0;
		byte[] data = new byte[4];

		do {
			if (len == 4) {
				if (isSectionStart(data)) {
					// this may be a section
					byte[] headData = new byte[UpdateImageHead.getNormalHeadSize()];
					is.reset();
					is.read(headData);
					// create UpdateSection
					UpdateSection section = new UpdateSection(headData, off);
					// check new section
					if (section.isAvailable()) {
						// this is a section
						mSections.add(section);
						len = section.getLen();
						// move back to the start of section
						is.reset();
						// skip data
						// it has to skip data step by step or will fail
						Stream.skip(is, len, READLIMIT);
					} else {
						// this is not a section
						// move back to the start of section
						is.reset();
						// continue other bytes
						is.skip(len);
					}
				}
				off += len;
			}
			is.mark(READLIMIT);
		} while ((len = is.read(data)) != -1);

		return mSections.size();
	}

	// print all section information
	public String getInfo() {
		String val = "";
		for (int i = 0; i < mSections.size(); i++) {
			UpdateSection us = mSections.get(i);
			val += "Section " + i + "\n";
			val += us.getInfo();
			val += "\n";
		}
		return val;
	}

	private void exportFromSection(UpdateSection section, int exOff, int buffer, String outPath) throws IOException {
		FileInputStream fis = new FileInputStream(mInPath);
		FileOutputStream fos = new FileOutputStream(outPath);

		section.exportData(fis, exOff, fos, buffer);

		fos.close();
		fis.close();
	}

	// export select<index> image
	public boolean exportImage(int index, int exOff, int buffer, String outPath) throws IOException {
		boolean val = false;
		if (index < mSections.size()) {
			UpdateSection us = mSections.get(index);
			exportFromSection(us, exOff, buffer, outPath);
			val = true;
		}
		return val;
	}

	// export select <name> image
	public boolean exportImage(String name, int exOff, int buffer, String outPath) throws IOException {
		boolean val = false;
		UpdateSection us = null;
		for (int i = 0; i < mSections.size(); i++) {
			us = mSections.get(i);
			if (us.getName().equals(name)) {
				exportFromSection(us, exOff, buffer, outPath);
				val = true;
				break;
			}
		}
		return val;
	}
}
