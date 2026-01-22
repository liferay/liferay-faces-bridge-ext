/**
 * Copyright (c) 2000-2025 Liferay, Inc. All rights reserved.
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
package com.liferay.faces.bridge.ext.filter.internal;

import java.util.List;

import jakarta.portlet.MimeResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;


/**
 * This class wraps an instance of jakarta.servlet.http.HttpServletRequest which represents the "original" request that
 * Liferay received that initiated the rendering of the current portlet.
 */
public class LiferayHttpServletRequest extends HttpServletRequestWrapper {
	private HttpServletRequest wrappedHttpServletRequest;

	public LiferayHttpServletRequest(HttpServletRequest httpServletRequest) {
		super(httpServletRequest);
		this.wrappedHttpServletRequest = httpServletRequest;
	}

	@SuppressWarnings("unchecked")
	public List<String> getMarkupHeadElements() {
		return (List<String>) wrappedHttpServletRequest.getAttribute(MimeResponse.MARKUP_HEAD_ELEMENT);
	}

	public void setMarkupHeadElements(List<String> markupHeadElements) {
		wrappedHttpServletRequest.setAttribute(MimeResponse.MARKUP_HEAD_ELEMENT, markupHeadElements);
	}

}
