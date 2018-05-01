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

import java.io.Writer;

import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitWrapper;


/**
 * @author  Kyle Stiemann
 */
public class RenderKitLiferayImpl extends RenderKitWrapper {

	// Private Data Members
	private RenderKit wrappedRenderKit;

	public RenderKitLiferayImpl(RenderKit wrappedRenderKit) {
		this.wrappedRenderKit = wrappedRenderKit;
	}

	public static ResponseWriter createSennaJSDisablingResponseWriter(ResponseWriter responseWriter) {

		ResponseWriter responseWriterToReturn = responseWriter;

		if (!isSennaJSDisablingResponseWriter(responseWriterToReturn)) {
			responseWriterToReturn = new SennaJSDisablingResponseWriterImpl(responseWriter);
		}

		return responseWriterToReturn;
	}

	private static boolean isSennaJSDisablingResponseWriter(ResponseWriter responseWriter) {

		if (responseWriter instanceof SennaJSDisablingResponseWriterImpl) {
			return true;
		}
		else if (responseWriter instanceof ResponseWriterWrapper) {

			ResponseWriterWrapper responseWriterWrapper = (ResponseWriterWrapper) responseWriter;

			return isSennaJSDisablingResponseWriter(responseWriterWrapper.getWrapped());
		}
		else {
			return false;
		}
	}

	@Override
	public ResponseWriter createResponseWriter(Writer writer, String contentTypeList, String characterEncoding) {

		ResponseWriter responseWriter = super.createResponseWriter(writer, contentTypeList, characterEncoding);

		return createSennaJSDisablingResponseWriter(responseWriter);
	}

	@Override
	public RenderKit getWrapped() {
		return wrappedRenderKit;
	}
}
