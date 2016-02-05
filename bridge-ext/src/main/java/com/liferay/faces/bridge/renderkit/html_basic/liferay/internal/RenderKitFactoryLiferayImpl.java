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

import java.util.Iterator;

import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;


/**
 * @author  Kyle Stiemann
 */
public class RenderKitFactoryLiferayImpl extends RenderKitFactory {

	// Private Members
	private RenderKitFactory wrappedRenderKitFactory;

	public RenderKitFactoryLiferayImpl(RenderKitFactory wrappedRenderKitFactory) {
		this.wrappedRenderKitFactory = wrappedRenderKitFactory;
	}

	@Override
	public void addRenderKit(String renderKitId, RenderKit renderKit) {
		getWrapped().addRenderKit(renderKitId, renderKit);
	}

	@Override
	public RenderKit getRenderKit(FacesContext context, String renderKitId) {

		RenderKit renderKit = getWrapped().getRenderKit(context, renderKitId);

		if ("HTML_BASIC".equals(renderKitId)) {
			renderKit = new RenderKitLiferayImpl(renderKit);
		}

		return renderKit;
	}

	@Override
	public Iterator<String> getRenderKitIds() {
		return getWrapped().getRenderKitIds();
	}

	@Override
	public RenderKitFactory getWrapped() {
		return wrappedRenderKitFactory;
	}
}
