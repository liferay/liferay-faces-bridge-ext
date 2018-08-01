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
package com.liferay.faces.bridge.ext.renderkit.html_basic.internal;

import java.io.IOException;
import java.io.StringWriter;

import javax.faces.context.ResponseWriter;

import org.junit.Assert;
import org.junit.Test;

import com.liferay.faces.bridge.ext.util.internal.HTMLUtil;


/**
 * @author  Kyle Stiemann
 */
public class ResponseWriterHeadResourceLiferayImplTest {

	private static void assertElementStringIdEquals(String expectedId, StringWriter elementStringWriter) {

		String elementString = elementStringWriter.toString();
		String regexTemplate = "<script\\s+id=\"{0}\"\\s*(/>|>\\s*</script>)?";
		String regex = regexTemplate.replace("{0}", expectedId);

		Assert.assertTrue(elementString + " does not match regex: " + regex, elementString.matches(regex));
	}

	@Test
	public void testResponseWriterHeadResourceLiferayImpl() throws IOException {

		StringWriter stringWriter = new StringWriter();
		String resourceName = "name";
		String resourceLibrary = "library";
		ResponseWriter responseWriter = new ResponseWriterHeadResourceLiferayImpl(new ResponseWriterMockImpl(
					stringWriter), resourceName, resourceLibrary);

		responseWriter.startElement("script", null);
		responseWriter.endElement("script");
		assertElementStringIdEquals(HTMLUtil.getStringAsHTML4Id(resourceLibrary + ":" + resourceName), stringWriter);

		stringWriter.getBuffer().setLength(0);
		responseWriter.startElement("script", null);

		String id = "my_id";
		responseWriter.writeAttribute("id", id, null);
		responseWriter.endElement("script");
		assertElementStringIdEquals(id, stringWriter);
	}
}
