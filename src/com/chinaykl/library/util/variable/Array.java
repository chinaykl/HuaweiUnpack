package com.chinaykl.library.util.variable;

public class Array {
	private static final byte[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static int bytesToInt(byte[] data) {
		int val = 0;
		if (data.length == 4) {
			for (int i = 0; i < 4; i++) {
				val <<= 8;
				val |= (data[3 - i] & 0x0FF);
			}
		}
		return val;
	}

	public static byte[] bytesToHex(byte[] data) {
		byte[] val = new byte[data.length * 2];

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < 2; j++) {
				val[i * 2 + j] = HEX[((data[i] >> (4 * (1 - j))) & 0x00f)];
			}
		}

		return val;
	}
}
