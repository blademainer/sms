package com.xiongyingqi.utils;

import java.util.Random;

public class StringUtil {
	private static char[] letters;

	static {
		letters = new char[62];
		int j = 0;
		for (int i = 0, k = 'A'; i < 26; i++) {
			letters[j++] = (char) (k + i);
		}
		for (int i = 0, k = 'a'; i < 26; i++) {
			letters[j++] = (char) (k + i);
		}
		for (int i = 0, k = '0'; i < 10; i++) {
			letters[j++] = (char) (k + i);
		}
	}

	public static String randomString() {
		return randomString(8);
	}

	public static String randomString(int length) {
		char[] strChar = new char[length];
		Random random = new Random();
		for (int i = 0; i < strChar.length; i++) {
			strChar[i] = letters[random.nextInt(letters.length)];
		}
		String randomString = new String(strChar);
		return randomString;
	}

	public static void main(String[] args) {
		System.out.println(randomString());
	}
}
