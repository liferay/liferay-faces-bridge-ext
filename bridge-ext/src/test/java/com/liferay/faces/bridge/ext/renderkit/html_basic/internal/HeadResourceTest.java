/**
 * Copyright (c) 2000-2019 Liferay, Inc. All rights reserved.
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

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

import org.junit.Assert;
import org.junit.Test;

import com.liferay.faces.bridge.ext.config.internal.LiferayPortletConfigParam;
import com.liferay.faces.util.lang.NameValuePair;


/**
 * @author  Kyle Stiemann
 */
public class HeadResourceTest {

	private static void assertElementStringDataSennaTrackEquals(String expectedId, StringWriter elementStringWriter) {

		String elementString = elementStringWriter.toString();
		String regexTemplate = "<script\\s+data-senna-track=\"{0}\"\\s*(/>|>\\s*</script>)?";
		String regex = regexTemplate.replace("{0}", expectedId);

		Assert.assertTrue(elementString + " does not match regex: " + regex, elementString.matches(regex));
	}

	private static void assertElementStringIdEquals(String expectedId, StringWriter elementStringWriter) {

		String elementString = elementStringWriter.toString();
		String regexTemplate = "<script\\s+id=\"{0}\"\\s*(/>|>\\s*</script>)?";
		String regex = regexTemplate.replace("{0}", expectedId);

		Assert.assertTrue(elementString + " does not match regex: " + regex, elementString.matches(regex));
	}

	private static void testGetDataSennaTrack(String componentDataSennaTrackValue, boolean styleSheet,
		String libraryName, String primeFacesCSSDefaultDataSennaTrackValue) {

		UIComponent uiComponent = new UIComponentMockImpl(null, null, ResourceRendererLiferayImpl.DATA_SENNA_TRACK,
				componentDataSennaTrackValue);
		NameValuePair<String, Object> dataSennaTrack = ResourceRendererLiferayImpl.getDataSennaTrack(uiComponent,
				styleSheet, libraryName, primeFacesCSSDefaultDataSennaTrackValue);
		Assert.assertEquals(ResourceRendererLiferayImpl.DATA_SENNA_TRACK, dataSennaTrack.getName());
		Assert.assertEquals(componentDataSennaTrackValue, dataSennaTrack.getValue());
	}

	private static void testGetDataSennaTrackWithNoPassThrougAttributes(String expectedDataSennaTrackValue,
		boolean styleSheet, String libraryName, String primeFacesCSSDefaultDataSennaTrackValue) {

		UIComponent uiComponent = new UIComponentMockImpl(null, null);
		NameValuePair<String, Object> dataSennaTrack = ResourceRendererLiferayImpl.getDataSennaTrack(uiComponent,
				styleSheet, libraryName, primeFacesCSSDefaultDataSennaTrackValue);
		Assert.assertEquals(ResourceRendererLiferayImpl.DATA_SENNA_TRACK, dataSennaTrack.getName());
		Assert.assertEquals(expectedDataSennaTrackValue, dataSennaTrack.getValue());
	}

