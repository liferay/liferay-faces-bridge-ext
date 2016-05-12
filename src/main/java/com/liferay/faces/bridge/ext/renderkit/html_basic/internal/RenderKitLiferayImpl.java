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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.component.UICommand;
import javax.faces.component.UIForm;
import javax.faces.component.UIOutput;
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
	private static final boolean ICEFACES_DETECTED = ProductMap.getInstance().get(ProductConstants.ICEFACES)
		.isDetected();
	private static final boolean PRIMEFACES_DETECTED = ProductMap.getInstance().get(ProductConstants.PRIMEFACES)
		.isDetected();
	private static final boolean RICHFACES_DETECTED = ProductMap.getInstance().get(ProductConstants.RICHFACES)
		.isDetected();

	// Private Data Members
	private RenderKit wrappedRenderKit;

	public RenderKitLiferayImpl(RenderKit wrappedRenderKit) {
		this.wrappedRenderKit = wrappedRenderKit;
	}

	@Override
	public Renderer getRenderer(String componentFamily, String rendererType) {

		Renderer renderer = super.getRenderer(componentFamily, rendererType);

		// FACES-2585 and FACES-2629 Turn off Single Page Application (SennaJS) features for command links and forms.
		if ((PRIMEFACES_DETECTED && "org.primefaces.component.CommandLinkRenderer".equals(rendererType)) ||
				(RICHFACES_DETECTED && "org.richfaces.CommandLinkRenderer".equals(rendererType)) ||
				(UICommand.COMPONENT_FAMILY.equals(componentFamily) && "javax.faces.Link".equals(rendererType)) ||
				(ICEFACES_DETECTED && "com.icesoft.faces.Form".equals(rendererType)) ||
				(UIForm.COMPONENT_FAMILY.equals(componentFamily) && "javax.faces.Form".equals(rendererType))) {
			renderer = new SennaJSDisablingRenderer(renderer);
		}

		return renderer;
	}

	@Override
	public RenderKit getWrapped() {
		return wrappedRenderKit;
	}
}
