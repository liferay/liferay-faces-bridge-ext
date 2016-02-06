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
package com.liferay.faces.bridge.filter.liferay.internal;

import java.util.Enumeration;

import javax.portlet.PortalContext;
import javax.portlet.RenderRequest;
import javax.portlet.filter.RenderRequestWrapper;

import com.liferay.faces.bridge.context.BridgePortalContext;


/**
 * @author  Neil Griffin
 */
public class RenderRequestBridgeLiferayImpl extends RenderRequestWrapper {

	// Private Data Members
	private BridgePortalContext bridgePortalContext;
	private LiferayPortletRequest liferayPortletRequest;

	public RenderRequestBridgeLiferayImpl(RenderRequest renderRequest, BridgePortalContext bridgePortalContext) {
		super(renderRequest);
		this.bridgePortalContext = bridgePortalContext;
		this.liferayPortletRequest = new LiferayPortletRequest(renderRequest);
	}

	@Override
	public Object getAttribute(String name) {

		// If the specified name is a Servlet-API reserved attribute name, then the JSF runtime is attempting to
		// determine the viewId for a webapp environment. Because of this, it is necessary to return null so that the
		// JSF runtime will attempt to determine the viewId a different way, namely by calling
		// ExternalContext#getRequestPathInfo() or ExternalContext#getRequestServletPath().
		if ("javax.servlet.include.path_info".equals(name) || "javax.servlet.include.servlet_path".equals(name)) {
			return null;
		}
		else {
			return super.getAttribute(name);
		}
	}

	@Override
	public PortalContext getPortalContext() {
		return bridgePortalContext;
	}

	@Override
	public Enumeration<String> getProperties(String name) {
		return liferayPortletRequest.getProperties(name);
	}

	@Override
	public Enumeration<String> getPropertyNames() {
		return liferayPortletRequest.getPropertyNames();
	}
}
