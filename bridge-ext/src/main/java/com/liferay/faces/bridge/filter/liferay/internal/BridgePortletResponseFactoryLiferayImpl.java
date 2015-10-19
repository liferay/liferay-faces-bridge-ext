/**
 * Copyright (c) 2000-2015 Liferay, Inc. All rights reserved.
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

import javax.portlet.ActionResponse;
import javax.portlet.EventResponse;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceResponse;

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
	public ActionResponse getActionResponse(ActionResponse actionResponse) {

		ActionResponse wrappedActionResponse = getWrapped().getActionResponse(actionResponse);

		return new ActionResponseBridgeLiferayImpl(wrappedActionResponse);
	}

	@Override
	public EventResponse getEventResponse(EventResponse eventResponse) {

		EventResponse wrappedEventResponse = getWrapped().getEventResponse(eventResponse);

		return new EventResponseBridgeLiferayImpl(wrappedEventResponse);
	}

	@Override
	public RenderResponse getRenderResponse(RenderResponse renderResponse) {

		RenderResponse wrappedRenderResponse = getWrapped().getRenderResponse(renderResponse);

		return new RenderResponseBridgeLiferayImpl(wrappedRenderResponse);
	}

	@Override
	public ResourceResponse getResourceResponse(ResourceResponse resourceResponse) {

		ResourceResponse wrappedResourceResponse = getWrapped().getResourceResponse(resourceResponse);

		return new ResourceResponseBridgeLiferayImpl(wrappedResourceResponse);
	}

	@Override
	public BridgePortletResponseFactory getWrapped() {
		return wrappedBridgePortletResponseFactory;
	}
}
