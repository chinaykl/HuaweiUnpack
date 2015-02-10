package com.chinaykl.library.util.variable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Stream {
	private static final int MAXERROR = 100;

	// skip <num> bytes in <is>
	// the max bytes skipped one time is <onetime>
	public static long skip(InputStream is, int num, int onetime) throws IOException {
		int realskip = 0;
		int willskip = num;
		// when skip can not continue ,it should get out of function
		int err = 0;
		while (realskip < num) {
			long rs = 0;
			if (willskip >= onetime) {
				rs = is.skip(onetime);
				realskip += rs;
			} else {
				rs = is.skip(willskip);
				realskip += rs;
			}
			willskip = num - realskip;

			// check error
			if (rs == 0) {
				err++;
				if (err > MAXERROR) {
					throw new IOException("skip error");
				}
			} else {
				err = 0;
			}
		}
		return realskip;
	}

	// copy <num> bytes in <is> to <os>
	// the max bytes copied one time is <onetime>
	public static long copy(InputStream is, OutputStream os, int num, int onetime) throws IOException {
		int realcopy = 0;
		int willcopy = num;
		// when skip can not continue ,it should get out of function
		int err = 0;

		// create buffer
		byte[] data = new byte[onetime];

		// start to transfer
		while (realcopy < num) {
			int rr = 0;
			if (willcopy >= onetime) {
				rr = is.read(data);
				os.write(data, 0, rr);
				realcopy += rr;
			} else {
				rr = is.read(data, 0, willcopy);
				os.write(data, 0, rr);
				realcopy += rr;
			}
			willcopy = num - realcopy;

			// check error
			if (rr == 0) {
				err++;
				if (err > MAXERROR) {
					throw new IOException("copy error");
				}
			} else {
				err = 0;
			}
		}
		return realcopy;
	}
}
