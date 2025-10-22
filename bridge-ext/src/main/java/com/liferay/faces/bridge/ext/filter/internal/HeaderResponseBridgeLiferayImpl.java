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

import javax.portlet.HeaderRequest;
import javax.portlet.HeaderResponse;
import javax.portlet.MimeResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletURL;
import javax.portlet.RenderURL;
import javax.portlet.ResourceURL;
import javax.portlet.faces.BridgeFactoryFinder;
import javax.portlet.filter.HeaderResponseWrapper;
import javax.servlet.http.Cookie;

/**
 * @author  Neil Griffin
 */
public class HeaderResponseBridgeLiferayImpl extends HeaderResponseWrapper {

	// Private Data Members
	private Boolean friendlyURLMapperEnabled;
	private LiferayURLFactory liferayURLFactory;
	private String namespace;
	private String namespaceWSRP;
	private HeaderRequest headerRequest;

	public HeaderResponseBridgeLiferayImpl(PortletContext portletContext, HeaderRequest headerRequest,
		HeaderResponse headerResponse) {
		super(headerResponse);
		this.headerRequest = headerRequest;
		this.liferayURLFactory = (LiferayURLFactory) BridgeFactoryFinder.getFactory(portletContext,
				LiferayURLFactory.class);
	}

	@Override
	public void addDependency(String name, String scope, String version, String markup) {

		if (version == null) {

			// In order for Liferay to de-duplicate resources, it is necessary to specify a non-null version.
			// Otherwise, it will assign a random version in order to force uniqueness.
			version = "1.0.0";
		}

		super.addDependency(name, scope, version, markup);
	}

	@Override
	public void addProperty(Cookie cookie) {
		CookiesManagerUtil.addCookie(CookiesConstants.CONSENT_TYPE_NECESSARY, cookie,
			PortalUtil.getHttpServletRequest(headerRequest), PortalUtil.getHttpServletResponse(super.getResponse()));
	}

	@Override
	public PortletURL createActionURL() throws IllegalStateException {
		return liferayURLFactory.getLiferayActionURL(headerRequest, getResponse(), isFriendlyURLMapperEnabled());
	}

	@Override
	public PortletURL createRenderURL() throws IllegalStateException {
		return liferayURLFactory.getLiferayRenderURL(headerRequest, getResponse(), isFriendlyURLMapperEnabled());
	}

	@Override
	public RenderURL createRenderURL(MimeResponse.Copy copy) throws IllegalStateException {
		return liferayURLFactory.getLiferayRenderURL(headerRequest, getResponse(), isFriendlyURLMapperEnabled(), copy);
	}

	@Override
	public ResourceURL createResourceURL() throws IllegalStateException {
		return liferayURLFactory.getLiferayResourceURL(headerRequest, getResponse(), isFriendlyURLMapperEnabled());
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
			namespaceWSRP = LiferayPortalUtil.getPortletId(headerRequest);
		}

		return namespaceWSRP;
	}

	protected boolean isFriendlyURLMapperEnabled() {

		if (friendlyURLMapperEnabled == null) {
			PortletConfig portletConfig = (PortletConfig) headerRequest.getAttribute(PortletConfig.class.getName());
			LiferayPortletRequest liferayPortletRequest = new LiferayPortletRequest(headerRequest,
					getResponse().getNamespace(), portletConfig);
			friendlyURLMapperEnabled = (liferayPortletRequest.getPortlet().getFriendlyURLMapperInstance() != null);
		}

		return friendlyURLMapperEnabled;
	}
}
