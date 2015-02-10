package com.chinaykl.library.util.variable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SystemIO {
	public static String getInput() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String input = reader.readLine();
		return input.trim();
	}
}
