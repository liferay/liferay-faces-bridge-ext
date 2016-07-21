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

import javax.portlet.PortletContext;

import com.liferay.faces.bridge.filter.BridgePortletContextFactory;


/**
 * @author  Neil Griffin
 */
public class BridgePortletContextFactoryLiferayImpl extends BridgePortletContextFactory implements Serializable {

	// serialVersionUID
	private static final long serialVersionUID = 1452826393887411234L;

	// Private Data Members
	private BridgePortletContextFactory wrappedBridgePortletContextFactory;

	// Instance field must be declared volatile in order for the double-check idiom to work (requires JRE 1.5+)
	private transient volatile PortletContext portletContext;

	public BridgePortletContextFactoryLiferayImpl(BridgePortletContextFactory bridgePortletContextFactory) {
		this.wrappedBridgePortletContextFactory = bridgePortletContextFactory;
	}

	@Override
	public PortletContext getPortletContext(PortletContext portletContext) {

		PortletContext threadSafePortletContext = this.portletContext;

		// First check without locking (not yet thread-safe)
		if (threadSafePortletContext == null) {

			synchronized (this) {

				threadSafePortletContext = this.portletContext;

				// Second check with locking (thread-safe)
				if (threadSafePortletContext == null) {

					PortletContext wrappedPortletContext = wrappedBridgePortletContextFactory.getPortletContext(
							portletContext);
					threadSafePortletContext = this.portletContext = new BridgePortletContextLiferayImpl(
								wrappedPortletContext);
				}
			}
		}

		return threadSafePortletContext;
	}

	@Override
	public BridgePortletContextFactory getWrapped() {
		return wrappedBridgePortletContextFactory;
	}
}
