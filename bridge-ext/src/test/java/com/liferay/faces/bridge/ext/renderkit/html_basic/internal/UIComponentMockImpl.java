/**
 * Copyright (c) 2000-2020 Liferay, Inc. All rights reserved.
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponentBase;


/**
 * @author  Kyle Stiemann
 */
public class UIComponentMockImpl extends UIComponentBase {

	// Private Data Members
	private final String componentFamily;
	private final Map<String, Object> passThroughAttrs;
	private final String rendererType;

	public UIComponentMockImpl(String componentFamily, String rendererType) {

		this.rendererType = rendererType;
		this.componentFamily = componentFamily;
		this.passThroughAttrs = null;
	}

	public UIComponentMockImpl(String componentFamily, String rendererType, String dataSennaName,
		String dataSennaValue) {

		this.rendererType = rendererType;
		this.componentFamily = componentFamily;

		if (dataSennaName != null) {

			Map<String, Object> passThroughAttrs = new HashMap<String, Object>();
			passThroughAttrs.put(dataSennaName, dataSennaValue);
			this.passThroughAttrs = Collections.unmodifiableMap(passThroughAttrs);
		}
		else {
			passThroughAttrs = null;
		}
	}

	@Override
	public String getFamily() {
		return componentFamily;
	}

	@Override
	public Map<String, Object> getPassThroughAttributes(boolean create) {

		if (create) {
			throw new UnsupportedOperationException();
		}

		return passThroughAttrs;
	}

	@Override
	public String getRendererType() {
		return rendererType;
	}
}
