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

import java.util.Map;

import javax.portlet.BaseURL;


/**
 * @author  Neil Griffin
 */
public final class LiferayBaseURLUtil {

	private LiferayBaseURLUtil() {
		throw new AssertionError();
	}

	public static void removeParameter(BaseURL baseURL, String name) {

		// According to a clarification in the Portlet 3.0 JavaDoc for BaseURL#setProperty(String,String), setting
		// the parameter to null will remove it. However, Liferay Portal 6.2 throws an IllegalArgumentException when
		// attempting to set a value of null. The only alternative is to replace the entire parameter map after
		// having removed the parameter with the specified name.
		Map<String, String[]> parameterMap = baseURL.getParameterMap();

		if (parameterMap.containsKey(name)) {
			parameterMap.remove(name);
			baseURL.setParameters(parameterMap);
		}
	}
}
