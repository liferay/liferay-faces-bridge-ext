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

import java.io.Serializable;

import javax.portlet.PortletConfig;

import com.liferay.faces.bridge.filter.BridgePortletConfigFactory;


/**
 * @author  Neil Griffin
 */
public class BridgePortletConfigFactoryLiferayImpl extends BridgePortletConfigFactory implements Serializable {

	// serialVersionUID
	private static final long serialVersionUID = 3839303045007599701L;

	// Private Data Members
	private BridgePortletConfigFactory wrappedBridgePortletConfigFactory;

	// Instance field must be declared volatile in order for the double-check idiom to work (requires JRE 1.5+)
	private transient volatile PortletConfig portletConfig;

	public BridgePortletConfigFactoryLiferayImpl(BridgePortletConfigFactory bridgePortletConfigFactory) {
		this.wrappedBridgePortletConfigFactory = bridgePortletConfigFactory;
	}

	@Override
	public PortletConfig getPortletConfig(PortletConfig portletConfig) {

		PortletConfig savedPortletConfig = this.portletConfig;

		// First check without locking (not yet thread-safe)
		if (savedPortletConfig == null) {

			synchronized (this) {

				savedPortletConfig = this.portletConfig;

				// Second check with locking (thread-safe)
				if (savedPortletConfig == null) {
					savedPortletConfig = this.portletConfig = new PortletConfigLiferayImpl(portletConfig);
				}
			}
		}

		return savedPortletConfig;
	}

	@Override
	public BridgePortletConfigFactory getWrapped() {
		return wrappedBridgePortletConfigFactory;
	}
}
