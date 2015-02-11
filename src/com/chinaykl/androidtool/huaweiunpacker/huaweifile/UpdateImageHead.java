package com.chinaykl.androidtool.huaweiunpacker.huaweifile;

import java.util.Arrays;

import com.chinaykl.library.util.variable.Array;

public class UpdateImageHead {
	// name start end len
	// magic 0 3 4
	// headsize 4 7 4 (include magic)
	// version 8 11 4
	// hardware 12 19 8
	// checksum 20 23 4
	// datasize 24 27 4
	// data 28 43 16
	// time 44 59 16
	// info 60 91 32
	// ......
	private final int MAGICS = 0;
	private final int MAGICL = 4;
	private final int MAGICE = MAGICS + MAGICL;
	private byte mMagic[];
	private final byte MAGIC[] = { (byte) 0x55, (byte) 0xAA, (byte) 0x5A, (byte) 0xA5 };

	private final int HEADSIZES = 4;
	private final int HEADSIZEL = 4;
	private final int HEADSIZEE = HEADSIZES + HEADSIZEL;
	private byte mHeadSize[];

	private final int VERSIONS = HEADSIZEE;
	private final int VERSIONL = 4;
	private final int VERSIONE = VERSIONS + VERSIONL;
	private byte mVersion[];

	private final int HARDWARES = VERSIONE;
	private final int HARDWAREL = 8;
	private final int HARDWAREE = HARDWARES + HARDWAREL;
	private byte mHardware[];
	private final byte HARDWAREPRE[] = { 'H', 'W' };

	private final int CHECKSUMS = HARDWAREE;
	private final int CHECKSUML = 4;
	private final int CHECKSUME = CHECKSUMS + CHECKSUML;
	private byte mCheckSum[];

	private final int DATASIZES = CHECKSUME;
	private final int DATASIZEL = 4;
	private final int DATASIZEE = DATASIZES + DATASIZEL;
	private byte mDataSize[];

	private final int DATES = DATASIZEE;
	private final int DATEL = 16;
	private final int DATEE = DATES + DATEL;
	private byte mDate[];

	private final int TIMES = DATEE;
	private final int TIMEL = 16;
	private final int TIMEE = TIMES + TIMEL;
	private byte mTime[];

	private final int INFOS = TIMEE;
	private final int INFOL = 32;
	private final int INFOE = INFOS + INFOL;
	private byte mInfo[];

	private static final int NORMALHEADSIZE = 92;

	public UpdateImageHead(byte[] head) {
		// get useful data from part of all head
		mMagic = Arrays.copyOfRange(head, MAGICS, MAGICE);
		mHeadSize = Arrays.copyOfRange(head, HEADSIZES, HEADSIZEE);
		mVersion = Arrays.copyOfRange(head, VERSIONS, VERSIONE);
		mHardware = Arrays.copyOfRange(head, HARDWARES, HARDWAREE);
		mCheckSum = Arrays.copyOfRange(head, CHECKSUMS, CHECKSUME);
		mDataSize = Arrays.copyOfRange(head, DATASIZES, DATASIZEE);
		mDate = Arrays.copyOfRange(head, DATES, DATEE);
		mTime = Arrays.copyOfRange(head, TIMES, TIMEE);
		mInfo = Arrays.copyOfRange(head, INFOS, INFOE);
	}

	// Check the image head flag is right or not (MAGIC)
	public boolean isRealMagic() {
		return Arrays.equals(MAGIC, mMagic);
	}

	// Check the image head is real or not
	// sometimes some data contain head flag
	// but it can not contain hardware info at the same time
	public boolean isRealHead() {
		String str = new String(mHardware).trim();
		byte hardwarepre[] = str.substring(0, 2).getBytes();
		return Arrays.equals(HARDWAREPRE, hardwarepre);
	}

	public static int getNormalHeadSize() {
		return NORMALHEADSIZE;
	}

	public int getOffset() {
		return 0;
	}

	public int getSize() {
		return Array.bytesToInt(mHeadSize);
	}

	public String getVersion() {
		String result = new String(mVersion);
		return result.trim();
	}

	public String getHardware() {
		String result = new String(mHardware);
		return result.trim();
	}

	public String getCheckSum() {
		String result = new String(Array.bytesToHex(mCheckSum));
		return result;
	}

	public int getDataSize() {
		return Array.bytesToInt(mDataSize);
	}

	public String getDate() {
		String result = new String(mDate);
		return result.trim();
	}

	public String getTime() {
		String result = new String(mTime);
		return result.trim();
	}

	public String getInfo() {
		String result = new String(mInfo);
		return result.trim();
	}
}
