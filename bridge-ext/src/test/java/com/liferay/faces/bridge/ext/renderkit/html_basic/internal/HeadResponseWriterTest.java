/**
 * Copyright (c) 2000-2021 Liferay, Inc. All rights reserved.
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
package com.liferay.faces.bridge.ext.renderkit.html_basic.internal;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author  Kyle Stiemann
 */
public class HeadResponseWriterTest {

	/**
	 * Verify that the {@link HeadResponseWriterLiferayImpl} correctly handles nested elements (for example in a script
	 * template). For more details see <a href="https://issues.liferay.com/browse/FACES-3231">
	 * https://issues.liferay.com/browse/FACES-3231</a>.
	 *
	 * @throws  IOException
	 */
	@Test
	public void testFACES_3231() throws IOException {

		HeadResponseWriterLiferayMockImpl headResponseWriterLiferayMockImpl = new HeadResponseWriterLiferayMockImpl(
				new ResponseWriterMockImpl(new StringWriter()));
		headResponseWriterLiferayMockImpl.startElement("script", null);
		headResponseWriterLiferayMockImpl.writeAttribute("id", "script:template", null);
		headResponseWriterLiferayMockImpl.writeAttribute("type", "data/template", null);
		headResponseWriterLiferayMockImpl.writeAttribute("data-senna-track", "temporary", null);
		headResponseWriterLiferayMockImpl.write("Template Text 1");
		headResponseWriterLiferayMockImpl.startElement("div", null);
		headResponseWriterLiferayMockImpl.writeAttribute("class", "template:div", null);
		headResponseWriterLiferayMockImpl.write("Div Text 1");
		headResponseWriterLiferayMockImpl.startElement("a", null);
		headResponseWriterLiferayMockImpl.writeAttribute("href", "http://liferay.com", null);
		headResponseWriterLiferayMockImpl.write("liferay");
		headResponseWriterLiferayMockImpl.endElement("a");
		headResponseWriterLiferayMockImpl.write("Div Text 2");
		headResponseWriterLiferayMockImpl.endElement("div");
		headResponseWriterLiferayMockImpl.write("Template Text 2");
		headResponseWriterLiferayMockImpl.startElement("span", null);
		headResponseWriterLiferayMockImpl.write("Span Text");
		headResponseWriterLiferayMockImpl.endElement("span");
		headResponseWriterLiferayMockImpl.endElement("script");

		String elementString = headResponseWriterLiferayMockImpl.getLastNodeAsString();

		//J-
		String expectedElementStringRegex =
			"<script\\s+id=\"script:template\"\\s+type=\"data/template\"\\s+data-senna-track=\"temporary\"\\s*>" +
				"\\s*Template Text 1\\s*" +
				"<div\\s+class=\"template:div\">" +
					"\\s*Div Text 1?" +
					"<a\\s+href=\"http://liferay[.]com\">liferay</a>" +
					"\\s*Div Text 2\\s*" +
				"</div>" +
				"\\s*Template Text 2\\s*" +
				"<span>\\s*Span Text\\s*</span>" +
			"</script>";
		//J+
		Assert.assertTrue("Nested head elements were not written correctly.",
			elementString.matches(expectedElementStringRegex));
	}
}
