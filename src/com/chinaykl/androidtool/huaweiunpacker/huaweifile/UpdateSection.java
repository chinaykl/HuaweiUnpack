package com.chinaykl.androidtool.huaweiunpacker.huaweifile;

public class UpdateSection {
	private int mOffset;
	private UpdateImageHead mHead;
	private UpdateImageData mData;

	public UpdateSection(byte[] ins, int off) {
		mOffset = off;
		mHead = new UpdateImageHead(ins);
		mData = new UpdateImageData();
	}
	
	public UpdateImageHead getImageHead(){
		return mHead;
	}
	
	public UpdateImageData getImageData(){
		return mData;
	}
}
