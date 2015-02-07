package com.chinaykl.library.util.variable;

public class array {
	public static int bytesToInt(byte[] data) {
		int val = 0;
		if (data.length == 4) {
			for (int i = 0; i < 4; i++) {
				val <<= 8;
				val |= (data[3 - i]&0x0FF);
			}
		}
		return val;
	}
}
