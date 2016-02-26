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

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.liferay.faces.bridge.config.BridgeConfig;
import com.liferay.faces.bridge.filter.BridgePortletRequestFactory;


/**
 * @author  Neil Griffin
 */
public class BridgePortletRequestFactoryLiferayImpl extends BridgePortletRequestFactory {

	// Private Members
	private BridgePortletRequestFactory wrappedBridgePortletRequestFactory;

	public BridgePortletRequestFactoryLiferayImpl(BridgePortletRequestFactory wrappedBridgePortletRequestFactory) {
		this.wrappedBridgePortletRequestFactory = wrappedBridgePortletRequestFactory;
	}

	@Override
	public ActionRequest getActionRequest(ActionRequest actionRequest, ActionResponse actionResponse,
		PortletConfig portletConfig, BridgeConfig bridgeConfig) {

		actionRequest = getWrapped().getActionRequest(actionRequest, actionResponse, portletConfig, bridgeConfig);

		return new ActionRequestBridgeLiferayImpl(actionRequest, actionResponse.getNamespace(), portletConfig);
	}

	@Override
	public EventRequest getEventRequest(EventRequest eventRequest, EventResponse eventResponse,
		PortletConfig portletConfig, BridgeConfig bridgeConfig) {

		eventRequest = getWrapped().getEventRequest(eventRequest, eventResponse, portletConfig, bridgeConfig);

		return new EventRequestBridgeLiferayImpl(eventRequest, eventResponse.getNamespace(), portletConfig);
	}

	@Override
	public RenderRequest getRenderRequest(RenderRequest renderRequest, RenderResponse renderResponse,
		PortletConfig portletConfig, BridgeConfig bridgeConfig) {

		renderRequest = getWrapped().getRenderRequest(renderRequest, renderResponse, portletConfig, bridgeConfig);

		return new RenderRequestBridgeLiferayImpl(renderRequest, renderResponse.getNamespace(), portletConfig);
	}

	@Override
	public ResourceRequest getResourceRequest(ResourceRequest resourceRequest, ResourceResponse resourceResponse,
		PortletConfig portletConfig, BridgeConfig bridgeConfig) {

		resourceRequest = getWrapped().getResourceRequest(resourceRequest, resourceResponse, portletConfig,
				bridgeConfig);

		return new ResourceRequestBridgeLiferayImpl(resourceRequest, resourceResponse.getNamespace(), portletConfig);
	}

	@Override
	public BridgePortletRequestFactory getWrapped() {
		return wrappedBridgePortletRequestFactory;
	}
}