	@Test
	public void testGetDataSennaTrack() throws IOException {

		testGetDataSennaTrack("temporary", false, "foo", "permanent");
		testGetDataSennaTrack("permanent", false, "foo", "temporary");
		testGetDataSennaTrack("temporary", true, "foo", "permanent");
		testGetDataSennaTrack("permanent", true, "foo", "temporary");

		testGetDataSennaTrack("temporary", false, null, "permanent");
		testGetDataSennaTrack("permanent", false, null, "temporary");
		testGetDataSennaTrack("temporary", true, null, "permanent");
		testGetDataSennaTrack("permanent", true, null, "temporary");

		UIComponent uiComponent = new UIComponentMockImpl(null, null);

		NameValuePair<String, Object> dataSennaTrack = ResourceRendererLiferayImpl.getDataSennaTrack(uiComponent, false,
				"foo", "permanent");
		Assert.assertNull(dataSennaTrack);

		dataSennaTrack = ResourceRendererLiferayImpl.getDataSennaTrack(uiComponent, true, "foo", "permanent");
		Assert.assertNull(dataSennaTrack);

		dataSennaTrack = ResourceRendererLiferayImpl.getDataSennaTrack(uiComponent, false, null, "permanent");
		Assert.assertNull(dataSennaTrack);

		dataSennaTrack = ResourceRendererLiferayImpl.getDataSennaTrack(uiComponent, true, null, "permanent");
		Assert.assertNull(dataSennaTrack);

		// PrimeFaces
		dataSennaTrack = ResourceRendererLiferayImpl.getDataSennaTrack(uiComponent, true, "primefaces", null);
		Assert.assertNull(dataSennaTrack);

		dataSennaTrack = ResourceRendererLiferayImpl.getDataSennaTrack(uiComponent, false, "primefaces", "permanent");
		Assert.assertNull(dataSennaTrack);

		dataSennaTrack = ResourceRendererLiferayImpl.getDataSennaTrack(uiComponent, true, "primefaces-theme", null);
		Assert.assertNull(dataSennaTrack);

		dataSennaTrack = ResourceRendererLiferayImpl.getDataSennaTrack(uiComponent, false, "primefaces-theme",
				"permanent");
		Assert.assertNull(dataSennaTrack);

		testGetDataSennaTrackWithNoPassThrougAttributes("temporary", true, "primefaces", "temporary");
		testGetDataSennaTrackWithNoPassThrougAttributes("permanent", true, "primefaces", "permanent");
		testGetDataSennaTrackWithNoPassThrougAttributes("foo", true, "primefaces", "foo");
		testGetDataSennaTrackWithNoPassThrougAttributes("", true, "primefaces", "");

		testGetDataSennaTrackWithNoPassThrougAttributes("temporary", true, "primefaces-theme", "temporary");
		testGetDataSennaTrackWithNoPassThrougAttributes("permanent", true, "primefaces-theme", "permanent");
		testGetDataSennaTrackWithNoPassThrougAttributes("foo", true, "primefaces-theme", "foo");
		testGetDataSennaTrackWithNoPassThrougAttributes("", true, "primefaces-theme", "");

		testGetDataSennaTrack("temporary", true, "primefaces", "permanent");
		testGetDataSennaTrack("permanent", true, "primefaces", "temporary");
		testGetDataSennaTrack("foo", true, "primefaces", "temporary");
		testGetDataSennaTrack("", true, "primefaces", "temporary");
		testGetDataSennaTrack(null, true, "primefaces", "temporary");

		testGetDataSennaTrack("temporary", true, "primefaces-theme", "permanent");
		testGetDataSennaTrack("permanent", true, "primefaces-theme", "temporary");
		testGetDataSennaTrack("foo", true, "primefaces-theme", "temporary");
		testGetDataSennaTrack("", true, "primefaces-theme", "temporary");
		testGetDataSennaTrack(null, true, "primefaces-theme", "temporary");
	}

	@Test
	public void testResponseWriterHeadResourceLiferayImpl() throws IOException {

		// Test id attribute
		StringWriter stringWriter = new StringWriter();
		String resourceName = "name";
		String resourceLibrary = "library";
		ResponseWriter responseWriter = new ResponseWriterHeadResourceLiferayImpl(new ResponseWriterMockImpl(
					stringWriter), null, resourceName, resourceLibrary);

		responseWriter.startElement("script", null);
		responseWriter.endElement("script");
		assertElementStringIdEquals(resourceLibrary + ":" + resourceName, stringWriter);

		stringWriter.getBuffer().setLength(0);
		responseWriter.startElement("script", null);

		String id = "my_id";
		responseWriter.writeAttribute("id", id, null);
		responseWriter.endElement("script");
		assertElementStringIdEquals(id, stringWriter);

		// Test data-senna-track attribute
		stringWriter.getBuffer().setLength(0);

		String dataSennaTrackValue = "temporary";
		NameValuePair<String, Object> dataSennaTrack = new NameValuePair<String, Object>(
				ResourceRendererLiferayImpl.DATA_SENNA_TRACK, dataSennaTrackValue);
		responseWriter = new ResponseWriterHeadResourceLiferayImpl(new ResponseWriterMockImpl(stringWriter),
				dataSennaTrack, null, null);

		responseWriter.startElement("script", null);
		responseWriter.endElement("script");
		assertElementStringDataSennaTrackEquals("temporary", stringWriter);

		stringWriter.getBuffer().setLength(0);
		responseWriter.startElement("script", null);

		dataSennaTrackValue = "permanent";
		responseWriter.writeAttribute(ResourceRendererLiferayImpl.DATA_SENNA_TRACK, dataSennaTrackValue, null);
		responseWriter.endElement("script");
		assertElementStringDataSennaTrackEquals(dataSennaTrackValue, stringWriter);
	}
}
