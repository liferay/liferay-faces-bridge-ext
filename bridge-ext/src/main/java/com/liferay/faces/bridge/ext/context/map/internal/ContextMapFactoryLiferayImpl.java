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
package com.liferay.faces.bridge.ext.context.map.internal;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.servlet.ServletContext;

import com.liferay.faces.bridge.context.ContextMapFactory;
import com.liferay.faces.bridge.ext.config.internal.LiferayPortletConfigParam;
import com.liferay.faces.bridge.model.UploadedFile;
import com.liferay.faces.bridge.scope.BridgeRequestScope;


/**
 * @author  Neil Griffin
 */
public class ContextMapFactoryLiferayImpl extends ContextMapFactory {

	// Private Data Members
	private ContextMapFactory wrappedContextMapFactory;

	public ContextMapFactoryLiferayImpl(ContextMapFactory contextMapFactory) {
		this.wrappedContextMapFactory = contextMapFactory;
	}

	@Override
	public Map<String, Object> getApplicationScopeMap(PortletContext portletContext, boolean preferPreDestroy) {
		return wrappedContextMapFactory.getApplicationScopeMap(portletContext, preferPreDestroy);
	}

	@Override
	public Map<String, String> getFacesViewParameterMap(String facesViewQueryString) {
		return wrappedContextMapFactory.getFacesViewParameterMap(facesViewQueryString);
	}

	@Override
	public Map<String, String> getInitParameterMap(PortletContext portletContext) {
		return wrappedContextMapFactory.getInitParameterMap(portletContext);
	}

	@Override
	public Map<String, Object> getRequestCookieMap(PortletRequest portletRequest) {
		return wrappedContextMapFactory.getRequestCookieMap(portletRequest);
	}

	@Override
	public Map<String, String> getRequestHeaderMap(PortletRequest portletRequest) {
		return wrappedContextMapFactory.getRequestHeaderMap(portletRequest);
	}

	@Override
	public Map<String, String[]> getRequestHeaderValuesMap(PortletRequest portletRequest) {
		return wrappedContextMapFactory.getRequestHeaderValuesMap(portletRequest);
	}

	@Override
	public Map<String, String> getRequestParameterMap(PortletRequest portletRequest, String responseNamespace,
		PortletConfig portletConfig, BridgeRequestScope bridgeRequestScope, String defaultRenderKitId,
		String facesViewQueryString) {
		return wrappedContextMapFactory.getRequestParameterMap(portletRequest, responseNamespace, portletConfig,
				bridgeRequestScope, defaultRenderKitId, facesViewQueryString);
	}

	@Override
	public Map<String, String[]> getRequestParameterValuesMap(PortletRequest portletRequest, String responseNamespace,
		PortletConfig portletConfig, BridgeRequestScope bridgeRequestScope, String defaultRenderKitId,
		String facesViewQueryString) {
		return wrappedContextMapFactory.getRequestParameterValuesMap(portletRequest, responseNamespace, portletConfig,
				bridgeRequestScope, defaultRenderKitId, facesViewQueryString);
	}

	@Override
	public Map<String, Object> getRequestScopeMap(PortletContext portletContext, PortletRequest portletRequest,
		String responseNamespace, Set<String> removedAttributeNames, boolean preferPreDestroy) {

		Map<String, Object> requestScopeMap = wrappedContextMapFactory.getRequestScopeMap(portletContext,
				portletRequest, responseNamespace, removedAttributeNames, preferPreDestroy);
		PortletConfig portletConfig = (PortletConfig) portletRequest.getAttribute(PortletConfig.class.getName());
		boolean distinctRequestScopedManagedBeans = LiferayPortletConfigParam.DistinctRequestScopedManagedBeans
			.getBooleanValue(portletConfig);

		return new RequestScopeMapLiferayImpl(requestScopeMap, portletRequest, responseNamespace,
				distinctRequestScopedManagedBeans);
	}

	@Override
	public Map<String, Object> getServletContextAttributeMap(ServletContext servletContext) {
		return wrappedContextMapFactory.getServletContextAttributeMap(servletContext);
	}

	@Override
	public Map<String, Object> getSessionScopeMap(PortletContext portletContext, PortletSession portletSession,
		int scope, boolean preferPreDestroy) {
		return wrappedContextMapFactory.getSessionScopeMap(portletContext, portletSession, scope, preferPreDestroy);
	}

	@Override
	public Map<String, List<UploadedFile>> getUploadedFileMap(PortletRequest portletRequest) {
		return wrappedContextMapFactory.getUploadedFileMap(portletRequest);
	}

	@Override
	public ContextMapFactory getWrapped() {
		return wrappedContextMapFactory;
	}
}
