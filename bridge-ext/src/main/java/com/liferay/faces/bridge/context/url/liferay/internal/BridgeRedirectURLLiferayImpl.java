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
package com.liferay.faces.bridge.context.url.liferay.internal;

import java.util.Map;
import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.BaseURL;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.faces.Bridge;
import javax.portlet.faces.BridgeUtil;
import javax.portlet.filter.PortletResponseWrapper;

import com.liferay.faces.bridge.config.BridgeConfig;
import com.liferay.faces.bridge.context.url.BridgeURL;
import com.liferay.faces.bridge.context.url.BridgeURLWrapper;

import com.liferay.portal.kernel.portlet.LiferayPortletResponse;


/**
 * @author  Neil Griffin
 */
public class BridgeRedirectURLLiferayImpl extends BridgeURLWrapper {

	// Private Data Members
	private PortletResponse portletResponse;
	private String viewId;
	private String viewIdRenderParameterName;
	private BridgeURL wrappedBridgeRedirectURL;

	public BridgeRedirectURLLiferayImpl(BridgeURL wrappedBridgeRedirectURL, FacesContext facesContext, String viewId) {

		this.wrappedBridgeRedirectURL = wrappedBridgeRedirectURL;

		ExternalContext externalContext = facesContext.getExternalContext();
		this.portletResponse = (PortletResponse) externalContext.getResponse();

		PortletRequest portletRequest = (PortletRequest) externalContext.getRequest();
		BridgeConfig bridgeConfig = (BridgeConfig) portletRequest.getAttribute(BridgeConfig.class.getName());
		this.viewIdRenderParameterName = bridgeConfig.getViewIdRenderParameterName();
		this.viewId = viewId;
	}

	public BaseURL createRenderURL(PortletResponse portletResponse) {

		BaseURL renderURL = null;

		if (portletResponse != null) {

			while ((portletResponse instanceof PortletResponseWrapper) &&
					!(portletResponse instanceof LiferayPortletResponse)) {

				PortletResponseWrapper portletResponseWrapper = (PortletResponseWrapper) portletResponse;
				portletResponse = portletResponseWrapper.getResponse();
			}

			if (portletResponse instanceof LiferayPortletResponse) {

				LiferayPortletResponse liferayPortletResponse = (LiferayPortletResponse) portletResponse;
				renderURL = liferayPortletResponse.createRenderURL();
			}
		}

		return renderURL;
	}

	@Override
	public String toString() {

		String string = null;

		if (BridgeUtil.getPortletRequestPhase() == Bridge.PortletPhase.ACTION_PHASE) {

			BaseURL baseURL = createRenderURL(portletResponse);

			if (baseURL != null) {

				baseURL.setParameter(viewIdRenderParameterName, viewId);

				Map<String, String[]> parameterMap = getParameterMap();
				Set<Map.Entry<String, String[]>> entrySet = parameterMap.entrySet();

				for (Map.Entry<String, String[]> mapEntry : entrySet) {

					String parameterName = mapEntry.getKey();
					String[] parameterValues = mapEntry.getValue();

					baseURL.setParameter(parameterName, parameterValues);
				}

				string = baseURL.toString();
			}
		}

		if (string == null) {
			string = super.toString();
		}

		return string;
	}

	@Override
	public BridgeURL getWrapped() {
		return wrappedBridgeRedirectURL;
	}
}
