/**
 * Copyright (c) 2000-2019 Liferay, Inc. All rights reserved.
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

import javax.portlet.PortletURL;
import javax.portlet.annotations.PortletSerializable;


/**
 * @author  Kyle Stiemann
 */
public class LiferayActionURLFriendlyImpl extends LiferayPortletURLFriendlyImpl implements LiferayActionURL {

	// Private Members
	private String responseNamespace;

	public LiferayActionURLFriendlyImpl(PortletURL wrappedLiferayPortletURL, String responseNamespace,
		String encoding) {
		super(wrappedLiferayPortletURL, encoding);
		this.responseNamespace = responseNamespace;
	}

	@Override
	public void setBeanParameter(PortletSerializable bean) {
		((PortletURL) getWrapped()).setBeanParameter(bean);
	}

	@Override
	protected LiferayURLGenerator getLiferayURLGenerator() {

		PortletURL actionURL = (PortletURL) getWrapped();

		return new LiferayURLGeneratorActionImpl(actionURL, responseNamespace, encoding);
	}
}
