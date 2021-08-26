/**
 * Copyright (c) 2000-2021 Liferay, Inc. All rights reserved.
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

import javax.faces.FacesWrapper;
import javax.portlet.PortletURL;


/**
 * @author  Kyle Stiemann
 */
public class LiferayActionURLFriendlyImpl extends LiferayPortletURLFriendlyImpl implements LiferayActionURL,
	FacesWrapper<PortletURL> {

	// Private Members
	private String responseNamespace;
	private PortletURL wrappedLiferayPortletURL;

	public LiferayActionURLFriendlyImpl(PortletURL wrappedLiferayPortletURL, String responseNamespace,
		String encoding) {
		super(encoding);
		this.wrappedLiferayPortletURL = wrappedLiferayPortletURL;
		this.responseNamespace = responseNamespace;
	}

	@Override
	public PortletURL getWrapped() {
		return wrappedLiferayPortletURL;
	}

	@Override
	protected LiferayURLGenerator getLiferayURLGenerator() {

		PortletURL actionURL = getWrapped();

		return new LiferayURLGeneratorActionImpl(actionURL, responseNamespace, encoding);
	}
}
