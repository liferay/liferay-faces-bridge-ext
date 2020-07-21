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

import java.net.MalformedURLException;
import java.net.URL;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.filter.PortletContextWrapper;


/**
 * @author  Neil Griffin
 */
public class BridgePortletContextLiferayImpl extends PortletContextWrapper {

	public BridgePortletContextLiferayImpl(PortletContext portletContext) {
		super(portletContext);
	}

	@Override
	public PortletRequestDispatcher getRequestDispatcher(String path) {

		PortletRequestDispatcher portletRequestDispatcher = super.getRequestDispatcher(path);

		return new PortletRequestDispatcherBridgeLiferayImpl(portletRequestDispatcher, path);
	}

	@Override
	public URL getResource(String path) throws MalformedURLException {

		// https://issues.liferay.com/browse/FACES-3473
		return super.getResource(path);
	}
}
