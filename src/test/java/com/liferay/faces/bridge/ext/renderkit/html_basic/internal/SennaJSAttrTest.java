/**
 * Copyright (c) 2000-2017 Liferay, Inc. All rights reserved.
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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;

import org.junit.Assert;
import org.junit.Test;

import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;
import com.liferay.faces.util.product.Product;


/**
 * @author  Kyle Stiemann
 */
public class SennaJSAttrTest {

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(SennaJSAttrTest.class);

	@Test
	public void runFACES_2937Test() {

		RenderKit renderKit = new RenderKitLiferayImpl(new RenderKitMockImpl());
		StringWriter stringWriter = new StringWriter();
		ResponseWriter responseWriter = renderKit.createResponseWriter(stringWriter, null, null);
		testStartElement(stringWriter, responseWriter, "form", new UIComponentMockImpl("form", "form"));
		testStartElement(stringWriter, responseWriter, "a", new UIComponentMockImpl("a", "a"));
	}

	@Test
	public void runSennaJSDisabledTest() {

		String primefacesProductName = Product.Name.PRIMEFACES.toString();
		String richfacesProductName = Product.Name.RICHFACES.toString();

		try {

			// Ensure that relevant component libraries are temporarily set to detected for this test.
			setDetected(primefacesProductName, true);
			setDetected(richfacesProductName, true);

			RenderKit renderKit = new RenderKitLiferayImpl(new RenderKitMockImpl());
			StringWriter stringWriter = new StringWriter();

			// CommandLinks
			testStartElement(stringWriter, renderKit, "a",
				new UIComponentMockImpl("org.primefaces.component.CommandLinkRenderer"));
			testStartElement(stringWriter, renderKit, "a",
				new UIComponentMockImpl("org.primefaces.component.CommandLinkRenderer", true));
			testStartElement(stringWriter, renderKit, "a",
				new UIComponentMockImpl(UICommand.COMPONENT_FAMILY, "javax.faces.Link"));
			testStartElement(stringWriter, renderKit, "a",
				new UIComponentMockImpl(UICommand.COMPONENT_FAMILY, "javax.faces.Link", false));
			testStartElement(stringWriter, renderKit, "a",
				new UIComponentMockImpl("org.richfaces.CommandLinkRenderer"));
			testStartElement(stringWriter, renderKit, "a",
				new UIComponentMockImpl("org.richfaces.CommandLinkRenderer", true));

			// Forms
			testStartElement(stringWriter, renderKit, "form", new UIComponentMockImpl("form"));
			testStartElement(stringWriter, renderKit, "form", new UIComponentMockImpl("form", true));

			// Non-CommandLink links
			testStartElement(stringWriter, renderKit, "a", new UIComponentMockImpl("a", "a"));

			// Other Elements
			testStartElement(stringWriter, renderKit, "script", null);
			testStartElement(stringWriter, renderKit, "style", new UIComponentMockImpl("style", "style"));
			testStartElement(stringWriter, renderKit, "link", new UIComponentMockImpl("link", true));
		}
		finally {

			// Reset component library settings to the default of undetected.
			setDetected(primefacesProductName, false);
			setDetected(richfacesProductName, false);
		}
	}

	private boolean isCommandLink(String elementName, UIComponent uiComponent) {

		boolean commandLink = false;

		if ("a".equals(elementName) && (uiComponent != null)) {

			String componentFamily = uiComponent.getFamily();
			String rendererType = uiComponent.getRendererType();

			commandLink = (("org.primefaces.component.CommandLinkRenderer".equals(rendererType)) ||
					("org.richfaces.CommandLinkRenderer".equals(rendererType)) ||
					(UICommand.COMPONENT_FAMILY.equals(componentFamily) && "javax.faces.Link".equals(rendererType)));
		}

		return commandLink;
	}

	private boolean isSennaOffAttrSet(UIComponent uiComponent) {

		boolean sennaOffAttrSet = false;

		if (uiComponent != null) {

			Map<String, Object> passThroughAttributes = uiComponent.getPassThroughAttributes();

			if ((passThroughAttributes != null) && !passThroughAttributes.isEmpty()) {
				sennaOffAttrSet = passThroughAttributes.containsKey("data-senna-off");
			}
		}

		return sennaOffAttrSet;
	}

	private void setDetected(String productName, boolean detected) {

		try {

			Field productDetectedField = SennaJSDisablingResponseWriterImpl.class.getDeclaredField(productName +
					"_DETECTED");
			productDetectedField.setAccessible(true);

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(productDetectedField, productDetectedField.getModifiers() & ~Modifier.FINAL);
			productDetectedField.setBoolean(null, detected);
			modifiersField.setInt(productDetectedField, productDetectedField.getModifiers() & Modifier.FINAL);
			productDetectedField.setAccessible(false);
		}
		catch (Exception e) {
			throw new AssertionError(e);
		}
	}

	private void testStartElement(StringWriter stringWriter, RenderKit renderKit, String name,
		UIComponent uiComponent) {

		ResponseWriter responseWriter = renderKit.createResponseWriter(stringWriter, null, null);
		testStartElement(stringWriter, responseWriter, name, uiComponent);
	}

	private void testStartElement(StringWriter stringWriter, ResponseWriter responseWriter, String name,
		UIComponent uiComponent) {

		// Reset the responseWriter's internal StringWriter.
		stringWriter.getBuffer().setLength(0);

		try {
			responseWriter.startElement(name, uiComponent);
		}
		catch (IOException e) {
			throw new AssertionError(e);
		}

		String elementString = stringWriter.toString();

		if (logger.isDebugEnabled()) {

			logger.debug("name = \"{0}\"", name);

			if (uiComponent != null) {

				logger.debug("uiComponent.getFamily() = \"{0}\"", uiComponent.getFamily());
				logger.debug("uiComponent.getRendererType() = \"{0}\"", uiComponent.getRendererType());
				logger.debug("isSennaOffAttrSet(uiComponent) = \"{0}\"", isSennaOffAttrSet(uiComponent));
			}

			logger.debug("elementString = \"{0}\"", elementString);
		}

		if ((name.equals("form") || isCommandLink(name, uiComponent)) && !isSennaOffAttrSet(uiComponent)) {
			Assert.assertTrue("The element does NOT have the \"data-senna-off\" attribute set to \"true\".",
				elementString.contains("data-senna-off=\"true\""));
		}
		else {
			Assert.assertFalse("The element has the \"data-senna-off\" attribute.",
				elementString.contains("data-senna-off"));
		}
	}
}
