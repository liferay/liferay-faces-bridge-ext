/**
 * Copyright (c) 2000-2019 Liferay, Inc. All rights reserved.
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
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.FacesWrapper;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.xml.namespace.QName;


/**
 * @author  Neil Griffin
 */
public class BridgePortletConfigLiferayImpl implements PortletConfig, FacesWrapper<PortletConfig> {

	// Private Data Members
	private PortletConfig wrappedPortletConfig;

	public BridgePortletConfigLiferayImpl(PortletConfig portletConfig) {
		this.wrappedPortletConfig = portletConfig;
	}

	@Override
	public Map<String, String[]> getContainerRuntimeOptions() {
		return wrappedPortletConfig.getContainerRuntimeOptions();
	}

	@Override
	public String getDefaultNamespace() {
		return wrappedPortletConfig.getDefaultNamespace();
	}

	@Override
	public String getInitParameter(String name) {
		return wrappedPortletConfig.getInitParameter(name);
	}

	@Override
	public Enumeration<String> getInitParameterNames() {
		return wrappedPortletConfig.getInitParameterNames();
	}

	@Override
	public PortletContext getPortletContext() {

		PortletContext wrappedPortletContext = wrappedPortletConfig.getPortletContext();

		return new BridgePortletContextLiferayImpl(wrappedPortletContext);
	}

	@Override
	public String getPortletName() {
		return wrappedPortletConfig.getPortletName();
	}

	@Override
	public Enumeration<QName> getProcessingEventQNames() {
		return wrappedPortletConfig.getProcessingEventQNames();
	}

	@Override
	public Enumeration<String> getPublicRenderParameterNames() {
		return wrappedPortletConfig.getPublicRenderParameterNames();
	}

	@Override
	public Enumeration<QName> getPublishingEventQNames() {
		return wrappedPortletConfig.getPublishingEventQNames();
	}

	@Override
	public ResourceBundle getResourceBundle(Locale locale) {
		return wrappedPortletConfig.getResourceBundle(locale);
	}

	@Override
	public Enumeration<Locale> getSupportedLocales() {
		return wrappedPortletConfig.getSupportedLocales();
	}

	@Override
	public PortletConfig getWrapped() {
		return wrappedPortletConfig;
	}
}
