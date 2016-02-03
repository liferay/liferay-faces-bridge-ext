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
package com.liferay.faces.bridge.context.map.liferay.internal;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.servlet.ServletContext;

import com.liferay.faces.bridge.config.liferay.internal.LiferayPortletConfigParam;
import com.liferay.faces.bridge.context.BridgeContext;
import com.liferay.faces.bridge.context.ContextMapFactory;
import com.liferay.faces.bridge.model.UploadedFile;


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
	public Map<String, Object> getApplicationScopeMap(BridgeContext bridgeContext) {
		return wrappedContextMapFactory.getApplicationScopeMap(bridgeContext);
	}

	@Override
	public Map<String, String> getFacesViewParameterMap(BridgeContext bridgeContext) {
		return wrappedContextMapFactory.getFacesViewParameterMap(bridgeContext);
	}

	@Override
	public Map<String, String> getInitParameterMap(PortletContext portletContext) {
		return wrappedContextMapFactory.getInitParameterMap(portletContext);
	}

	@Override
	public Map<String, Object> getRequestCookieMap(BridgeContext bridgeContext) {
		return wrappedContextMapFactory.getRequestCookieMap(bridgeContext);
	}

	@Override
	public Map<String, String> getRequestHeaderMap(BridgeContext bridgeContext) {
		return wrappedContextMapFactory.getRequestHeaderMap(bridgeContext);
	}

	@Override
	public Map<String, String[]> getRequestHeaderValuesMap(BridgeContext bridgeContext) {
		return wrappedContextMapFactory.getRequestHeaderValuesMap(bridgeContext);
	}

	@Override
	public Map<String, String> getRequestParameterMap(BridgeContext bridgeContext) {
		return wrappedContextMapFactory.getRequestParameterMap(bridgeContext);
	}

	@Override
	public Map<String, String[]> getRequestParameterValuesMap(BridgeContext bridgeContext) {
		return wrappedContextMapFactory.getRequestParameterValuesMap(bridgeContext);
	}

	@Override
	public Map<String, Object> getRequestScopeMap(BridgeContext bridgeContext) {

		Map<String, Object> requestScopeMap = wrappedContextMapFactory.getRequestScopeMap(bridgeContext);
		PortletConfig portletConfig = bridgeContext.getPortletConfig();
		boolean distinctRequestScopedManagedBeans = LiferayPortletConfigParam.DistinctRequestScopedManagedBeans
			.getBooleanValue(portletConfig);

		return new RequestScopeMapLiferayImpl(requestScopeMap, bridgeContext.getPortletRequest(),
				bridgeContext.getPortletResponse().getNamespace(), distinctRequestScopedManagedBeans);
	}

	@Override
	public Map<String, Object> getServletContextAttributeMap(ServletContext servletContext) {
		return wrappedContextMapFactory.getServletContextAttributeMap(servletContext);
	}

	@Override
	public Map<String, Object> getSessionScopeMap(BridgeContext bridgeContext, int scope) {
		return wrappedContextMapFactory.getSessionScopeMap(bridgeContext, scope);
	}

	@Override
	public Map<String, List<UploadedFile>> getUploadedFileMap(BridgeContext bridgeContext) {
		return wrappedContextMapFactory.getUploadedFileMap(bridgeContext);
	}

	@Override
	public ContextMapFactory getWrapped() {
		return wrappedContextMapFactory;
	}
}
