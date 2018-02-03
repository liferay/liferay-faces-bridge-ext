/**
 * Copyright (c) 2000-2019 Liferay, Inc. All rights reserved.
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
package com.liferay.faces.bridge.ext.client.internal;

import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

import com.liferay.faces.util.client.BrowserSniffer;
import com.liferay.faces.util.client.BrowserSnifferFactory;

import com.liferay.portal.kernel.util.PortalUtil;


/**
 * @author  Kyle Stiemann
 */
public class BrowserSnifferFactoryLiferayImpl extends BrowserSnifferFactory implements Serializable {

	// serialVersionUID
	private static final long serialVersionUID = 1618487876717568315L;

	// Private Final Data Members
	private final BrowserSnifferFactory wrappedBrowserSnifferFactory;

	public BrowserSnifferFactoryLiferayImpl(BrowserSnifferFactory browserSnifferFactory) {
		this.wrappedBrowserSnifferFactory = browserSnifferFactory;
	}

	@Override
	public BrowserSniffer getBrowserSniffer(ExternalContext externalContext) {

		PortletRequest portletRequest = (PortletRequest) externalContext.getRequest();
		HttpServletRequest httpServletRequest = PortalUtil.getHttpServletRequest(portletRequest);

		// Calling ExternalContext.setRequest(httpServletRequest) adds extra overhead because all of the
		// underlying maps have to get re-created. Instead, create a simple ExternalContextWrapper.
		ExternalContext externalContextBrowserSnifferImpl = new ExternalContextBrowserSnifferImpl(externalContext,
				httpServletRequest);

		BrowserSnifferFactory firstNonBridgeBrowserSnifferFactory = getFirstNonBridgeBrowserSnifferFactory();

		return firstNonBridgeBrowserSnifferFactory.getBrowserSniffer(externalContextBrowserSnifferImpl);
	}

	@Override
	public BrowserSnifferFactory getWrapped() {
		return wrappedBrowserSnifferFactory;
	}

	private BrowserSnifferFactory getFirstNonBridgeBrowserSnifferFactory() {

		BrowserSnifferFactory firstNonBridgeBrowserSnifferFactory = wrappedBrowserSnifferFactory;

		while (firstNonBridgeBrowserSnifferFactory.getClass().getName().startsWith("com.liferay.faces.bridge")) {
			firstNonBridgeBrowserSnifferFactory = firstNonBridgeBrowserSnifferFactory.getWrapped();
		}

		return firstNonBridgeBrowserSnifferFactory;
	}
}
