/**
 * Copyright (c) 2000-2017 Liferay, Inc. All rights reserved.
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

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponentBase;


/**
 * @author  Kyle Stiemann
 */
public class UIComponentMockImpl extends UIComponentBase {

	// Private Data Members
	private String componentFamily;
	private Map<String, Object> passThroughAttrs;
	private String rendererType;

	public UIComponentMockImpl(String componentFamily, String rendererType) {

		this.rendererType = rendererType;
		this.componentFamily = componentFamily;
	}

	public UIComponentMockImpl(String componentFamily, String rendererType, String dataSennaOff) {

		this(rendererType, componentFamily);
		passThroughAttrs = new HashMap<String, Object>();
		passThroughAttrs.put("data-senna-off", dataSennaOff);
	}

	@Override
	public String getFamily() {
		return componentFamily;
	}

	@Override
	public Map<String, Object> getPassThroughAttributes(boolean create) {
		return passThroughAttrs;
	}

	@Override
	public String getRendererType() {
		return rendererType;
	}
}
