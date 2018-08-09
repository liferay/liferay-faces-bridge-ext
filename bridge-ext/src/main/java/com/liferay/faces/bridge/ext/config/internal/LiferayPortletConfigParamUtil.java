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
package com.liferay.faces.bridge.ext.config.internal;

import javax.faces.context.ExternalContext;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;

import com.liferay.faces.util.helper.BooleanHelper;
import com.liferay.faces.util.helper.IntegerHelper;


/**
 * @author  Neil Griffin
 */
public class LiferayPortletConfigParamUtil {

	public static boolean getBooleanValue(PortletConfig portletConfig, String name, String alternateName,
		boolean defaultBooleanValue) {

		boolean booleanValue = defaultBooleanValue;

		String portletName = portletConfig.getPortletName();

		if (portletName == null) {
			String configuredValue = getConfiguredValue(portletConfig, name, alternateName);

			if (configuredValue != null) {
				booleanValue = BooleanHelper.isTrueToken(configuredValue);
			}
		}
		else {
			String configuredValue = getConfiguredValue(portletConfig, name, alternateName);

			if (configuredValue != null) {
				booleanValue = BooleanHelper.isTrueToken(configuredValue);
			}
		}

		return booleanValue;
	}

	public static boolean getBooleanValue(ExternalContext externalContext, String name, String alternateName,
		boolean defaultBooleanValue) {

		boolean booleanValue = defaultBooleanValue;
		String configuredValue = externalContext.getInitParameter(name);

		if ((configuredValue == null) && (alternateName != null)) {
			configuredValue = externalContext.getInitParameter(alternateName);
		}

		if (configuredValue != null) {
			booleanValue = BooleanHelper.toBoolean(configuredValue);
		}

		return booleanValue;
	}

	public static String getConfiguredValue(PortletConfig portletConfig, String name, String alternateName) {

		String configuredValue = portletConfig.getInitParameter(name);

		PortletContext portletContext = null;

		if (configuredValue == null) {
			portletContext = portletConfig.getPortletContext();
			configuredValue = portletContext.getInitParameter(name);
		}

		if ((configuredValue == null) && (alternateName != null)) {
			configuredValue = portletConfig.getInitParameter(alternateName);

			if (configuredValue == null) {
				configuredValue = portletContext.getInitParameter(alternateName);
			}
		}

		return configuredValue;
	}

	public static String getConfiguredValue(ExternalContext externalContext, String name, String alternateName,
		String defaultStringValue) {

		String configuredValue = externalContext.getInitParameter(name);

		if ((configuredValue == null) && (alternateName != null)) {
			configuredValue = externalContext.getInitParameter(alternateName);
		}

		if (configuredValue == null) {
			configuredValue = defaultStringValue;
		}

		return configuredValue;
	}

	public static int getIntegerValue(ExternalContext externalContext, String name, String alternateName,
		int defaultIntegerValue) {

		int integerValue = defaultIntegerValue;
		String configuredValue = externalContext.getInitParameter(name);

		if ((configuredValue == null) && (alternateName != null)) {
			configuredValue = externalContext.getInitParameter(alternateName);
		}

		if (configuredValue != null) {
			integerValue = IntegerHelper.toInteger(configuredValue);
		}

		return integerValue;
	}
}
