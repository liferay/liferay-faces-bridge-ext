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
package com.liferay.faces.bridge.ext.config.internal;

import javax.faces.context.ExternalContext;
import javax.portlet.PortletConfig;

import com.liferay.faces.util.config.ConfigParam;
import com.liferay.faces.util.helper.BooleanHelper;


/**
 * @author  Neil Griffin
 */
public enum LiferayPortletConfigParam implements ConfigParam<PortletConfig> {

	/**
	 * Flag indicating whether or not JSF {@link javax.faces.bean.ManagedBean} classes annotated with {@link
	 * javax.faces.bean.RequestScoped} should be distinct for each portlet. Default value is false.
	 */
	DistinctRequestScopedManagedBeans("com.liferay.faces.bridge.distinctRequestScopedManagedBeans", false),

	/** Default value for Liferay Portal 6.2 is false, default value for 7.0+ is true. */
	RequestDispatcherForwardEnabled("com.liferay.faces.bridge.requestDispatcherForwardEnabled", true),

	/**
	 * Comma-delimited list of [library:resource] values for which the AMD loader should be disabled in addition to the
	 * resources listed in {@link com.liferay.faces.bridge.ext.application.internal.ResourceHandlerLiferayImpl}.
	 */
	DisabledAMDLoaderResources("com.liferay.faces.bridge.ext.application.disabledAMDLoaderResources", ""),

	DisabledAMDLoaderResourcesInitialCacheCapacity(
		"com.liferay.faces.bridge.ext.application.disabledAMDLoaderResources.INITIAL_CACHE_CAPACITY", 16),

	DisabledAMDLoaderResourcesMaxCacheCapacity(
		"com.liferay.faces.bridge.ext.application.disabledAMDLoaderResources.MAX_CACHE_CAPACITY", -1),

	/**
	 * Flag indicating whether an id should be rendered for head resources such as scripts and style sheets in order to
	 * allow single page application engines (SPAs) such as Liferay's SennaJS to deduplicate resources between portlets.
	 * If the same resource is rendered by two different portlets, it will have different URLs in each case, and SPAs
	 * will not be able to tell that the resources are the same. Setting this flag to true causes an id attribute to be
	 * rendered for each resource which has a name. The id includes the name and the library (if it exists) of the
	 * resource in order to uniquely identify the resource. The default value is true.
	 */
	RenderHeadResourceIds("com.liferay.faces.bridge.ext.renderHeadResourceIds", true),

	/**
	 * The default value of the data-senna-track attribute for PrimeFaces CSS resources. Defaults to "permanent" in
	 * order to ensure that PrimeFaces CSS remains on the page after SennaJS navigation (in order to hide PrimeFaces
	 * elements that may not have been cleaned up). This value can be overridden on a per-component basis by setting the
	 * data-senna-track attribute as a JSF pass-through attribute in your view or via {@link
	 * javax.faces.component.UIComponent#getPassThroughAttributes()}. This value will be ignored if {@link
	 * #PrimeFacesCSSRenderDefaultDataSennaTrack} is set to false.
	 */
	PrimeFacesCSSDefaultDataSennaTrackValue("com.liferay.faces.bridge.ext.primeFacesCSSDefaultDataSennaTrackValue",
		"permanent"),

	PrimeFacesCSSRenderDefaultDataSennaTrack("com.liferay.faces.bridge.ext.primeFacesCSSRenderDefaultDataSennaTrack",
		true);

	// Private Data Members
	private String alternateName;
	private boolean defaultBooleanValue;
	private String defaultStringValue;
	private int defaultIntegerValue;
	private long defaultLongValue;
	private String name;

	private LiferayPortletConfigParam(String name, String defaultStringValue) {
		this(name, null, defaultStringValue);
	}

	private LiferayPortletConfigParam(String name, boolean defaultBooleanValue) {
		this(name, null, defaultBooleanValue);
	}

	private LiferayPortletConfigParam(String name, int defaultIntegerValue) {
		this.name = name;
		this.defaultBooleanValue = (defaultIntegerValue != 0);
		this.defaultIntegerValue = defaultIntegerValue;
		this.defaultLongValue = defaultIntegerValue;
		this.defaultStringValue = Integer.toString(defaultIntegerValue);
	}

	private LiferayPortletConfigParam(String name, String alternateName, String defaultStringValue) {
		this.name = name;
		this.alternateName = alternateName;

		if (BooleanHelper.isTrueToken(defaultStringValue)) {
			this.defaultBooleanValue = true;
			this.defaultIntegerValue = 1;
			this.defaultLongValue = 1L;
		}
		else {
			this.defaultBooleanValue = false;
			this.defaultIntegerValue = 0;
			this.defaultLongValue = 0L;
		}

		this.defaultStringValue = defaultStringValue;
	}

	private LiferayPortletConfigParam(String name, String alternateName, boolean defaultBooleanValue) {

		this.name = name;
		this.alternateName = alternateName;
		this.defaultBooleanValue = defaultBooleanValue;

		if (defaultBooleanValue) {
			this.defaultIntegerValue = 1;
			this.defaultLongValue = 1L;
			this.defaultStringValue = Boolean.TRUE.toString();
		}
		else {
			this.defaultIntegerValue = 0;
			this.defaultLongValue = 0L;
			this.defaultStringValue = Boolean.FALSE.toString();
		}
	}

	@Override
	public String getAlternateName() {
		return alternateName;
	}

	public boolean getBooleanValue(ExternalContext externalContext) {
		return LiferayPortletConfigParamUtil.getBooleanValue(externalContext, name, alternateName, defaultBooleanValue);
	}

	@Override
	public boolean getBooleanValue(PortletConfig portletConfig) {
		return LiferayPortletConfigParamUtil.getBooleanValue(portletConfig, name, alternateName, defaultBooleanValue);
	}

	@Override
	public String getConfiguredValue(PortletConfig config) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getDefaultBooleanValue() {
		return defaultBooleanValue;
	}

	@Override
	public int getDefaultIntegerValue() {
		return defaultIntegerValue;
	}

	@Override
	public long getDefaultLongValue() {
		return defaultLongValue;
	}

	@Override
	public String getDefaultStringValue() {
		return defaultStringValue;
	}

	@Override
	public int getIntegerValue(PortletConfig config) {
		throw new UnsupportedOperationException();
	}

	public int getIntegerValue(ExternalContext externalContext) {
		return LiferayPortletConfigParamUtil.getIntegerValue(externalContext, name, alternateName, defaultIntegerValue);
	}

	@Override
	public long getLongValue(PortletConfig config) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getStringValue(PortletConfig config) {
		throw new UnsupportedOperationException();
	}

	public String getStringValue(ExternalContext externalContext) {
		return LiferayPortletConfigParamUtil.getConfiguredValue(externalContext, name, alternateName,
				defaultStringValue);
	}

	@Override
	public boolean isConfigured(PortletConfig config) {
		throw new UnsupportedOperationException();
	}
}
