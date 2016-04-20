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
package com.liferay.faces.bridge.ext.filter.internal;

import java.util.Enumeration;

import javax.portlet.PortalContext;
import javax.portlet.PortletConfig;
import javax.portlet.ResourceRequest;
import javax.portlet.filter.ResourceRequestWrapper;

import com.liferay.faces.bridge.context.BridgePortalContext;


/**
 * @author  Neil Griffin
 */
public class ResourceRequestBridgeLiferayImpl extends ResourceRequestWrapper {

	// Private Data Members
	private BridgePortalContext bridgePortalContext;
	private LiferayPortletRequest liferayPortletRequest;

	public ResourceRequestBridgeLiferayImpl(ResourceRequest resourceRequest, String responseNamespace,
		PortletConfig portletConfig, BridgePortalContext bridgePortalContext) {
		super(resourceRequest);
		this.liferayPortletRequest = new LiferayPortletRequest(resourceRequest, responseNamespace, portletConfig);
		this.bridgePortalContext = bridgePortalContext;
	}

	@Override
	public Object getAttribute(String name) {
		return liferayPortletRequest.getAttribute(name);
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
	public String getProperty(String name) {
		return liferayPortletRequest.getProperty(name);
	}

	@Override
	public Enumeration<String> getPropertyNames() {
		return liferayPortletRequest.getPropertyNames();
	}
}
