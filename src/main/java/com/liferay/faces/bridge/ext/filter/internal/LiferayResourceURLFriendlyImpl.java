/**
 * Copyright (c) 2000-2017 Liferay, Inc. All rights reserved.
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

import javax.portlet.ResourceURL;

import com.liferay.faces.util.helper.Wrapper;


/**
 * @author  Kyle Stiemann
 */
public class LiferayResourceURLFriendlyImpl extends LiferayBaseURLFriendlyImpl implements LiferayResourceURL,
	Wrapper<ResourceURL> {

	// Private Members
	private String responseNamespace;
	private ResourceURL wrappedLiferayResourceURL;

	public LiferayResourceURLFriendlyImpl(ResourceURL wrappedLiferayResourceURL, String responseNamespace) {
		this.wrappedLiferayResourceURL = wrappedLiferayResourceURL;
		this.responseNamespace = responseNamespace;
	}

	// Java 1.6+ @Override
	public String getCacheability() {
		return getWrapped().getCacheability();
	}

	@Override
	public ResourceURL getWrapped() {
		return wrappedLiferayResourceURL;
	}

	// Java 1.6+ @Override
	public void setCacheability(String cacheLevel) {
		getWrapped().setCacheability(cacheLevel);
		resetToString();
	}

	public void setResourceID(String resourceID) {
		getWrapped().setResourceID(resourceID);
		resetToString();
	}

	@Override
	protected LiferayURLGenerator getLiferayURLGenerator() {

		ResourceURL resourceURL = getWrapped();

		return new LiferayURLGeneratorResourceImpl(resourceURL.toString(), responseNamespace);
	}
}
