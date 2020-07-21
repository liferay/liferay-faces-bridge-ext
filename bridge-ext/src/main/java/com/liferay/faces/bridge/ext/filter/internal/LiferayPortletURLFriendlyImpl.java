/**
 * Copyright (c) 2000-2020 Liferay, Inc. All rights reserved.
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

import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;


/**
 * @author  Neil Griffin
 */
public abstract class LiferayPortletURLFriendlyImpl extends LiferayBaseURLFriendlyImpl implements LiferayPortletURL {

	protected LiferayPortletURLFriendlyImpl(String encoding) {
		super(encoding);
	}

	@Override
	public abstract PortletURL getWrapped();

	public PortletMode getPortletMode() {
		return getWrapped().getPortletMode();
	}

	public WindowState getWindowState() {
		return getWrapped().getWindowState();
	}

	public void removePublicRenderParameter(String name) {
		getWrapped().removePublicRenderParameter(name);
	}

	public void setPortletMode(PortletMode portletMode) throws PortletModeException {
		getWrapped().setPortletMode(portletMode);
		resetToString();
	}

	public void setWindowState(WindowState windowState) throws WindowStateException {
		getWrapped().setWindowState(windowState);
		resetToString();
	}
}
