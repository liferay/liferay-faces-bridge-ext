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

import javax.portlet.EventRequest;
import javax.portlet.PortalContext;
import javax.portlet.PortletConfig;
import javax.portlet.filter.EventRequestWrapper;

import com.liferay.faces.bridge.context.liferay.internal.PortalContextBridgeLiferayImpl;


/**
 * @author  Neil Griffin
 */
public class EventRequestBridgeLiferayImpl extends EventRequestWrapper {

	// Private Data Members
	private PortalContext portalContext;
	private LiferayPortletRequest liferayPortletRequest;

	public EventRequestBridgeLiferayImpl(EventRequest eventRequest, String responseNamespace,
		PortletConfig portletConfig) {
		super(eventRequest);
		this.liferayPortletRequest = new LiferayPortletRequest(eventRequest, responseNamespace, portletConfig);
		this.portalContext = new PortalContextBridgeLiferayImpl(eventRequest);
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
