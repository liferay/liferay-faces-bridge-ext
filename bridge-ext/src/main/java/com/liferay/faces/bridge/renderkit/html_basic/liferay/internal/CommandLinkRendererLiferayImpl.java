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
package com.liferay.faces.bridge.renderkit.html_basic.liferay.internal;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.render.RendererWrapper;


/**
 * @author  Kyle Stiemann
 */
public class CommandLinkRendererLiferayImpl extends RendererWrapper {

	private Renderer wrappedRenderer;

	public CommandLinkRendererLiferayImpl(Renderer wrappedRenderer) {
		this.wrappedRenderer = wrappedRenderer;
	}

	@Override
	public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {

		ResponseWriter responseWriter = facesContext.getResponseWriter();
		ResponseWriter responseWriterCommandLinkImpl = new CommandLinkResponseWriterLiferayImpl(responseWriter);
		facesContext.setResponseWriter(responseWriterCommandLinkImpl);
		super.encodeBegin(facesContext, uiComponent);
		facesContext.setResponseWriter(responseWriter);
	}

	@Override
	public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {

		ResponseWriter responseWriter = facesContext.getResponseWriter();
		ResponseWriter responseWriterCommandLinkImpl = new CommandLinkResponseWriterLiferayImpl(responseWriter);
		facesContext.setResponseWriter(responseWriterCommandLinkImpl);
		super.encodeChildren(facesContext, uiComponent);
		facesContext.setResponseWriter(responseWriter);
	}

	@Override
	public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {

		ResponseWriter responseWriter = facesContext.getResponseWriter();
		ResponseWriter responseWriterCommandLinkImpl = new CommandLinkResponseWriterLiferayImpl(responseWriter);
		facesContext.setResponseWriter(responseWriterCommandLinkImpl);
		super.encodeEnd(facesContext, uiComponent);
		facesContext.setResponseWriter(responseWriter);
	}

	@Override
	public Renderer getWrapped() {
		return wrappedRenderer;
	}
}
