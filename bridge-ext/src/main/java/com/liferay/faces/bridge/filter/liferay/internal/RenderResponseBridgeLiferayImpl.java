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

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceURL;
import javax.portlet.filter.RenderResponseWrapper;

import com.liferay.faces.bridge.BridgeFactoryFinder;
import com.liferay.faces.bridge.context.BridgeContext;
import com.liferay.faces.bridge.filter.liferay.LiferayURLFactory;


/**
 * @author  Neil Griffin
 */
public class RenderResponseBridgeLiferayImpl extends RenderResponseWrapper {

	// Private Data Members
	private Boolean friendlyURLMapperEnabled;
	private LiferayURLFactory liferayURLFactory;
	private String namespace;
	private String responseNamespaceWSRP;

	public RenderResponseBridgeLiferayImpl(RenderResponse renderResponse) {
		super(renderResponse);
		this.liferayURLFactory = (LiferayURLFactory) BridgeFactoryFinder.getFactory(LiferayURLFactory.class);
	}

	@Override
	public PortletURL createActionURL() throws IllegalStateException {

		BridgeContext bridgeContext = BridgeContext.getCurrentInstance();

		return liferayURLFactory.getLiferayActionURL(bridgeContext, getResponse(), super.getNamespace());
	}

	@Override
	public PortletURL createRenderURL() throws IllegalStateException {

		BridgeContext bridgeContext = BridgeContext.getCurrentInstance();

		return liferayURLFactory.getLiferayRenderURL(bridgeContext, getResponse(), super.getNamespace(),
				isFriendlyURLMapperEnabled(bridgeContext));
	}

	@Override
	public ResourceURL createResourceURL() throws IllegalStateException {

		BridgeContext bridgeContext = BridgeContext.getCurrentInstance();

		return liferayURLFactory.getLiferayResourceURL(bridgeContext, getResponse(), super.getNamespace());
	}

	protected boolean isFriendlyURLMapperEnabled(BridgeContext bridgeContext) {

		if (friendlyURLMapperEnabled == null) {
			PortletRequest portletRequest = bridgeContext.getPortletRequest();
			LiferayPortletRequest liferayPortletRequest = new LiferayPortletRequest(portletRequest);
			friendlyURLMapperEnabled = (liferayPortletRequest.getPortlet().getFriendlyURLMapperInstance() != null);
		}

		return friendlyURLMapperEnabled;
	}

	@Override
	public String getNamespace() {

		if (namespace == null) {

			namespace = super.getNamespace();

			if (namespace.startsWith("wsrp_rewrite")) {

				BridgeContext bridgeContext = BridgeContext.getCurrentInstance();
				namespace = getNamespaceWSRP(bridgeContext);
			}
		}

		return namespace;
	}

	protected String getNamespaceWSRP(BridgeContext bridgeContext) {

		if (responseNamespaceWSRP == null) {

			PortletRequest portletRequest = bridgeContext.getPortletRequest();
			responseNamespaceWSRP = LiferayPortalUtil.getPortletId(portletRequest);
		}

		return responseNamespaceWSRP;
	}
}
