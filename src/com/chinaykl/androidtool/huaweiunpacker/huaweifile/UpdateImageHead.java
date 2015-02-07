package com.chinaykl.androidtool.huaweiunpacker.huaweifile;

import java.util.Arrays;

import com.chinaykl.library.util.variable.array;

public class UpdateImageHead {
	// name start end len
	// magic 0 3 4
	// headsize 4 7 4
	// version 8 11 4
	// hardware 12 19 8
	// checksum 20 23 4
	// datasize 24 27 4
	// data 28 43 16
	// time 44 59 16
	// info 60 91 32
	// ......
	private static final byte MAGIC[] = { (byte) 0x55, (byte) 0xAA, (byte) 0x5A, (byte) 0xA5 };

	private final int HEADSIZES = 4;
	private final int HEADSIZEL = 4;
	private final int HEADSIZEE = HEADSIZES + HEADSIZEL - 1;
	private byte mHeadSize[];

	private final int VERSIONS = HEADSIZEE + 1;
	private final int VERSIONL = 4;
	private final int VERSIONE = VERSIONS + VERSIONL - 1;
	private byte mVersion[];

	private final int HARDWARES = VERSIONE + 1;
	private final int HARDWAREL = 8;
	private final int HARDWAREE = HARDWARES + HARDWAREL - 1;
	private byte mHardware[];

	private final int CHECKSUMS = HARDWAREE + 1;
	private final int CHECKSUML = 4;
	private final int CHECKSUME = CHECKSUMS + CHECKSUML - 1;
	private byte mCheckSum[];

	private final int DATASIZES = CHECKSUME + 1;
	private final int DATASIZEL = 4;
	private final int DATASIZEE = DATASIZES + DATASIZEL - 1;
	private byte mDataSize[];

	private final int DATES = DATASIZEE + 1;
	private final int DATEL = 16;
	private final int DATEE = DATES + DATEL - 1;
	private byte mDate[];

	private final int TIMES = DATEE + 1;
	private final int TIMEL = 16;
	private final int TIMEE = TIMES + TIMEL - 1;
	private byte mTime[];

	private final int INFOS = TIMEE + 1;
	private final int INFOL = 32;
	private final int INFOE = INFOS + INFOL - 1;
	private byte mInfo[];

	private static final byte HARDWAREPRE[] = { 'H', 'W' };
	private static final int MAXHEADSIZE = 1024;

	public UpdateImageHead(byte[] head) {
		mHeadSize = Arrays.copyOfRange(head, HEADSIZES, HEADSIZEE);
		mVersion = Arrays.copyOfRange(head, VERSIONS, VERSIONE);
		mHardware = Arrays.copyOfRange(head, HARDWARES, HARDWAREE);
		mCheckSum = Arrays.copyOfRange(head, CHECKSUMS, CHECKSUME);
		mDataSize = Arrays.copyOfRange(head, DATASIZES, DATASIZEE);
		mDate = Arrays.copyOfRange(head, DATES, DATEE);
		mTime = Arrays.copyOfRange(head, TIMES, TIMEE);
		mInfo = Arrays.copyOfRange(head, INFOS, INFOE);
	}

	public static boolean isHeadStart(byte[] data) {
		return Arrays.equals(MAGIC, data);
	}

	public static boolean isRealHead(byte[] data) {
		return Arrays.equals(HARDWAREPRE, data);
	}

	public static int getMaxHeadSize() {
		return MAXHEADSIZE;
	}

	public int getHeadSize() {
		return array.bytesToInt(mHeadSize);
	}

	public String getVersion() {
		return String.valueOf(mVersion);
	}

	public String getHardware() {
		return String.valueOf(mHardware).trim();
	}

	public int getDataSize() {
		return array.bytesToInt(mDataSize);
	}

	public String getDate() {
		return String.valueOf(mDate).trim();

	}

	public String getTime() {
		return String.valueOf(mTime).trim();
	}

	public String getInfo() {
		return String.valueOf(mInfo).trim();
	}
}
