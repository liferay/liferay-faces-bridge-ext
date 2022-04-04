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

import java.io.Serializable;

import javax.portlet.PortletConfig;

import com.liferay.faces.bridge.filter.BridgePortletConfigFactory;


/**
 * @author  Neil Griffin
 */
public class BridgePortletConfigFactoryLiferayImpl extends BridgePortletConfigFactory implements Serializable {

	// serialVersionUID
	private static final long serialVersionUID = 1452826393887411234L;

	// Private Data Members
	private final BridgePortletConfigFactory wrappedBridgePortletConfigFactory;

	public BridgePortletConfigFactoryLiferayImpl(BridgePortletConfigFactory bridgePortletConfigFactory) {
		this.wrappedBridgePortletConfigFactory = bridgePortletConfigFactory;
	}

	@Override
	public PortletConfig getPortletConfig(PortletConfig portletContext) {

		PortletConfig wrappedPortletConfig = wrappedBridgePortletConfigFactory.getPortletConfig(portletContext);

		return new BridgePortletConfigLiferayImpl(wrappedPortletConfig);
	}

	@Override
	public BridgePortletConfigFactory getWrapped() {
		return wrappedBridgePortletConfigFactory;
	}
}
