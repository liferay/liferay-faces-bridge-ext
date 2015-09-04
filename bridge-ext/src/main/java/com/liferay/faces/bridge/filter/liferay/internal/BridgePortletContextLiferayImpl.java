package com.liferay.faces.bridge.filter.liferay.internal;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequestDispatcher;

public class BridgePortletContextLiferayImpl extends PortletContextWrapper{

	// Private Data Members
	private PortletContext wrappedPortletContext;

	public BridgePortletContextLiferayImpl(PortletContext portletContext) {
		this.wrappedPortletContext = portletContext;
	}

	@Override
	public PortletRequestDispatcher getRequestDispatcher(String path) {

		PortletRequestDispatcher portletRequestDispatcher = super.getRequestDispatcher(path);

		return new PortletRequestDispatcherBridgeLiferayImpl(portletRequestDispatcher);
	}

	@Override
	public PortletContext getWrapped() {
		return wrappedPortletContext;
	}
}
