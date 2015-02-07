package com.chinaykl.androidtool.huaweiunpacker.huaweifile;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.chinaykl.library.util.variable.array;

public class UpdateFile {
	private FileInputStream mInStream;
	private ArrayList<UpdateSection> mSections = new ArrayList<UpdateSection>();

	public UpdateFile(String pathname) throws IOException {
		mInStream = new FileInputStream(pathname);
		BufferedInputStream bif = new BufferedInputStream(mInStream);
		seachSection(bif);
		for(int i =0;i<mSections.size();i++){
			System.out.println(mSections.get(i).getImageHead().getInfo());
		}
	}

	private int seachSection(BufferedInputStream is) throws IOException {
		int len = 0;
		int off = 0;
		byte[] data = new byte[4];
		while ((len = is.read(data)) != -1) {	
			if (len == 4) {
				if (UpdateImageHead.isHeadStart(data)) {
					// found a section
					is.mark(UpdateImageHead.getMaxHeadSize());
					
					// check is this a real section
					byte[] hardwarePre = new byte[2];
					is.skip(8);
					is.read(hardwarePre);
					if (UpdateImageHead.isRealHead(hardwarePre)) {
						// read head size
						byte[] headSizeData = new byte[4];
						is.reset();
						is.read(headSizeData);
						int headSize = array.bytesToInt(headSizeData);
						
						// create UpdateSection
						byte[] headData = new byte[headSize];
						is.reset();
						len = is.read(headData);
						if (len == headSize) {
							UpdateSection section = new UpdateSection(headData, off);
							mSections.add(section);
							off+=len;
						}
					}
				}
				off += 4;
			}
		}
		return mSections.size();
	}
}
