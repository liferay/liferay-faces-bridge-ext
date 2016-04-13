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
import javax.portlet.PortalContext;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.liferay.faces.bridge.config.BridgeConfig;
import com.liferay.faces.bridge.context.BridgePortalContext;
import com.liferay.faces.bridge.ext.context.internal.BridgePortalContextLiferayImpl;
import com.liferay.faces.bridge.filter.BridgePortletRequestFactory;
import com.liferay.faces.util.product.Product;
import com.liferay.faces.util.product.ProductConstants;
import com.liferay.faces.util.product.ProductMap;


/**
 * @author  Neil Griffin
 */
public class BridgePortletRequestFactoryLiferayImpl extends BridgePortletRequestFactory {

	// Private Constants
	private static final Product LIFERAY_PORTAL = ProductMap.getInstance().get(ProductConstants.LIFERAY_PORTAL);

	@Override
	public ActionRequest getActionRequest(ActionRequest actionRequest, ActionResponse actionResponse,
		PortletConfig portletConfig, BridgeConfig bridgeConfig) {

		PortalContext portalContext = actionRequest.getPortalContext();
		BridgePortalContext bridgePortalContext = new BridgePortalContextLiferayImpl(portalContext, actionRequest,
				LIFERAY_PORTAL);

		return new ActionRequestBridgeLiferayImpl(actionRequest, actionResponse.getNamespace(), portletConfig,
				bridgePortalContext);
	}

	@Override
	public EventRequest getEventRequest(EventRequest eventRequest, EventResponse eventResponse,
		PortletConfig portletConfig, BridgeConfig bridgeConfig) {

		PortalContext portalContext = eventRequest.getPortalContext();
		BridgePortalContext bridgePortalContext = new BridgePortalContextLiferayImpl(portalContext, eventRequest,
				LIFERAY_PORTAL);

		return new EventRequestBridgeLiferayImpl(eventRequest, eventResponse.getNamespace(), portletConfig,
				bridgePortalContext);
	}

	@Override
	public RenderRequest getRenderRequest(RenderRequest renderRequest, RenderResponse renderResponse,
		PortletConfig portletConfig, BridgeConfig bridgeConfig) {

		PortalContext portalContext = renderRequest.getPortalContext();
		BridgePortalContext bridgePortalContext = new BridgePortalContextLiferayImpl(portalContext, renderRequest,
				LIFERAY_PORTAL);

		return new RenderRequestBridgeLiferayImpl(renderRequest, renderResponse.getNamespace(), portletConfig,
				bridgePortalContext);
	}

	@Override
	public ResourceRequest getResourceRequest(ResourceRequest resourceRequest, ResourceResponse resourceResponse,
		PortletConfig portletConfig, BridgeConfig bridgeConfig) {

		PortalContext portalContext = resourceRequest.getPortalContext();

		BridgePortalContext bridgePortalContext = new BridgePortalContextLiferayImpl(portalContext, resourceRequest,
				LIFERAY_PORTAL);

		return new ResourceRequestBridgeLiferayImpl(resourceRequest, resourceResponse.getNamespace(), portletConfig,
				bridgePortalContext);
	}

	@Override
	public BridgePortletRequestFactory getWrapped() {

		// Since this is the factory instance provided by the bridge, it will never wrap another factory.
		return null;
	}
}
