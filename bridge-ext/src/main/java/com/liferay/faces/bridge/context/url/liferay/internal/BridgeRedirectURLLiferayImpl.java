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

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.BaseURL;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.faces.Bridge;
import javax.portlet.faces.BridgeUtil;

import com.liferay.faces.bridge.config.BridgeConfig;
import com.liferay.faces.bridge.context.BridgeContext;
import com.liferay.faces.bridge.context.url.BridgeURI;
import com.liferay.faces.bridge.context.url.BridgeURL;


/**
 * @author  Neil Griffin
 */
public class BridgeRedirectURLLiferayImpl extends BridgeURLLiferayBase {

	// Private Data Members
	private PortletResponse portletResponse;
	private String uri;
	private String viewId;
	private String viewIdRenderParameterName;
	private BridgeURL wrappedBridgeRedirectURL;

	public BridgeRedirectURLLiferayImpl(BridgeContext bridgeContext, BridgeURI bridgeURI,
		Map<String, List<String>> parameters, String viewId, BridgeURL bridgeRedirectURL) {

		super(bridgeContext, bridgeURI, viewId);
		this.uri = bridgeURI.toString();
		this.portletResponse = bridgeContext.getPortletResponse();
		this.viewId = viewId;

		BridgeConfig bridgeConfig = bridgeContext.getBridgeConfig();
		this.viewIdRenderParameterName = bridgeConfig.getViewIdRenderParameterName();
		this.wrappedBridgeRedirectURL = bridgeRedirectURL;

		if (parameters != null) {

			Map<String, String[]> parameterMap = getParameterMap();
			Set<Map.Entry<String, List<String>>> entrySet = parameters.entrySet();

			for (Map.Entry<String, List<String>> mapEntry : entrySet) {

				String key = mapEntry.getKey();
				String[] valueArray = null;
				List<String> valueList = mapEntry.getValue();

				if (valueList != null) {
					valueArray = valueList.toArray(new String[valueList.size()]);
				}

				parameterMap.put(key, valueArray);
			}
		}

	}

	@Override
	public PortletURL createRenderURL(String fromURL) throws MalformedURLException {

		LiferayPortletResponse liferayPortletResponse = new LiferayPortletResponse(portletResponse);

		return liferayPortletResponse.createRenderURL();
	}

	@Override
	public BaseURL toBaseURL() throws MalformedURLException {

		BaseURL baseURL;

		if (BridgeUtil.getPortletRequestPhase() == Bridge.PortletPhase.ACTION_PHASE) {

			baseURL = createRenderURL(uri);
			baseURL.setParameter(viewIdRenderParameterName, viewId);

			Map<String, String[]> parameterMap = getParameterMap();
			Set<Map.Entry<String, String[]>> entrySet = parameterMap.entrySet();

			for (Map.Entry<String, String[]> mapEntry : entrySet) {

				String parameterName = mapEntry.getKey();
				String[] parameterValues = mapEntry.getValue();

				baseURL.setParameter(parameterName, parameterValues);
			}
		}
		else {
			baseURL = wrappedBridgeRedirectURL.toBaseURL();
		}

		return baseURL;
	}
}
