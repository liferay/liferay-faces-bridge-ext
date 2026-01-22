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

import java.io.Serializable;

import jakarta.portlet.ActionRequest;
import jakarta.portlet.ActionResponse;
import jakarta.portlet.EventRequest;
import jakarta.portlet.EventResponse;
import jakarta.portlet.HeaderRequest;
import jakarta.portlet.HeaderResponse;
import jakarta.portlet.PortalContext;
import jakarta.portlet.PortletConfig;
import jakarta.portlet.RenderRequest;
import jakarta.portlet.RenderResponse;
import jakarta.portlet.ResourceRequest;
import jakarta.portlet.ResourceResponse;
import jakarta.portlet.faces.BridgeConfig;
import jakarta.portlet.faces.filter.BridgePortletRequestFactory;

import com.liferay.faces.bridge.ext.context.internal.PortalContextBridgeLiferayImpl;


/**
 * @author  Neil Griffin
 */
public class BridgePortletRequestFactoryLiferayImpl extends BridgePortletRequestFactory implements Serializable {

	// serialVersionUID
	private static final long serialVersionUID = 3857140152055099132L;

	// Private Final Data Members
	private final BridgePortletRequestFactory wrappedBridgePortletRequestFactory;

	public BridgePortletRequestFactoryLiferayImpl(BridgePortletRequestFactory wrappedBridgePortletRequestFactory) {
		this.wrappedBridgePortletRequestFactory = wrappedBridgePortletRequestFactory;
	}

	@Override
	public ActionRequest getActionRequest(ActionRequest actionRequest, ActionResponse actionResponse,
		PortletConfig portletConfig, BridgeConfig bridgeConfig) {

		actionRequest = getWrapped().getActionRequest(actionRequest, actionResponse, portletConfig, bridgeConfig);

		PortalContext portalContext = new PortalContextBridgeLiferayImpl(actionRequest);

		return new ActionRequestBridgeLiferayImpl(actionRequest, actionResponse.getNamespace(), portletConfig,
				portalContext);
	}

	@Override
	public EventRequest getEventRequest(EventRequest eventRequest, EventResponse eventResponse,
		PortletConfig portletConfig, BridgeConfig bridgeConfig) {

		eventRequest = getWrapped().getEventRequest(eventRequest, eventResponse, portletConfig, bridgeConfig);

		PortalContext portalContext = new PortalContextBridgeLiferayImpl(eventRequest);

		return new EventRequestBridgeLiferayImpl(eventRequest, eventResponse.getNamespace(), portletConfig,
				portalContext);
	}

	@Override
	public HeaderRequest getHeaderRequest(HeaderRequest headerRequest, HeaderResponse headerResponse,
		PortletConfig portletConfig, BridgeConfig bridgeConfig) {

		headerRequest = getWrapped().getHeaderRequest(headerRequest, headerResponse, portletConfig, bridgeConfig);

		PortalContext portalContext = new PortalContextBridgeLiferayImpl(headerRequest);

		return new HeaderRequestBridgeLiferayImpl(headerRequest, headerResponse.getNamespace(), portletConfig,
				portalContext);
	}

	@Override
	public RenderRequest getRenderRequest(RenderRequest renderRequest, RenderResponse renderResponse,
		PortletConfig portletConfig, BridgeConfig bridgeConfig) {

		renderRequest = getWrapped().getRenderRequest(renderRequest, renderResponse, portletConfig, bridgeConfig);

		PortalContext portalContext = new PortalContextBridgeLiferayImpl(renderRequest);

		return new RenderRequestBridgeLiferayImpl(renderRequest, renderResponse.getNamespace(), portletConfig,
				portalContext);
	}

	@Override
	public ResourceRequest getResourceRequest(ResourceRequest resourceRequest, ResourceResponse resourceResponse,
		PortletConfig portletConfig, BridgeConfig bridgeConfig) {

		resourceRequest = getWrapped().getResourceRequest(resourceRequest, resourceResponse, portletConfig,
				bridgeConfig);

		PortalContext portalContext = new PortalContextBridgeLiferayImpl(resourceRequest);

		return new ResourceRequestBridgeLiferayImpl(resourceRequest, resourceResponse.getNamespace(), portletConfig,
				portalContext);
	}

	@Override
	public BridgePortletRequestFactory getWrapped() {
		return wrappedBridgePortletRequestFactory;
	}
}
