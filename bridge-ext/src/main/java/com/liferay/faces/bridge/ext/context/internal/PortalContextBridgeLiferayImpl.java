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
package com.liferay.faces.bridge.ext.context.internal;

import java.util.Enumeration;

import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.WindowState;
import javax.portlet.faces.Bridge;

import com.liferay.faces.bridge.context.BridgePortalContext;
import com.liferay.faces.util.helper.BooleanHelper;
import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.service.PortletLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;


/**
 * @author  Neil Griffin
 */
public class PortalContextBridgeLiferayImpl extends PortalContextBridgeLiferayCompatImpl {

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(PortalContextBridgeLiferayImpl.class);

	// Private Final Data Members
	private final String addToHeadSupport;
	private final String namespacedParametersRequired;
	private final PortalContext wrappedPortalContext;

	public PortalContextBridgeLiferayImpl(PortletRequest portletRequest) {

		this.wrappedPortalContext = portletRequest.getPortalContext();

		// Determine whether or not the portlet was added via $theme.runtime(...)
		Boolean renderPortletResource = (Boolean) portletRequest.getAttribute("RENDER_PORTLET_RESOURCE");

		// If this is a runtime portlet, then it is not possible to add resources to the head section since
		// top_head.jsp is included prior to the runtime portlet being invoked.
		boolean runtimePortlet = (renderPortletResource != null) && renderPortletResource;

		// If this portlet is running via WSRP, then it is not possible to add resources to the head section
		// because Liferay doesn't support that feature with WSRP.
		boolean wsrpPortlet = BooleanHelper.isTrueToken(portletRequest.getParameter("wsrp"));

		// If the portlet's state is exclusive, then the head section will not be written.
		ThemeDisplay themeDisplay = (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
		boolean stateExclusive = themeDisplay.isStateExclusive();

		String portletPhase = (String) portletRequest.getAttribute(PortletRequest.LIFECYCLE_PHASE);

		boolean headerPhase = PortletRequest.HEADER_PHASE.equals(portletPhase);

		if (headerPhase && !wsrpPortlet && !stateExclusive) {
			this.addToHeadSupport = "true";
		}
		else {
			this.addToHeadSupport = null;
		}

		if (isLiferayNamespacingParameters(portletRequest)) {
			this.namespacedParametersRequired = "true";
		}
		else {
			this.namespacedParametersRequired = null;
		}
	}

	@Override
	public String getPortalInfo() {
		return wrappedPortalContext.getPortalInfo();
	}

	@Override
	public String getProperty(String name) {

		if (BridgePortalContext.ADD_ELEMENT_TO_HEAD_SUPPORT.equals(name) ||
				BridgePortalContext.ADD_SCRIPT_RESOURCE_TO_HEAD_SUPPORT.equals(name) ||
				BridgePortalContext.ADD_SCRIPT_TEXT_TO_HEAD_SUPPORT.equals(name) ||
				BridgePortalContext.ADD_STYLE_SHEET_RESOURCE_TO_HEAD_SUPPORT.equals(name)) {
			return addToHeadSupport;
		}
		else if (BridgePortalContext.ADD_STYLE_SHEET_TEXT_TO_HEAD_SUPPORT.equals(name)) {
			return null;
		}
		else if (BridgePortalContext.CREATE_RENDER_URL_DURING_ACTION_PHASE_SUPPORT.equals(name)) {
			return "true";
		}
		else if (BridgePortalContext.STRICT_NAMESPACED_PARAMETERS_SUPPORT.equals(name)) {
			return namespacedParametersRequired;
		}
		else if (BridgePortalContext.POST_REDIRECT_GET_SUPPORT.equals(name)) {

			// Liferay Portal does not implement the POST-REDIRECT-GET design pattern. Rather, the ACTION_PHASE and
			// RENDER_PHASE are both part of a single HTTP POST request.
			return null;
		}
		else if (BridgePortalContext.SET_HTTP_STATUS_CODE_SUPPORT.equals(name)) {
			return "true";
		}
		else {
			return wrappedPortalContext.getProperty(name);
		}
	}

	@Override
	public Enumeration<String> getPropertyNames() {
		return wrappedPortalContext.getPropertyNames();
	}

	@Override
	public Enumeration<PortletMode> getSupportedPortletModes() {
		return wrappedPortalContext.getSupportedPortletModes();
	}

	@Override
	public Enumeration<WindowState> getSupportedWindowStates() {
		return wrappedPortalContext.getSupportedWindowStates();
	}

	private boolean isLiferayNamespacingParameters(PortletRequest portletRequest) {

		boolean liferayNamespacingParameters = false;
		String portletId = (String) portletRequest.getAttribute(WebKeys.PORTLET_ID);

		try {

			ThemeDisplay themeDisplay = (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
			Portlet portlet = PortletLocalServiceUtil.getPortletById(themeDisplay.getCompanyId(), portletId);
			liferayNamespacingParameters = portlet.isRequiresNamespacedParameters();
		}
		catch (SystemException e) {
			logger.error(e);
		}

		return liferayNamespacingParameters;
	}
}
