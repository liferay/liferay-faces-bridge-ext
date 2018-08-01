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

import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;


/**
 * @author  Kyle Stiemann
 */
public class ResponseWriterHeadResourceLiferayImpl extends ResponseWriterWrapper {

	// Private Final Data Members
	private final ResponseWriter wrappedResponseWriter;
	private final String resourceName;
	private final String resourceLibrary;

	// Private Data Members
	private boolean idAttributeWritten;

	public ResponseWriterHeadResourceLiferayImpl(ResponseWriter wrappedResponseWriter, String resourceName,
		String resourceLibrary) {

		this.wrappedResponseWriter = wrappedResponseWriter;
		this.resourceName = resourceName;
		this.resourceLibrary = resourceLibrary;
	}

	@Override
	public void endElement(String name) throws IOException {

		if (!idAttributeWritten) {

			String resourceId = resourceName;

			if (resourceLibrary != null) {
				resourceId = resourceLibrary + ":" + resourceId;
			}

			if (resourceId != null) {
				writeAttribute("id", resourceId, null);
			}
		}

		super.endElement(name);
	}

	@Override
	public ResponseWriter getWrapped() {
		return wrappedResponseWriter;
	}

	@Override
	public void writeAttribute(String name, Object value, String property) throws IOException {

		super.writeAttribute(name, value, property);

		if (!idAttributeWritten && "id".equals(name)) {
			idAttributeWritten = true;
		}
	}
}
