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
package com.liferay.faces.bridge.ext.filter.internal;

import javax.portlet.PortletRequest;

import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;

import com.liferay.portal.kernel.util.PortalUtil;


/**
 * This class provides access to the com.liferay.portal.util.PortalUtil static utility class via reflection in order to
 * avoid a compile-time dependency.
 */
public class LiferayPortalUtil {

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(LiferayPortalUtil.class);

	public static String getPortletId(PortletRequest portletRequest) {
		String portletId = null;

		try {
			portletId = PortalUtil.getPortletId(portletRequest);

			if (portletRequest.getParameter("wsrp") != null) {

				// For some reason, when running as a WSRP producer, the underscores are missing from the beginning
				// and end...
				portletId = "_" + portletId + "_";
			}
		}
		catch (Exception e) {
			logger.error(e);
		}

		return portletId;
	}

}
