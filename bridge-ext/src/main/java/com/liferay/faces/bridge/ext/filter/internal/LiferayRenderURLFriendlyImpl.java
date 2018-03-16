/**
 * Copyright (c) 2000-2018 Liferay, Inc. All rights reserved.
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
 * @author  Neil Griffin
 */
public class LiferayRenderURLFriendlyImpl extends LiferayPortletURLFriendlyImpl implements LiferayRenderURL {

	// Private Data Members
	private String responseNamespace;

	public LiferayRenderURLFriendlyImpl(PortletURL liferayPortletURL, String responseNamespace, String encoding) {
		super(liferayPortletURL, encoding);
		this.responseNamespace = responseNamespace;
	}

	@Override
	public void setBeanParameter(PortletSerializable bean) {
		((PortletURL) getWrapped()).setBeanParameter(bean);
	}

	@Override
	protected LiferayURLGenerator getLiferayURLGenerator() {

		PortletURL renderURL = (PortletURL) getWrapped();

		return new LiferayURLGeneratorRenderImpl(renderURL, responseNamespace, encoding);
	}
}
