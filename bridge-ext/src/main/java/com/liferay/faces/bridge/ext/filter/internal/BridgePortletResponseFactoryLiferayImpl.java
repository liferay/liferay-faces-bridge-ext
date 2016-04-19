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
import com.liferay.faces.bridge.filter.BridgePortletResponseFactory;


/**
 * @author  Neil Griffin
 */
public class BridgePortletResponseFactoryLiferayImpl extends BridgePortletResponseFactory {

	// Private Data Members
	private BridgePortletResponseFactory wrappedBridgePortletResponseFactory;

	public BridgePortletResponseFactoryLiferayImpl(BridgePortletResponseFactory bridgePortletResponseFactory) {
		this.wrappedBridgePortletResponseFactory = bridgePortletResponseFactory;
	}

	@Override
	public ActionResponse getActionResponse(ActionRequest actionRequest, ActionResponse actionResponse,
		PortletConfig portletConfig, BridgeConfig bridgeConfig) {

		ActionResponse wrappedActionResponse = getWrapped().getActionResponse(actionRequest, actionResponse,
				portletConfig, bridgeConfig);

		return new ActionResponseBridgeLiferayImpl(wrappedActionResponse);
	}

	@Override
	public EventResponse getEventResponse(EventRequest eventRequest, EventResponse eventResponse,
		PortletConfig portletConfig, BridgeConfig bridgeConfig) {

		EventResponse wrappedEventResponse = getWrapped().getEventResponse(eventRequest, eventResponse, portletConfig,
				bridgeConfig);

		return new EventResponseBridgeLiferayImpl(wrappedEventResponse);
	}

	@Override
	public RenderResponse getRenderResponse(RenderRequest renderRequest, RenderResponse renderResponse,
		PortletConfig portletConfig, BridgeConfig bridgeConfig) {

		RenderResponse wrappedRenderResponse = getWrapped().getRenderResponse(renderRequest, renderResponse,
				portletConfig, bridgeConfig);

		return new RenderResponseBridgeLiferayImpl(renderRequest, wrappedRenderResponse);
	}

	@Override
	public ResourceResponse getResourceResponse(ResourceRequest resourceRequest, ResourceResponse resourceResponse,
		PortletConfig portletConfig, BridgeConfig bridgeConfig) {

		ResourceResponse wrappedResourceResponse = getWrapped().getResourceResponse(resourceRequest, resourceResponse,
				portletConfig, bridgeConfig);

		return new ResourceResponseBridgeLiferayImpl(resourceRequest, wrappedResourceResponse);
	}

	@Override
	public BridgePortletResponseFactory getWrapped() {
		return wrappedBridgePortletResponseFactory;
	}
}
