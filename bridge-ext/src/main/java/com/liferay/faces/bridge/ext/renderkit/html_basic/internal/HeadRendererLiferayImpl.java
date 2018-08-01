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
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import javax.faces.render.RendererWrapper;


/**
 * This class simply sets a {@link FacesContext} attribute when rendering begins for the head section and unsets it when
 * rendering ends. This is helpful in determining whether a resource is being rendered in the head sections since some
 * head resources are included in facets. Otherwise we would need to iterate over all head facets and facet children to
 * determine if a resource is being rendered in the head section.
 *
 * @author  Kyle Stiemann
 */
public class HeadRendererLiferayImpl extends RendererWrapper {

	// Private Constants
	private static final String RENDERING_HEAD_SECTION_KEY = HeadRendererLiferayImpl.class.getName() +
		"RENDERING_HEAD_SECTION";

	// Private Final Data Members
	private final Renderer wrappedRenderer;

	public HeadRendererLiferayImpl(Renderer wrappedRenderer) {
		this.wrappedRenderer = wrappedRenderer;
	}

	/**
	 * Returns true if the head section is currently being rendered.
	 */
	/* package-private */ static boolean isRenderingHeadSection(FacesContext facesContext) {

		Map<Object, Object> attributes = facesContext.getAttributes();
		Boolean renderingHeadSection = (Boolean) attributes.get(RENDERING_HEAD_SECTION_KEY);

		return (renderingHeadSection != null) && renderingHeadSection;
	}

	@Override
	public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {

		super.encodeBegin(facesContext, component);

		Map<Object, Object> attributes = facesContext.getAttributes();
		attributes.put(RENDERING_HEAD_SECTION_KEY, true);
	}

	@Override
	public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {

		Map<Object, Object> attributes = facesContext.getAttributes();
		attributes.remove(RENDERING_HEAD_SECTION_KEY);
		super.encodeEnd(facesContext, component);
	}

	@Override
	public Renderer getWrapped() {
		return wrappedRenderer;
	}
}
