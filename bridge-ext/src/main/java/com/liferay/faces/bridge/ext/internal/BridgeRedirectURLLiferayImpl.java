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
package com.liferay.faces.bridge.ext.internal;

import java.util.Map;
import java.util.Set;

import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.portlet.PortletResponse;
import jakarta.portlet.PortletURL;
import jakarta.portlet.faces.Bridge;
import jakarta.portlet.faces.BridgeConfig;
import jakarta.portlet.faces.BridgeURL;
import jakarta.portlet.faces.BridgeURLWrapper;
import jakarta.portlet.faces.BridgeUtil;


/**
 * @author  Neil Griffin
 */
public class BridgeRedirectURLLiferayImpl extends BridgeURLWrapper {

	// Private Data Members
	private BridgeURL wrappedBridgeRedirectURL;

	public BridgeRedirectURLLiferayImpl(BridgeURL bridgeURL) {
		this.wrappedBridgeRedirectURL = bridgeURL;
	}

	@Override
	public BridgeURL getWrapped() {
		return wrappedBridgeRedirectURL;
	}

	@Override
	public String toString() {

		FacesContext facesContext = FacesContext.getCurrentInstance();

		if (BridgeUtil.getPortletRequestPhase(facesContext) == Bridge.PortletPhase.ACTION_PHASE) {

			ExternalContext externalContext = facesContext.getExternalContext();
			Map<String, Object> requestMap = externalContext.getRequestMap();
			BridgeConfig bridgeConfig = (BridgeConfig) requestMap.get(BridgeConfig.class.getName());
			PortletResponse portletResponse = (PortletResponse) externalContext.getResponse();
			LiferayPortletResponse liferayPortletResponse = new LiferayPortletResponse(portletResponse);
			PortletURL renderURL = liferayPortletResponse.createRenderURL();
			renderURL.setParameter(bridgeConfig.getViewIdRenderParameterName(), wrappedBridgeRedirectURL.getViewId());

			Map<String, String[]> parameterMap = getParameterMap();
			Set<Map.Entry<String, String[]>> entrySet = parameterMap.entrySet();

			for (Map.Entry<String, String[]> mapEntry : entrySet) {

				String parameterName = mapEntry.getKey();
				String[] parameterValues = mapEntry.getValue();
				renderURL.setParameter(parameterName, parameterValues);
			}

			return renderURL.toString();
		}
		else {
			return wrappedBridgeRedirectURL.toString();
		}
	}
}
