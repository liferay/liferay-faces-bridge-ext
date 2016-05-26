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

import javax.portlet.MutableRenderParameters;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;


/**
 * @author  Neil Griffin
 */
public abstract class LiferayPortletURLFriendlyImpl extends LiferayBaseURLFriendlyImpl implements LiferayPortletURL {

	public LiferayPortletURLFriendlyImpl(PortletURL portletURL) {
		super(portletURL);
	}

	@Override
	public PortletMode getPortletMode() {
		return getWrapped().getPortletMode();
	}

	@Override
	public MutableRenderParameters getRenderParameters() {
		return ((PortletURL) getWrapped()).getRenderParameters();
	}

	@Override
	public WindowState getWindowState() {
		return getWrapped().getWindowState();
	}

	@Override
	public void removePublicRenderParameter(String name) {
		((PortletURL) getWrapped()).removePublicRenderParameter(name);
	}

	@Override
	public void setPortletMode(PortletMode portletMode) throws PortletModeException {
		((PortletURL) getWrapped()).setPortletMode(portletMode);
		resetToString();
	}

	@Override
	public void setWindowState(WindowState windowState) throws WindowStateException {
		((PortletURL) getWrapped()).setWindowState(windowState);
		resetToString();
	}
}
