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

import java.util.Enumeration;

import javax.portlet.HeaderRequest;
import javax.portlet.PortalContext;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.portlet.filter.HeaderRequestWrapper;

import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.service.permission.PortletPermissionUtil;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;


/**
 * @author  Neil Griffin
 */
public class HeaderRequestBridgeLiferayImpl extends HeaderRequestWrapper {

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(HeaderRequestBridgeLiferayImpl.class);

	// Private Data Members
	private PortalContext portalContext;
	private LiferayPortletRequest liferayPortletRequest;

	public HeaderRequestBridgeLiferayImpl(HeaderRequest headerRequest, String responseNamespace,
		PortletConfig portletConfig, PortalContext portalContext) {
		super(headerRequest);
		this.liferayPortletRequest = new LiferayPortletRequest(headerRequest, responseNamespace, portletConfig);
		this.portalContext = portalContext;

		// Hack: Need to save information that's only available at RenderRequest time in order to have
		// LiferayURLGeneratorBaseImpl be able to create ResourceURLs properly during the RESOURCE_PHASE.
		String p_p_col_id = responseNamespace.concat("p_p_col_id");
		String p_p_col_pos = responseNamespace.concat("p_p_col_pos");
		String p_p_col_count = responseNamespace.concat("p_p_col_count");
		String p_p_mode = responseNamespace.concat("p_p_mode");
		String p_p_state = responseNamespace.concat("p_p_state");

		try {
			PortletContext portletContext = headerRequest.getPortletSession().getPortletContext();

			// Get the p_p_col_id and save it.
			ThemeDisplay themeDisplay = (ThemeDisplay) headerRequest.getAttribute(WebKeys.THEME_DISPLAY);
			PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();
			portletContext.setAttribute(p_p_col_id, portletDisplay.getColumnId());

			// Get the p_p_col_pos and save it.
			portletContext.setAttribute(p_p_col_pos, Integer.toString(portletDisplay.getColumnPos()));

			// Get the p_p_col_count and save it.
			portletContext.setAttribute(p_p_col_count, Integer.toString(portletDisplay.getColumnCount()));

			// Get the p_p_mode and save it.
			PortletMode portletMode = headerRequest.getPortletMode();

			if (portletMode != null) {
				portletContext.setAttribute(p_p_mode, portletMode.toString());
			}

			// Get the p_p_state and save it.
			WindowState windowState = headerRequest.getWindowState();

			if (windowState != null) {
				portletContext.setAttribute(p_p_state, windowState.toString());
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public Object getAttribute(String name) {
		return liferayPortletRequest.getAttribute(name);
	}

	@Override
	public PortalContext getPortalContext() {
		return portalContext;
	}

	@Override
	public Enumeration<String> getProperties(String name) {
		return liferayPortletRequest.getProperties(name);
	}

	@Override
	public Enumeration<String> getPropertyNames() {
		return liferayPortletRequest.getPropertyNames();
	}

	@Override
	public boolean isPortletModeAllowed(PortletMode portletMode) {
		boolean portletModeAllowed = super.isPortletModeAllowed(portletMode);

		if (portletModeAllowed && PortletMode.EDIT.equals(portletMode)) {
			ThemeDisplay themeDisplay = (ThemeDisplay) super.getAttribute(WebKeys.THEME_DISPLAY);

			try {
				Portlet portlet = (Portlet) getAttribute(WebKeys.RENDER_PORTLET);

				if (portlet == null) {
					com.liferay.portal.kernel.portlet.LiferayPortletRequest liferayPortletRequest = PortalUtil
						.getLiferayPortletRequest(getRequest());

					if (liferayPortletRequest != null) {
						portlet = liferayPortletRequest.getPortlet();
					}
				}

				if (portlet == null) {
					logger.warn("Unable to determine com.liferay.portal.kernel.model.Portlet from PortletRequest");
					portletModeAllowed = !isUserInRole(RoleConstants.GUEST);
				}
				else {
					portletModeAllowed = PortletPermissionUtil.hasAccessPermission(themeDisplay.getPermissionChecker(),
							themeDisplay.getScopeGroupId(), themeDisplay.getLayout(), portlet, portletMode);
				}
			}
			catch (PortalException portalException) {
				logger.error(portalException);
				portletModeAllowed = false;
			}
		}

		return portletModeAllowed;
	}
}
