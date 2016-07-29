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

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.liferay.faces.util.map.AbstractPropertyMap;
import com.liferay.faces.util.map.AbstractPropertyMapEntry;


/**
 * @author  Neil Griffin
 */
public class RequestScope extends AbstractPropertyMap<Object> {

	// Private Data Members
	private HttpServletRequest httpServletRequest;

	public RequestScope(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}

	@Override
	protected AbstractPropertyMapEntry<Object> createPropertyMapEntry(String name) {
		return new RequestScopeEntry(httpServletRequest, name);
	}

	@Override
	protected Object getProperty(String name) {
		return httpServletRequest.getAttribute(name);
	}

	@Override
	protected Enumeration<String> getPropertyNames() {
		return (Enumeration<String>) httpServletRequest.getAttributeNames();
	}

	@Override
	protected void removeProperty(String name) {
		httpServletRequest.removeAttribute(name);
	}

	@Override
	protected void setProperty(String name, Object value) {
		httpServletRequest.setAttribute(name, value);
	}

}
