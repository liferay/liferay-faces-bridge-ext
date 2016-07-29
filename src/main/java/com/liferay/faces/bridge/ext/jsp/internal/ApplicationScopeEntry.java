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
package com.liferay.faces.bridge.ext.jsp.internal;

import javax.servlet.ServletContext;

import com.liferay.faces.util.map.AbstractPropertyMapEntry;


/**
 * @author  Neil Griffin
 */
public class ApplicationScopeEntry extends AbstractPropertyMapEntry<Object> {

	// Private Data Members
	private ServletContext servletContext;

	public ApplicationScopeEntry(ServletContext servletContext, String key) {
		super(key);
		this.servletContext = servletContext;
	}

	public Object getValue() {
		return servletContext.getAttribute(getKey());
	}

	public Object setValue(Object value) {

		Object oldValue = getValue();
		servletContext.setAttribute(getKey(), value);

		return oldValue;
	}

}
