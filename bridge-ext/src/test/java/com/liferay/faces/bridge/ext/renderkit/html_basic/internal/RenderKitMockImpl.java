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

import java.io.OutputStream;
import java.io.Writer;

import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;


/**
 * @author  Kyle Stiemann
 */
public class RenderKitMockImpl extends RenderKit {

	@Override
	public void addRenderer(String family, String rendererType, Renderer renderer) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public ResponseStream createResponseStream(OutputStream out) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public ResponseWriter createResponseWriter(Writer writer, String contentTypeList, String characterEncoding) {
		return new ResponseWriterMockImpl(writer);
	}

	@Override
	public Renderer getRenderer(String family, String rendererType) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public ResponseStateManager getResponseStateManager() {
		throw new UnsupportedOperationException("");
	}
}
