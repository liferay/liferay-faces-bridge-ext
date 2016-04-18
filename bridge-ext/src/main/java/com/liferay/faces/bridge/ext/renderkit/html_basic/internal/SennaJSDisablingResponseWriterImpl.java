/**
 * Copyright (c) 2000-2016 Liferay, Inc. All rights reserved.
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
	private ResponseWriter wrappedResponseWriter;

	public SennaJSDisablingResponseWriterImpl(ResponseWriter wrappedResponseWriter) {
		this.wrappedResponseWriter = wrappedResponseWriter;
	}

	@Override
	public void startElement(String name, UIComponent component) throws IOException {

		super.startElement(name, component);

		// FACES-2585 and FACES-2629 Turn off Single Page Application (SennaJS) features for command links and forms.
		if ("a".equals(name) || "form".equals(name)) {
			writeAttribute("data-senna-off", "true", null);
		}
	}

	@Override
	public ResponseWriter getWrapped() {
		return wrappedResponseWriter;
	}
}
