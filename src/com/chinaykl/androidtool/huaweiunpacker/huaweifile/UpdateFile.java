package com.chinaykl.androidtool.huaweiunpacker.huaweifile;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.chinaykl.library.util.variable.Stream;

public class UpdateFile {
	private String mInPath;
	private final String OUTFOLDER = "out/";
	private ArrayList<UpdateSection> mSections = new ArrayList<UpdateSection>();

	public UpdateFile(String path) throws IOException {
		mInPath = path;

		FileInputStream fis = new FileInputStream(mInPath);
		BufferedInputStream bif = new BufferedInputStream(fis);
		seachSection(bif);
		bif.close();
		fis.close();
	}

	private String getOutPath(String imageName) {
		int lastPS = mInPath.lastIndexOf('/');
		String val = mInPath.substring(0, lastPS + 1);

		val += OUTFOLDER;
		val += imageName;

		return val;
	}

	public ArrayList<UpdateSection> getSections() {
		return mSections;
	}

	public void exportImageFormFile(int index, int exOff, int buffer) throws IOException {
		UpdateSection us = mSections.get(index);

		FileInputStream fis = new FileInputStream(mInPath);
		FileOutputStream fos = new FileOutputStream(getOutPath(us.getImageHead().getInfo()));

		us.exportImageFromSection(fis, exOff, fos, buffer);

		fos.close();
		fis.close();
	}

	private int seachSection(BufferedInputStream is) throws IOException {
		int len = 0;
		int off = 0;
		byte[] data = new byte[4];

		do {
			if (len == 4) {
				if (UpdateImageHead.isHeadStart(data)) {
					// this may be a section

					// get hardware prefix
					byte[] hardwarePre = new byte[2];
					is.skip(8);
					is.read(hardwarePre);

					if (UpdateImageHead.isRealHead(hardwarePre)) {
						// this is a section
						// create UpdateSection
						byte[] headData = new byte[UpdateImageHead.getNormalHeadSize()];
						is.reset();
						is.read(headData);
						UpdateSection section = new UpdateSection(headData, off);
						mSections.add(section);
						len = section.getImageHead().getHeadSize() + section.getImageHead().getDataSize();

						// align at 4 byte
						if ((len % 4) != 0) {
							len = (len / 4 + 1) * 4;
						}

						// skip data
						// it has to skip data step by step or will fail
						is.reset();
						Stream.skip(is, len, UpdateImageHead.getMaxHeadSize());
					} else {
						// this is not a section
						// move back to the start of section
						is.reset();
						is.skip(len);
					}
				}
				off += len;
			}
			is.mark(UpdateImageHead.getMaxHeadSize());
		} while ((len = is.read(data)) != -1);

		return mSections.size();
	}
}
