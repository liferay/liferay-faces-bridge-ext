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

import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitWrapper;
import javax.faces.render.Renderer;

import com.liferay.faces.util.product.ProductConstants;
import com.liferay.faces.util.product.ProductMap;


/**
 * @author  Kyle Stiemann
 */
public class RenderKitLiferayImpl extends RenderKitWrapper {

	// Private Constants
	private static boolean PRIMEFACES_DETECTED = ProductMap.getInstance().get(ProductConstants.PRIMEFACES).isDetected();
	private static boolean RICHFACES_DETECTED = ProductMap.getInstance().get(ProductConstants.RICHFACES).isDetected();

	// Private Members
	private RenderKit wrappedRenderKit;

	public RenderKitLiferayImpl(RenderKit wrappedRenderKit) {
		this.wrappedRenderKit = wrappedRenderKit;
	}

	@Override
	public Renderer getRenderer(String componentFamily, String rendererType) {

		Renderer renderer = super.getRenderer(componentFamily, rendererType);

		if ((PRIMEFACES_DETECTED && "org.primefaces.component.CommandLinkRenderer".equals(rendererType)) ||
				(RICHFACES_DETECTED && "org.richfaces.CommandLinkRenderer".equals(rendererType)) ||
				("javax.faces.Command".equals(componentFamily) && "javax.faces.Link".equals(rendererType))) {
			renderer = new CommandLinkRendererLiferayImpl(renderer);
		}

		return renderer;
	}

	@Override
	public RenderKit getWrapped() {
		return wrappedRenderKit;
	}
}
