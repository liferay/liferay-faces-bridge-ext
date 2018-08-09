/**
 * Copyright (c) 2000-2018 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.liferay.faces.bridge.ext.util.internal;

import java.util.Locale;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * @author  Kyle Stiemann
 */
public final class HTMLUtil {

	// Private Constants
	private static final int MAX_ASCII_CHARACTER = 127;
	private static final int MAX_ASCII_HEX_STRING_LENGTH = 2;
	private static final int MAX_UTF_HEX_STRING_LENGTH = 4;

	private HTMLUtil() {
		throw new AssertionError();
	}

	/**
	 * Returns a string as a valid HTML4+ id:
	 * https://stackoverflow.com/questions/70579/what-are-valid-values-for-the-id-attribute-in-html.
	 */
	public static String getStringAsHTML4Id(String string) {

		StringBuilder stringBuilder = new StringBuilder();
		char[] resourceIdArray = string.toCharArray();

		for (int i = 0; i < resourceIdArray.length; i++) {

			char c = resourceIdArray[i];
			int charAsInt = (int) c;
			boolean isUnicodeChar = charAsInt > MAX_ASCII_CHARACTER;

			// HTML4 ids must begin with a letter.
			if ((i == 0) && (isUnicodeChar || !Character.isLetter(c))) {
				stringBuilder.append("id_");
			}

			if (isUnicodeChar) {
				appendHexString(stringBuilder, charAsInt, true);
			}
			else if (!Character.isLetterOrDigit(c) && ('-' != c) && ('_' != c) && (':' != c) && ('.' != c)) {
				appendHexString(stringBuilder, charAsInt, false);
			}
			else {
				stringBuilder.append(c);
			}
		}

		return stringBuilder.toString();
	}

	private static void appendHexString(StringBuilder stringBuilder, int charAsInt, boolean unicode) {

		int maxHexStringLength;

		if (unicode) {

			maxHexStringLength = MAX_UTF_HEX_STRING_LENGTH;
			stringBuilder.append("u");
		}
		else {

			maxHexStringLength = MAX_ASCII_HEX_STRING_LENGTH;
			stringBuilder.append("a");
		}

		String hexString = Integer.toHexString(charAsInt).toUpperCase(Locale.ENGLISH);
		int zeroPadLength = maxHexStringLength - hexString.length();

		for (int i = 0; i < zeroPadLength; i++) {
			stringBuilder.append("0");
		}

		stringBuilder.append(hexString);
	}
}
