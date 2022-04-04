/**
 * Copyright (c) 2000-2022 Liferay, Inc. All rights reserved.
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
import java.io.Writer;
import java.util.Map;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;


/**
 * The purpose of this class is to turn off Single Page Application (SennaJS) features for certain components. See
 * {@link RenderKitLiferayImpl}, FACES-2585, and FACES-2629 for more details.
 *
 * @author  Kyle Stiemann
 */
public class SennaJSDisablingResponseWriterImpl extends ResponseWriterWrapper {

	// Private Data Members
	private UIComponent currentCommandComponent;
	private int elementsOfCurrentCommandComponent = 0;
	private ResponseWriter wrappedResponseWriter;

	public SennaJSDisablingResponseWriterImpl(ResponseWriter wrappedResponseWriter) {
		this.wrappedResponseWriter = wrappedResponseWriter;
	}

	@Override
	public ResponseWriter cloneWithWriter(Writer writer) {

		ResponseWriter responseWriter = super.cloneWithWriter(writer);

		return RenderKitLiferayImpl.createSennaJSDisablingResponseWriter(responseWriter);
	}

	@Override
	public void endElement(String name) throws IOException {

		elementsOfCurrentCommandComponent--;

		if (elementsOfCurrentCommandComponent <= 0) {

			currentCommandComponent = null;
			elementsOfCurrentCommandComponent = 0;
		}

		super.endElement(name);
	}

	@Override
	public ResponseWriter getWrapped() {
		return wrappedResponseWriter;
	}

	@Override
	public void startElement(String name, UIComponent uiComponent) throws IOException {

		super.startElement(name, uiComponent);

		if (isCommandComponent(uiComponent)) {

			currentCommandComponent = uiComponent;
			elementsOfCurrentCommandComponent = 0;
		}

		if (currentCommandComponent != null) {
			elementsOfCurrentCommandComponent++;
		}

		// FACES-2585 and FACES-2696 Turn off Single Page Application (SennaJS) features for forms and commandLinks.
		if (("a".equals(name) && (currentCommandComponent != null) && !isSennaOffAttrSet(currentCommandComponent)) ||
				("form".equals(name) && !isSennaOffAttrSet(uiComponent))) {
			writeAttribute("data-senna-off", "true", null);
		}
	}

	private boolean isCommandComponent(UIComponent uiComponent) {
		return (uiComponent != null) &&
			((uiComponent instanceof UICommand) || UICommand.COMPONENT_FAMILY.equals(uiComponent.getFamily()));
	}

	private boolean isSennaOffAttrSet(UIComponent uiComponent) {

		boolean sennaOffAttrSet = false;

		if (uiComponent != null) {

			Map<String, Object> passThroughAttributes = uiComponent.getPassThroughAttributes(false);

			if ((passThroughAttributes != null) && !passThroughAttributes.isEmpty()) {
				sennaOffAttrSet = passThroughAttributes.containsKey("data-senna-off");
			}
		}

		return sennaOffAttrSet;
	}
}
