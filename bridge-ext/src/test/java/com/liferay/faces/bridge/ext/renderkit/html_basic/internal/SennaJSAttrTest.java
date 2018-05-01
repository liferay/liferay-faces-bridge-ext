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
import java.util.Map;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIOutcomeTarget;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;
import javax.faces.render.RenderKit;

import org.junit.Assert;
import org.junit.Test;

import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;


/**
 * @author  Kyle Stiemann
 */
public class SennaJSAttrTest {

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(SennaJSAttrTest.class);

	@Test
	public void runSennaJSDisabledTest() {

		//J-
		/*
		<example:form										pt:data-data-senna-off-true-expected="true" />
		<alloy:form											pt:data-data-senna-off-true-expected="true" />
		<h:form pt:data-senna-off="false"					pt:data-data-senna-off-true-expected="false" />
		<h:form												pt:data-data-senna-off-true-expected="true">

			<!-- FACES-2937 SennaJS disabled for all links which are children of forms -->
			<alloy:link value="alloy:link"					pt:data-data-senna-off-true-expected="false" /><br />
			<p:link value="p:link"							pt:data-data-senna-off-true-expected="false" /><br />
			<h:link value="h:link"							pt:data-data-senna-off-true-expected="false" /><br />
			<!-- /FACES-2937 -->

			<alloy:commandLink value="alloy:commandLink"	pt:data-data-senna-off-true-expected="true" /><br />
			<p:commandLink value="p:commandLink"			pt:data-data-senna-off-true-expected="true" /><br />
			<ace:linkButton value="ace:linkButton"			pt:data-data-senna-off-true-expected="true" /><br />
			<h:link value="h:link"							pt:data-data-senna-off-true-expected="false" /><br />
			<a4j:commandLink value="a4j:commandLink"		pt:data-data-senna-off-true-expected="true" /><br />
			<h:commandLink value="h:commandLink"			pt:data-data-senna-off-true-expected="true" /><br />
			<h:commandLink value="h:commandLink data-senna-off=&quot;false&quot;" pt:data-senna-off="false"
															pt:data-data-senna-off-true-expected="false" /><br />
			<h:commandButton value="h:commandButton"		pt:data-data-senna-off-true-expected="false" /><br />
		</h:form>

		<alloy:link value="alloy:link"						pt:data-data-senna-off-true-expected="false" /><br />
		<p:link value="p:link"								pt:data-data-senna-off-true-expected="false" /><br />
		<h:link value="h:link"								pt:data-data-senna-off-true-expected="false" /><br />
		*/

		RenderKit renderKit = new RenderKitLiferayImpl(new RenderKitMockImpl());
		StringWriter stringWriter = new StringWriter();
		ResponseWriter responseWriter = renderKit.createResponseWriter(stringWriter, null, null);

		// <example:form										pt:data-data-senna-off-true-expected="true" />
		testWriteElement(responseWriter, "form", new UIComponentMockImpl("example", "example"), true);

		// <alloy:form											pt:data-data-senna-off-true-expected="true" />
		testWriteElement(responseWriter, "form",
				new UIComponentMockImpl(UIForm.COMPONENT_FAMILY, "com.liferay.faces.alloy.component.form.FormRenderer",
						"false"),
				false);

		// <h:form pt:data-senna-off="false"					pt:data-data-senna-off-true-expected="false" />
		testWriteElement(responseWriter, "form",
				new UIComponentMockImpl(UIForm.COMPONENT_FAMILY, UIForm.COMPONENT_FAMILY, "false"),
				false);

		// <h:form												pt:data-data-senna-off-true-expected="true">
		testWriteElement(responseWriter, "form",
				new UIComponentMockImpl(UIForm.COMPONENT_FAMILY, UIForm.COMPONENT_FAMILY), false, true);

		//	<!-- FACES-2937 SennaJS disabled for all links which are children of forms -->
		//	<alloy:link value="alloy:link"					pt:data-data-senna-off-true-expected="false" /><br />
		testWriteElement(responseWriter, "a",
				new UIComponentMockImpl(UIOutcomeTarget.COMPONENT_FAMILY,
					"com.liferay.faces.alloy.component.link.LinkRenderer"), false);

		//	<p:link value="p:link"							pt:data-data-senna-off-true-expected="false" /><br />
		testWriteElement(responseWriter, "a",
				new UIComponentMockImpl("org.primefaces.component", "org.primefaces.component.LinkRenderer"),
				false);

		//	<h:link value="h:link"							pt:data-data-senna-off-true-expected="false" /><br />
		testWriteElement(responseWriter, "a",
				new UIComponentMockImpl(UIOutcomeTarget.COMPONENT_FAMILY, "javax.faces.Link"), false);

		//	<!-- /FACES-2937 -->

		//	<alloy:commandLink value="alloy:commandLink"	pt:data-data-senna-off-true-expected="true" /><br />
		testWriteElement(responseWriter, "a",
				new UICommandMockImpl(UICommand.COMPONENT_FAMILY,
					"com.liferay.faces.alloy.component.commandlink.CommandLinkRenderer"), true);

		//	<p:commandLink value="p:commandLink"			pt:data-data-senna-off-true-expected="true" /><br />
		testWriteElement(responseWriter, "a",
				new UICommandMockImpl("org.primefaces.component", "org.primefaces.component.CommandLinkRenderer"),
				true);

		//	<ace:linkButton value="ace:linkButton"			pt:data-data-senna-off-true-expected="true" /><br />
		//	<h:link value="h:link"							pt:data-data-senna-off-true-expected="false" /><br />
		testWriteIceFacesElement(responseWriter);

		//	<a4j:commandLink value="a4j:commandLink"		pt:data-data-senna-off-true-expected="true" /><br />
		testWriteElement(responseWriter, "a",
				new UICommandMockImpl(UICommand.COMPONENT_FAMILY, "org.richfaces.CommandLinkRenderer"), true);

		//	<h:commandLink value="h:commandLink"			pt:data-data-senna-off-true-expected="false" /><br />
		testWriteElement(responseWriter, "a", new UICommandMockImpl(UICommand.COMPONENT_FAMILY, "javax.faces.Link"),
				true);

		//	<h:commandLink value="h:commandLink data-senna-off=&quot;false&quot;" pt:data-senna-off="false"
		//													pt:data-data-senna-off-true-expected="false" /><br />
		testWriteElement(responseWriter, "a",
				new UICommandMockImpl(UICommand.COMPONENT_FAMILY, "javax.faces.Link", "false"),
				false);

		//	<h:commandButton value="h:commandButton"		pt:data-data-senna-off-true-expected="false" /><br />
		testWriteElement(responseWriter, "input",
				new UICommandMockImpl(UICommand.COMPONENT_FAMILY, "javax.faces.Button"), false);

		// </h:form>
		try {
			responseWriter.endElement("form");
		}
		catch (IOException e) {
			throw new AssertionError(e);
		}

		// <alloy:link value="alloy:link"					pt:data-data-senna-off-true-expected="false" /><br />
		testWriteElement(responseWriter, "a",
				new UIComponentMockImpl(UIOutcomeTarget.COMPONENT_FAMILY,
					"com.liferay.faces.alloy.component.link.LinkRenderer"), false);

		// <p:link value="p:link"							pt:data-data-senna-off-true-expected="false" /><br />
		testWriteElement(responseWriter, "a",
				new UIComponentMockImpl("org.primefaces.component", "org.primefaces.component.LinkRenderer"),
				false);

		// <h:link value="h:link"							pt:data-data-senna-off-true-expected="false" /><br />
		testWriteElement(responseWriter, "a",
				new UIComponentMockImpl(UIOutcomeTarget.COMPONENT_FAMILY, "javax.faces.Link"), false);
		//J+
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

	private void testWriteElement(ResponseWriter responseWriter, String name, UIComponent uiComponent,
		boolean dataSennaOffTrueExpected) {
		testWriteElement(responseWriter, name, uiComponent, true, dataSennaOffTrueExpected);
	}

	private void testWriteElement(ResponseWriter responseWriter, String name, UIComponent uiComponent,
		boolean endElement, boolean dataSennaOffTrueExpected) {

		// Reset the responseWriter's internal StringWriter.
		ResponseWriterWrapper responseWriterWrapper = (ResponseWriterWrapper) responseWriter;
		ResponseWriterMockImpl responseWriterMockImpl = (ResponseWriterMockImpl) responseWriterWrapper.getWrapped();
		responseWriterMockImpl.resetStringWriter();

		try {

			responseWriter.startElement(name, uiComponent);

			if (endElement) {
				responseWriter.endElement(name);
			}
		}
		catch (IOException e) {
			throw new AssertionError(e);
		}

		String elementString = responseWriterMockImpl.toString();

		if (logger.isDebugEnabled()) {

			logger.debug("name = \"{0}\"", name);

			if (uiComponent != null) {

				logger.debug("uiComponent instanceof UICommand = {0}", uiComponent instanceof UICommand);
				logger.debug("uiComponent.getFamily() = \"{0}\"", uiComponent.getFamily());
				logger.debug("uiComponent.getRendererType() = \"{0}\"", uiComponent.getRendererType());
				logger.debug("isSennaOffAttrSet(uiComponent) = \"{0}\"", isSennaOffAttrSet(uiComponent));
			}

			logger.debug("elementString = \"{0}\"", elementString);
		}

		if (dataSennaOffTrueExpected) {
			Assert.assertTrue("The element does NOT have the \"data-senna-off\" attribute set to \"true\".",
				elementString.contains("data-senna-off=\"true\""));
		}
		else {
			Assert.assertFalse("The element has the \"data-senna-off\" attribute.",
				elementString.contains("data-senna-off"));
		}
	}

	private void testWriteIceFacesElement(ResponseWriter responseWriter) {

		try {

			responseWriter.startElement("div",
				new UICommandMockImpl("org.icefaces.ace.LinkButton", "org.icefaces.ace.component.LinkButtonRenderer"));
			responseWriter.startElement("span", null);
			responseWriter.startElement("span", null);
			testWriteElement(responseWriter, "a", null, true);
			responseWriter.endElement("span");
			responseWriter.endElement("span");
			responseWriter.endElement("div");

			// Test writing another <a> tag to ensure that the ResponseWriter does not add the data-senna-off="true"
			// attribute once a command link component has completed its rendering.
			testWriteElement(responseWriter, "a", null, false);
		}
		catch (IOException e) {
			throw new AssertionError(e);
		}

	}
}
