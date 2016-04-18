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
package com.liferay.faces.bridge.ext.context.url.internal;

import java.lang.reflect.Method;

import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.filter.PortletResponseWrapper;

import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;


/**
 * @author  Neil Griffin
 */
public class LiferayPortletResponse {

	// Private Constants
	private static final String METHOD_NAME_CREATE_RENDER_URL = "createRenderURL";

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(LiferayPortletResponse.class);

	// Private Data Members
	private PortletResponse wrappedPortletResponse;

	public LiferayPortletResponse(PortletResponse portletResponse) {

		if (portletResponse != null) {

			while (portletResponse instanceof PortletResponseWrapper) {
				PortletResponseWrapper portletResponseWrapper = (PortletResponseWrapper) portletResponse;
				portletResponse = portletResponseWrapper.getResponse();
			}
		}

		this.wrappedPortletResponse = portletResponse;
	}

	public PortletURL createRenderURL() {

		PortletURL renderURL = null;

		try {
			Method method = wrappedPortletResponse.getClass().getMethod(METHOD_NAME_CREATE_RENDER_URL, (Class[]) null);

			renderURL = (PortletURL) method.invoke(wrappedPortletResponse, (Object[]) null);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return renderURL;
	}
}
