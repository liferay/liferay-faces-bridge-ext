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

import org.junit.Assert;
import org.junit.Test;


/**
 * @author  Kyle Stiemann
 */
public class HTMLUtilTest {

	// Private Constants
	private static final String INVALID_ID_PREFIX = "id_";
	private static final String FIRST_UNICODE_CHARACTER;
	private static final String EXPECTED_FIRST_UNICODE_CHARACTER_AS_ID;
	private static final int FIRST_UNICODE_CHARACTER_AS_INT = 128;
	private static final String INVALID_HTML4_ID_CHARS;
	private static final String VALID_HTML4_ID_CHARS =
		"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_:.";
	private static final String EXPECTED_INVALID_HTML4_ID_CHARS_AS_ID;

	static {

		FIRST_UNICODE_CHARACTER = Character.toString((char) FIRST_UNICODE_CHARACTER_AS_INT);
		EXPECTED_FIRST_UNICODE_CHARACTER_AS_ID = "u00" + toHexString(FIRST_UNICODE_CHARACTER_AS_INT);

		StringBuilder invalideHTML4IdChars = new StringBuilder();
		StringBuilder expectedInvalideHTML4IdCharsAsId = new StringBuilder();

		for (int i = 0; i < FIRST_UNICODE_CHARACTER_AS_INT; i++) {

			char c = (char) i;

			if (!VALID_HTML4_ID_CHARS.contains(Character.toString(c))) {

				invalideHTML4IdChars.append(c);
				expectedInvalideHTML4IdCharsAsId.append("a");

				// Left pad with zeros.
				if (i < (0xF + 1)) {
					expectedInvalideHTML4IdCharsAsId.append("0");
				}

				expectedInvalideHTML4IdCharsAsId.append(toHexString(i));
			}
		}

		INVALID_HTML4_ID_CHARS = invalideHTML4IdChars.toString();
		EXPECTED_INVALID_HTML4_ID_CHARS_AS_ID = expectedInvalideHTML4IdCharsAsId.toString();
	}

	private static String toHexString(int integer) {
		return Integer.toHexString(integer).toUpperCase(Locale.ENGLISH);
	}

	private static String toString(int... unicodeCharsAsInt) {

		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < unicodeCharsAsInt.length; i++) {
			stringBuilder.append((char) unicodeCharsAsInt[i]);
		}

		return stringBuilder.toString();
	}

	@Test
	public void testHTML4Id() {

		Assert.assertEquals("", HTMLUtil.getStringAsHTML4Id(""));
		Assert.assertEquals(VALID_HTML4_ID_CHARS, HTMLUtil.getStringAsHTML4Id(VALID_HTML4_ID_CHARS));
		Assert.assertEquals(INVALID_ID_PREFIX + EXPECTED_INVALID_HTML4_ID_CHARS_AS_ID,
			HTMLUtil.getStringAsHTML4Id(INVALID_HTML4_ID_CHARS));
		Assert.assertEquals(INVALID_ID_PREFIX + EXPECTED_FIRST_UNICODE_CHARACTER_AS_ID,
			HTMLUtil.getStringAsHTML4Id(FIRST_UNICODE_CHARACTER));
		Assert.assertEquals(VALID_HTML4_ID_CHARS + EXPECTED_INVALID_HTML4_ID_CHARS_AS_ID +
			EXPECTED_FIRST_UNICODE_CHARACTER_AS_ID,
			HTMLUtil.getStringAsHTML4Id(VALID_HTML4_ID_CHARS + INVALID_HTML4_ID_CHARS + FIRST_UNICODE_CHARACTER));
		Assert.assertEquals(INVALID_ID_PREFIX + "a26", HTMLUtil.getStringAsHTML4Id("&"));
		Assert.assertEquals(INVALID_ID_PREFIX + "a3C", HTMLUtil.getStringAsHTML4Id("<"));
		Assert.assertEquals(INVALID_ID_PREFIX + "a3E", HTMLUtil.getStringAsHTML4Id(">"));
		Assert.assertEquals(INVALID_ID_PREFIX + "a22", HTMLUtil.getStringAsHTML4Id("\""));
		Assert.assertEquals(INVALID_ID_PREFIX + "a27", HTMLUtil.getStringAsHTML4Id("'"));

		int unicodeCharAsInt = 0x00BB;
		Assert.assertEquals(INVALID_ID_PREFIX + "u00" + toHexString(unicodeCharAsInt),
			HTMLUtil.getStringAsHTML4Id(toString(unicodeCharAsInt)));

		unicodeCharAsInt = 0x2013;
		Assert.assertEquals(INVALID_ID_PREFIX + "u" + toHexString(unicodeCharAsInt),
			HTMLUtil.getStringAsHTML4Id(toString(unicodeCharAsInt)));

		unicodeCharAsInt = 0x2014;
		Assert.assertEquals(INVALID_ID_PREFIX + "u" + toHexString(unicodeCharAsInt),
			HTMLUtil.getStringAsHTML4Id(toString(unicodeCharAsInt)));

		unicodeCharAsInt = 0x2028;
		Assert.assertEquals(INVALID_ID_PREFIX + "u" + toHexString(unicodeCharAsInt),
			HTMLUtil.getStringAsHTML4Id(toString(unicodeCharAsInt)));
		unicodeCharAsInt = 0;

		int[] unicodeCharsAsInt = new int[] { 0xD83D, 0xDE42 };
		Assert.assertEquals(INVALID_ID_PREFIX + "u" + toHexString(unicodeCharsAsInt[0]) + "u" +
			toHexString(unicodeCharsAsInt[1]), HTMLUtil.getStringAsHTML4Id(toString(unicodeCharsAsInt)));
		Assert.assertEquals("liferay-faces-alloy:liferaya2Ffacesa2Falloy.js",
			HTMLUtil.getStringAsHTML4Id("liferay-faces-alloy:liferay/faces/alloy.js"));
		Assert.assertEquals(INVALID_ID_PREFIX + "01_javascript.js", HTMLUtil.getStringAsHTML4Id("01_javascript.js"));
		Assert.assertEquals(INVALID_ID_PREFIX + "_javascript.js", HTMLUtil.getStringAsHTML4Id("_javascript.js"));
		Assert.assertEquals(INVALID_ID_PREFIX + ".javascript.js", HTMLUtil.getStringAsHTML4Id(".javascript.js"));
		Assert.assertEquals(INVALID_ID_PREFIX + ":javascript.js", HTMLUtil.getStringAsHTML4Id(":javascript.js"));
		Assert.assertEquals(INVALID_ID_PREFIX + "-javascript.js", HTMLUtil.getStringAsHTML4Id("-javascript.js"));
	}
}
