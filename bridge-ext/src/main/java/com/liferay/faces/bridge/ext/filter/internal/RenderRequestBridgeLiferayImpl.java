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

import java.util.Enumeration;

import javax.portlet.PortalContext;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletMode;
import javax.portlet.RenderRequest;
import javax.portlet.WindowState;
import javax.portlet.filter.RenderRequestWrapper;

import com.liferay.faces.bridge.context.BridgePortalContext;
import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;

import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;


/**
 * @author  Neil Griffin
 */
public class RenderRequestBridgeLiferayImpl extends RenderRequestWrapper {

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(RenderRequestBridgeLiferayImpl.class);

	// Private Data Members
	private BridgePortalContext bridgePortalContext;
	private LiferayPortletRequest liferayPortletRequest;

	public RenderRequestBridgeLiferayImpl(RenderRequest renderRequest, String responseNamespace,
		PortletConfig portletConfig, BridgePortalContext bridgePortalContext) {
		super(renderRequest);
		this.liferayPortletRequest = new LiferayPortletRequest(renderRequest, responseNamespace, portletConfig);
		this.bridgePortalContext = bridgePortalContext;

		// Hack: Need to save information that's only available at RenderRequest time in order to have
		// LiferayURLGeneratorBaseImpl be able to create ResourceURLs properly during the RESOURCE_PHASE.
		String p_p_col_id = responseNamespace.concat("p_p_col_id");
		String p_p_col_pos = responseNamespace.concat("p_p_col_pos");
		String p_p_col_count = responseNamespace.concat("p_p_col_count");
		String p_p_mode = responseNamespace.concat("p_p_mode");
		String p_p_state = responseNamespace.concat("p_p_state");

		try {
			PortletContext portletContext = renderRequest.getPortletSession().getPortletContext();

			// Get the p_p_col_id and save it.
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
			PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();
			portletContext.setAttribute(p_p_col_id, portletDisplay.getColumnId());

			// Get the p_p_col_pos and save it.
			portletContext.setAttribute(p_p_col_pos, Integer.toString(portletDisplay.getColumnPos()));

			// Get the p_p_col_count and save it.
			portletContext.setAttribute(p_p_col_count, Integer.toString(portletDisplay.getColumnCount()));

			// Get the p_p_mode and save it.
			PortletMode portletMode = renderRequest.getPortletMode();

			if (portletMode != null) {
				portletContext.setAttribute(p_p_mode, portletMode.toString());
			}

			// Get the p_p_state and save it.
			WindowState windowState = renderRequest.getWindowState();

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
		return bridgePortalContext;
	}

	@Override
	public Enumeration<String> getProperties(String name) {
		return liferayPortletRequest.getProperties(name);
	}

	@Override
	public Enumeration<String> getPropertyNames() {
		return liferayPortletRequest.getPropertyNames();
	}
}
