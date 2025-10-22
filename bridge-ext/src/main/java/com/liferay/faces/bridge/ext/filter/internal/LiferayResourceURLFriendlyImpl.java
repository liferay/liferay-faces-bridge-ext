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
package com.liferay.faces.bridge.ext.filter.internal;

import java.io.IOException;

import javax.portlet.MutableResourceParameters;
import javax.portlet.PortletMode;
import javax.portlet.RenderParameters;
import javax.portlet.ResourceURL;
import javax.portlet.WindowState;


/**
 * @author  Kyle Stiemann
 */
public class LiferayResourceURLFriendlyImpl extends LiferayBaseURLFriendlyImpl implements LiferayResourceURL {

	// Private Members
	private String responseNamespace;

	public LiferayResourceURLFriendlyImpl(ResourceURL wrappedLiferayResourceURL, String responseNamespace,
		String encoding) {
		super(wrappedLiferayResourceURL, encoding);
		this.responseNamespace = responseNamespace;
	}

	@Override
	public Appendable append(Appendable out) throws IOException {
		return getWrapped().append(out);
	}

	@Override
	public Appendable append(Appendable out, boolean escapeXML) throws IOException {
		return getWrapped().append(out, escapeXML);
	}

	@Override
	public String getCacheability() {
		return ((ResourceURL) getWrapped()).getCacheability();
	}

	@Override
	public PortletMode getPortletMode() {
		return getWrapped().getPortletMode();
	}

	@Override
	public RenderParameters getRenderParameters() {
		return getWrapped().getRenderParameters();
	}

	@Override
	public String getResourceID() {
		return ((ResourceURL) getWrapped()).getResourceID();
	}

	@Override
	public MutableResourceParameters getResourceParameters() {
		return ((ResourceURL) getWrapped()).getResourceParameters();
	}

	@Override
	public WindowState getWindowState() {
		return getWrapped().getWindowState();
	}

	@Override
	public void setCacheability(String cacheLevel) {
		((ResourceURL) getWrapped()).setCacheability(cacheLevel);
		resetToString();
	}

	@Override
	public void setResourceID(String resourceID) {
		((ResourceURL) getWrapped()).setResourceID(resourceID);
		resetToString();
	}

	@Override
	protected LiferayURLGenerator getLiferayURLGenerator() {

		ResourceURL resourceURL = (ResourceURL) getWrapped();

		return new LiferayURLGeneratorResourceImpl(resourceURL.toString(), responseNamespace, encoding);
	}
}
