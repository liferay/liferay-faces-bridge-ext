/**
 * Copyright (c) 2000-2022 Liferay, Inc. All rights reserved.
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

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletRequest;
import javax.portlet.filter.EventResponseWrapper;
import javax.servlet.http.Cookie;

/**
 * @author  Neil Griffin
 */
public class EventResponseBridgeLiferayImpl extends EventResponseWrapper {

	// Private Data Members
	private EventRequest eventRequest;
	private String namespace;
	private String namespaceWSRP;

	public EventResponseBridgeLiferayImpl(EventRequest eventRequest, EventResponse eventResponse) {
		super(eventResponse);
		this.eventRequest = eventRequest;
	}

	@Override
	public void addProperty(Cookie cookie) {
		CookiesManagerUtil.addCookie(CookiesConstants.CONSENT_TYPE_NECESSARY, cookie,
			PortalUtil.getHttpServletRequest(eventRequest), PortalUtil.getHttpServletResponse(super.getResponse()));
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

			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			PortletRequest portletRequest = (PortletRequest) externalContext.getRequest();
			namespaceWSRP = LiferayPortalUtil.getPortletId(portletRequest);
		}

		return namespaceWSRP;
	}
}
