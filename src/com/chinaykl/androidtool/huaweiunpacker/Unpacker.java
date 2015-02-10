package com.chinaykl.androidtool.huaweiunpacker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.chinaykl.androidtool.huaweiunpacker.huaweifile.UpdateFile;
import com.chinaykl.androidtool.huaweiunpacker.huaweifile.UpdateImageHead;
import com.chinaykl.androidtool.huaweiunpacker.huaweifile.UpdateSection;
import com.chinaykl.library.util.variable.SystemIO;

public class Unpacker {
	private static final String UPDATEFILE = "UPDATE.APP";
	private static final String VERSION = "1.00";
	private static UpdateFile mFile;

	public static void main(String[] args) {
		Cmd cmd = new Cmd();
		cmd.start();
	}

	private static class Cmd {
		// start an environment to handle update file
		public void start() {
			System.out.println("Enter the path of " + UPDATEFILE);
			System.out.print(">>");

			// get path input by user
			String oldPath = null;
			try {
				oldPath = SystemIO.getInput();
			} catch (IOException exception) {
				System.out.println("An I/O error occured when got input");
			}

			// revise the path of update file
			String newPath = revisePath(oldPath);

			try {
				System.out.println("Finding " + UPDATEFILE + " ...");
				mFile = new UpdateFile(newPath);
			} catch (FileNotFoundException e) {
				System.out.println(UPDATEFILE + " was not found and exit");
				return;
			} catch (IOException e) {
				System.out.println("I/O error occured when open " + UPDATEFILE);
				return;
			}

			System.out.println(UPDATEFILE + " open successful");

			// main function
			mainLoop();
		}

		// to adjust to different possible path
		private String revisePath(String op) {
			String np = null;
			if (op.endsWith(UPDATEFILE) == false) {
				if (op.endsWith(File.pathSeparator)) {
					np = op + UPDATEFILE;
				} else {
					np = op + File.pathSeparator + UPDATEFILE;
				}
			}
			return np;
		}

		// get input and deal with it
		private void mainLoop() {
			String input = null;
			while (true) {
				System.out.print(">>");
				try {
					input = SystemIO.getInput();
				} catch (IOException exception) {
					System.out.println("An I/O error occured when got input");
				}
				try {
					if (function(input)) {
						break;
					}
				} catch (IOException e) {
					System.out.println("An I/O error occurs when deal with input");
				}
			}
		}

		// print help information
		private void help() {
			System.out.println("Unpacker v" + VERSION + ":");
			System.out.println("-h  --help");
			System.out.println("-l  --list all the image in UPDATE.APP");
			System.out.println("-o  --out image");
			System.out.println("-q  --quit");
		}

		// deal with input
		private boolean function(String input) throws IOException {
			boolean quit = false;
			String command[] = input.trim().split(" ");
			switch (command[0]) {
			case "-h":
				help();
				break;
			case "-l":
				listSection();
				break;
			case "-o":
				if (command.length == 2) {
					int index = Integer.valueOf(command[1]);
					exportImage(index);
				}
				break;
			case "-q":
				quit = true;
				System.out.println("quit");
				break;
			default:
				System.out.println("unknow");
				break;
			}
			return quit;
		}

		// print sections information of every section
		private void listSection() {
			ArrayList<UpdateSection> list = mFile.getSections();
			for (int i = 0; i < list.size(); i++) {
				UpdateSection us = list.get(i);
				UpdateImageHead uih = us.getImageHead();
				System.out.println("Section " + i + ":" + uih.getInfo());
				System.out.println("OFFSET:    " + us.getOffset());
				System.out.println("HEADSIZE:  " + uih.getHeadSize());
				System.out.println("DATASIZE:  " + uih.getDataSize());
				System.out.println("END:       " + (us.getOffset() + uih.getHeadSize() + uih.getDataSize()));
				System.out.println();
			}
		}

		// export selected image
		private void exportImage(int index) {
			int buffer = 64 * 1024;
			try {
				mFile.exportImageFormFile(index, 0, buffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}