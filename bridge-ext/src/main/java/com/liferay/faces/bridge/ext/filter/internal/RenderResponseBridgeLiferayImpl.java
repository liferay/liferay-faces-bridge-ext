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

import javax.portlet.PortletConfig;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceURL;
import javax.portlet.filter.RenderResponseWrapper;

import com.liferay.faces.bridge.BridgeFactoryFinder;
import com.liferay.faces.bridge.ext.filter.LiferayURLFactory;


/**
 * @author  Neil Griffin
 */
public class RenderResponseBridgeLiferayImpl extends RenderResponseWrapper {

	// Private Data Members
	private Boolean friendlyURLMapperEnabled;
	private LiferayURLFactory liferayURLFactory;
	private String namespace;
	private String namespaceWSRP;
	private RenderRequest renderRequest;

	public RenderResponseBridgeLiferayImpl(RenderRequest renderRequest, RenderResponse renderResponse) {
		super(renderResponse);
		this.renderRequest = renderRequest;
		this.liferayURLFactory = (LiferayURLFactory) BridgeFactoryFinder.getFactory(LiferayURLFactory.class);
	}

	@Override
	public PortletURL createActionURL() throws IllegalStateException {
		return liferayURLFactory.getLiferayActionURL(renderRequest, getResponse());
	}

	@Override
	public PortletURL createRenderURL() throws IllegalStateException {
		return liferayURLFactory.getLiferayRenderURL(renderRequest, getResponse(), isFriendlyURLMapperEnabled());
	}

	@Override
	public ResourceURL createResourceURL() throws IllegalStateException {
		return liferayURLFactory.getLiferayResourceURL(renderRequest, getResponse());
	}

	protected boolean isFriendlyURLMapperEnabled() {

		if (friendlyURLMapperEnabled == null) {
			PortletConfig portletConfig = (PortletConfig) renderRequest.getAttribute(PortletConfig.class.getName());
			LiferayPortletRequest liferayPortletRequest = new LiferayPortletRequest(renderRequest,
					getResponse().getNamespace(), portletConfig);
			friendlyURLMapperEnabled = (liferayPortletRequest.getPortlet().getFriendlyURLMapperInstance() != null);
		}

		return friendlyURLMapperEnabled;
	}

	@Override
	public String getNamespace() {

		if (namespace == null) {

			namespace = super.getNamespace();

			if (namespace.startsWith("wsrp_rewrite")) {

				namespace = getNamespaceWSRP();
			}
		}

		return namespace;
	}

	protected String getNamespaceWSRP() {

		if (namespaceWSRP == null) {
			namespaceWSRP = LiferayPortalUtil.getPortletId(renderRequest);
		}

		return namespaceWSRP;
	}
}
