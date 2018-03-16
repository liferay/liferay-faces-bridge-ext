/**
 * Copyright (c) 2000-2018 Liferay, Inc. All rights reserved.
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.faces.Bridge;
import javax.portlet.faces.BridgeUtil;

import com.liferay.faces.bridge.BridgeConfig;
import com.liferay.faces.bridge.BridgeURL;
import com.liferay.faces.bridge.BridgeURLWrapper;
import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;


/**
 * @author  Neil Griffin
 */
public class BridgeRedirectURLLiferayImpl extends BridgeURLWrapper {

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(BridgeRedirectURLLiferayImpl.class);

	// Private Final Data Members
	private final String encoding;

	// Private Data Members
	private BridgeURL wrappedBridgeRedirectURL;

	public BridgeRedirectURLLiferayImpl(BridgeURL bridgeURL, String encoding) {

		this.wrappedBridgeRedirectURL = bridgeURL;
		this.encoding = encoding;
	}

	public static String decodeParameterName(String name, String encoding) throws UnsupportedEncodingException {

		String decodedName = name;

		if (name != null) {

			decodedName = URLDecoder.decode(name, encoding);
			decodedName = decodedName.replace("+", " ");
		}

		return decodedName;
	}

	public static String[] decodeParameterValues(String[] values, String encoding) throws UnsupportedEncodingException {

		String[] encodedValues = values;

		if (values != null) {

			encodedValues = new String[values.length];

			for (int i = 0; i < values.length; i++) {
				encodedValues[i] = decodeParameterName(values[i], encoding);
			}
		}

		return encodedValues;
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

				try {

					parameterName = decodeParameterName(parameterName, encoding);
					parameterValues = decodeParameterValues(parameterValues, encoding);
					renderURL.setParameter(parameterName, parameterValues);
				}
				catch (UnsupportedEncodingException e) {

					logger.error(
						"Unable to decode and append parameter name=\"{0}\" and value=\"{1}\" with encoding \"{2}\".",
						parameterName, parameterValues, encoding);
					logger.error(e);
				}
			}

			return renderURL.toString();
		}
		else {
			return wrappedBridgeRedirectURL.toString();
		}
	}
}
