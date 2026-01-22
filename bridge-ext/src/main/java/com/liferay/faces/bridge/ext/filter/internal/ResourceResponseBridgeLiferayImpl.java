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

import com.liferay.portal.kernel.cookies.CookiesManagerUtil;
import com.liferay.portal.kernel.cookies.constants.CookiesConstants;
import com.liferay.portal.kernel.util.PortalUtil;

import jakarta.portlet.PortletConfig;
import jakarta.portlet.PortletContext;
import jakarta.portlet.PortletURL;
import jakarta.portlet.ResourceRequest;
import jakarta.portlet.ResourceResponse;
import jakarta.portlet.ResourceURL;
import jakarta.portlet.faces.BridgeFactoryFinder;
import jakarta.portlet.filter.ResourceResponseWrapper;
import jakarta.servlet.http.Cookie;

/**
 * @author  Neil Griffin
 */
public class ResourceResponseBridgeLiferayImpl extends ResourceResponseWrapper {

	// Private Data Members
	private Boolean friendlyURLMapperEnabled;
	private LiferayURLFactory liferayURLFactory;
	private String namespace;
	private String namespaceWSRP;
	private ResourceRequest resourceRequest;

	public ResourceResponseBridgeLiferayImpl(PortletContext portletContext, ResourceRequest resourceRequest,
		ResourceResponse resourceResponse) {
		super(resourceResponse);
		this.resourceRequest = resourceRequest;
		this.liferayURLFactory = (LiferayURLFactory) BridgeFactoryFinder.getFactory(portletContext,
				LiferayURLFactory.class);
	}

	@Override
	public void addProperty(Cookie cookie) {
		CookiesManagerUtil.addCookie(CookiesConstants.CONSENT_TYPE_NECESSARY, cookie,
			PortalUtil.getHttpServletRequest(resourceRequest), PortalUtil.getHttpServletResponse(super.getResponse()));
	}

	@Override
	public PortletURL createActionURL() throws IllegalStateException {
		return liferayURLFactory.getLiferayActionURL(resourceRequest, getResponse(), isFriendlyURLMapperEnabled());
	}

	@Override
	public PortletURL createRenderURL() throws IllegalStateException {
		return liferayURLFactory.getLiferayRenderURL(resourceRequest, getResponse(), isFriendlyURLMapperEnabled());
	}

	@Override
	public ResourceURL createResourceURL() throws IllegalStateException {
		return liferayURLFactory.getLiferayResourceURL(resourceRequest, getResponse(), isFriendlyURLMapperEnabled());
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
			namespaceWSRP = LiferayPortalUtil.getPortletId(resourceRequest);
		}

		return namespaceWSRP;
	}

	protected boolean isFriendlyURLMapperEnabled() {

		if (friendlyURLMapperEnabled == null) {
			PortletConfig portletConfig = (PortletConfig) resourceRequest.getAttribute(PortletConfig.class.getName());
			LiferayPortletRequest liferayResourceRequest = new LiferayPortletRequest(resourceRequest,
					getResponse().getNamespace(), portletConfig);
			friendlyURLMapperEnabled = (liferayResourceRequest.getPortlet().getFriendlyURLMapperInstance() != null);
		}

		return friendlyURLMapperEnabled;
	}
}
