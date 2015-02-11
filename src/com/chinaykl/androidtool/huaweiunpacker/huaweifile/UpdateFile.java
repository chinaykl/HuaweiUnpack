package com.chinaykl.androidtool.huaweiunpacker.huaweifile;

import java.io.BufferedInputStream;
import java.io.File;
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
	// out folder
	private final String OUTFOLDER = "out/";
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

	// generate output path according to input path
	private String getOutPath(String imageName) throws IOException {
		int lastPS = mInPath.lastIndexOf('/');
		String val = mInPath.substring(0, lastPS + 1);

		// check out folder is exist or not
		val += OUTFOLDER;
		File folder = new File(val);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		// check out file is exist or not
		val += imageName;
		File file = new File(val);
		if (file.exists()) {
			file.delete();
		}

		return val;
	}

	private void exportFromSection(UpdateSection section, int exOff, int buffer) throws IOException {
		FileInputStream fis = new FileInputStream(mInPath);
		FileOutputStream fos = new FileOutputStream(getOutPath(section.getName()));

		section.exportData(fis, exOff, fos, buffer);

		fos.close();
		fis.close();
	}

	// export select<index> image
	public boolean exportImage(int index, int exOff, int buffer) throws IOException {
		boolean val = false;
		if (index < mSections.size()) {
			UpdateSection us = mSections.get(index);
			exportFromSection(us, exOff, buffer);
			val = true;
		}
		return val;
	}

	// export select <name> image
	public boolean exportImage(String name, int exOff, int buffer) throws IOException {
		boolean val = false;
		UpdateSection us = null;
		for (int i = 0; i < mSections.size(); i++) {
			us = mSections.get(i);
			if (us.getName().equalsIgnoreCase(name)) {
				exportFromSection(us, exOff, buffer);
				val = true;
				break;
			}
		}
		return val;
	}
}
