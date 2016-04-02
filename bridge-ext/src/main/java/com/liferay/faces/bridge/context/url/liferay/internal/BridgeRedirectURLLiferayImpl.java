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

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.BaseURL;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.faces.Bridge;
import javax.portlet.faces.BridgeUtil;

import com.liferay.faces.bridge.config.BridgeConfig;
import com.liferay.faces.bridge.config.liferay.internal.LiferayBridgeConfigAttributeMap;
import com.liferay.faces.bridge.context.url.BridgeURI;
import com.liferay.faces.bridge.context.url.BridgeURL;
import com.liferay.faces.util.config.ConfiguredServletMapping;
import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;


/**
 * @author  Neil Griffin
 */
public class BridgeRedirectURLLiferayImpl extends BridgeURLLiferayBase {

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(BridgeRedirectURLLiferayImpl.class);

	// Private Data Members
	private List<ConfiguredServletMapping> configuredFacesServletMappings;
	private BridgeURL wrappedBridgeRedirectURL;

	public BridgeRedirectURLLiferayImpl(BridgeURI bridgeURI, String contextPath, String namespace, String viewId,
		String viewIdRenderParameterName, String viewIdResourceParameterName, BridgeConfig bridgeConfig,
		Map<String, List<String>> parameters, BridgeURL bridgeRedirectURL) {

		super(bridgeURI, contextPath, namespace, viewId, viewIdRenderParameterName, viewIdResourceParameterName);

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

		this.configuredFacesServletMappings = (List<ConfiguredServletMapping>) bridgeConfig.getAttributes().get(
				LiferayBridgeConfigAttributeMap.CONFIGURED_FACES_SERVLET_MAPPINGS);
	}

	@Override
	public PortletURL createRenderURL(FacesContext facesContext, String fromURL) throws MalformedURLException {

		ExternalContext externalContext = facesContext.getExternalContext();
		PortletResponse portletResponse = (PortletResponse) externalContext.getResponse();
		LiferayPortletResponse liferayPortletResponse = new LiferayPortletResponse(portletResponse);

		return liferayPortletResponse.createRenderURL();
	}

	@Override
	public BaseURL toBaseURL() throws MalformedURLException {

		BaseURL baseURL;

		if (BridgeUtil.getPortletRequestPhase() == Bridge.PortletPhase.ACTION_PHASE) {

			FacesContext facesContext = FacesContext.getCurrentInstance();
			baseURL = createRenderURL(facesContext, getBridgeURI().toString());
			baseURL.setParameter(getViewIdRenderParameterName(), getViewId());

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

	@Override
	protected boolean isMappedToFacesServlet(String viewPath) {

		// Try to determine the viewId by examining the servlet-mapping entries for the Faces Servlet.
		// For each servlet-mapping:
		for (ConfiguredServletMapping configuredFacesServletMapping : configuredFacesServletMappings) {

			// If the current servlet-mapping matches the viewPath, then
			logger.debug("Attempting to determine the facesViewId from {0}=[{1}]", Bridge.VIEW_PATH, viewPath);

			if (configuredFacesServletMapping.isMatch(viewPath)) {
				return true;
			}
		}

		return false;
	}
}
