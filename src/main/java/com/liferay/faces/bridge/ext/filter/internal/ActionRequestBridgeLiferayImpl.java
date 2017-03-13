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

import javax.portlet.ActionRequest;
import javax.portlet.PortalContext;
import javax.portlet.PortletConfig;
import javax.portlet.filter.ActionRequestWrapper;


/**
 * @author  Neil Griffin
 */
public class ActionRequestBridgeLiferayImpl extends ActionRequestWrapper {

	// Private Data Members
	private PortalContext portalContext;
	private LiferayPortletRequest liferayPortletRequest;

	public ActionRequestBridgeLiferayImpl(ActionRequest actionRequest, String responseNamespace,
		PortletConfig portletConfig, PortalContext portalContext) {
		super(actionRequest);
		this.liferayPortletRequest = new LiferayPortletRequest(actionRequest, responseNamespace, portletConfig);
		this.portalContext = portalContext;
	}

	@Override
	public Object getAttribute(String name) {
		return liferayPortletRequest.getAttribute(name);
	}

	@Override
	public PortalContext getPortalContext() {
		return portalContext;
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
