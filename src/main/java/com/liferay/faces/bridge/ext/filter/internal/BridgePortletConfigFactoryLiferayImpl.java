package com.liferay.faces.bridge.ext.filter.internal;

import com.liferay.faces.bridge.filter.BridgePortletConfigFactory;

import javax.portlet.PortletConfig;

public class BridgePortletConfigFactoryLiferayImpl extends BridgePortletConfigFactory {

	// Private Data Members
	private BridgePortletConfigFactory wrappedBridgePortletConfigFactory;

	// Instance field must be declared volatile in order for the double-check idiom to work (requires JRE 1.5+)
	private volatile PortletConfig portletConfig;

	public BridgePortletConfigFactoryLiferayImpl(BridgePortletConfigFactory bridgePortletConfigFactory) {
		this.wrappedBridgePortletConfigFactory = bridgePortletConfigFactory;
	}

	@Override
	public PortletConfig getPortletConfig(PortletConfig portletConfig) {

		// First check without locking (not yet thread-safe)
		if (this.portletConfig == null) {

			synchronized (this) {

				if (this.portletConfig == null) {

					// Second check with locking (thread-safe)
					if (this.portletConfig == null) {
						this.portletConfig = new PortletConfigLiferayImpl(portletConfig);
					}
				}
			}
		}

		return this.portletConfig;
	}

	@Override
	public BridgePortletConfigFactory getWrapped() {
		return wrappedBridgePortletConfigFactory;
	}
}
