package com.chinaykl.androidtool.huaweiunpacker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.chinaykl.androidtool.huaweiunpacker.huaweifile.UpdateFile;

public class Unpacker {
	private static final String UPDATEFILE = "UPDATE.APP";
	private static final String VERSION = "1.00";
	private static UpdateFile mFile;

	public static void main(String[] args) {
		Cmd cmd = new Cmd();
		cmd.start();
	}

	private static class Cmd {
		public void start() {
			System.out.println("Enter the path of folder which include UPDATE.APP:");
			System.out.print(">>");
			String path = getInputPath().trim();
			// revise the path of update file
			if (path.endsWith(UPDATEFILE) == false) {
				if (path.endsWith(File.pathSeparator)) {
					path = path + UPDATEFILE;
				} else {
					path = path + File.pathSeparator + UPDATEFILE;
				}
			}

			try {
				mFile = new UpdateFile(path);
			} catch (FileNotFoundException e) {
				System.out.println(UPDATEFILE+" not found and exit");
				return;
			} catch (IOException e) {
				System.out.println(UPDATEFILE+" I/O error");
				return;
			}

			System.out.println(UPDATEFILE+" found");
			mainLoop();
		}

		private String getInputPath() {
			String path = null;
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
				path = reader.readLine();
			} catch (IOException exception) {
				System.out.println("an I/O error occurs");
			}
			return path;
		}

		private void mainLoop() {
			String input = null;
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				System.out.print(">>");
				try {
					input = reader.readLine();
				} catch (IOException exception) {
					System.out.println("an I/O error occurs");
				}
				if (function(input)) {
					break;
				}
			}
		}

		private void helpNote() {
			System.out.println("h  --help");
			System.out.println("l  --list all the image in UPDATE.APP");
			System.out.println("q  --quit");
		}

		private boolean function(String input) {
			boolean quit = false;
			String command[] = input.trim().split(" ");
			switch (command[0]) {
			case "h":
				System.out.println("help:");
				helpNote();
				break;
			case "l":
				System.out.println("list");
				break;
			case "q":
				quit = true;
				System.out.println("quit");
				break;
			default:
				System.out.println("unknow");
				break;
			}
			return quit;
		}
	}
}