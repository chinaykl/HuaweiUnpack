package com.chinaykl.androidtool.huaweiunpacker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.chinaykl.androidtool.huaweiunpacker.huaweifile.UpdateFile;
import com.chinaykl.library.util.variable.SystemIO;

public class Unpacker {
	private static final String PROGRAMNAME = "Huawei Rom Unpacker";
	private static final String VERSION = "1.01";
	private static final String UPDATEFILE = "UPDATE.APP";
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
			String np = op.trim();
			// real path may be surrounded by ' or "
			// when you replace input with drag a file into it
			if ((np.startsWith("\"") && np.endsWith("\"")) || (np.startsWith("\'") && np.endsWith("\'"))) {
				np = np.substring(1, op.length() - 1);
			}

			// real path may be surrounded by ' or "
			// when you replace input with copy a file into it
			if (np.startsWith("file://")) {
				np = np.replaceFirst("file://", "");
			}

			// to adjust different input
			if (np.endsWith(UPDATEFILE) == false) {
				if (np.endsWith(File.pathSeparator)) {
					np = np + UPDATEFILE;
				} else {
					np = np + File.pathSeparator + UPDATEFILE;
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
			System.out.println(PROGRAMNAME + " v" + VERSION + ":");
			System.out.println("h   help");
			System.out.println("l   list all the image in UPDATE.APP");
			System.out.println("o   out image");
			System.out.println("    -name  <Image Name>");
			System.out.println("    -index <Index>");
			System.out.println("q   quit");
		}

		// deal with input
		private boolean function(String input) throws IOException {
			boolean quit = false;
			String command[] = input.trim().split(" ");
			switch (command[0]) {
			case "h":
				help();
				break;
			case "l":
				listSection();
				break;
			case "o":
				if (command.length == 3) {
					boolean result = false;
					result = export(command[1], command[2]);
					if (result) {
						System.out.println("Image Export Success");
					} else {
						System.out.println("Image Export Fail");
					}
				}
				break;
			case "q":
				quit = true;
				System.out.println("Quit Program");
				break;
			default:
				System.out.println("Unknow Input");
				help();
				break;
			}
			return quit;
		}

		// print sections information of every section
		private void listSection() {
			System.out.print(mFile.getInfo());
		}

		// export selected image
		private boolean export(String type, String value) throws IOException {
			int buffer = 64 * 1024;
			boolean val = false;
			if (type.equalsIgnoreCase("-name")) {
				val = mFile.exportImage(value, 0, buffer);
			} else if (type.equalsIgnoreCase("-index")) {
				int index = 0;
				try {
					index = Integer.valueOf(value);
				} catch (NumberFormatException e) {
					System.out.println("Unknow Input");
					return val;
				}
				val = mFile.exportImage(index, 0, buffer);
			}
			return val;
		}
	}
}