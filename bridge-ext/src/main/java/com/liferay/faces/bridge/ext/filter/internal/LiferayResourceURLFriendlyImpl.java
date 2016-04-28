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

import javax.faces.FacesWrapper;
import javax.portlet.ResourceURL;


/**
 * @author  Kyle Stiemann
 */
public class LiferayResourceURLFriendlyImpl extends LiferayBaseURLFriendlyImpl implements LiferayResourceURL,
	FacesWrapper<ResourceURL> {

	// Private Members
	private String responseNamespace;
	private ResourceURL wrappedLiferayResourceURL;

	public LiferayResourceURLFriendlyImpl(ResourceURL wrappedLiferayResourceURL, String responseNamespace) {
		this.wrappedLiferayResourceURL = wrappedLiferayResourceURL;
		this.responseNamespace = responseNamespace;
	}

	@Override
	public String getCacheability() {
		return getWrapped().getCacheability();
	}

	@Override
	public void setCacheability(String cacheLevel) {
		getWrapped().setCacheability(cacheLevel);
		resetToString();
	}

	@Override
	protected LiferayURLGenerator getLiferayURLGenerator() {

		ResourceURL resourceURL = getWrapped();

		return new LiferayURLGeneratorResourceImpl(resourceURL.toString(), responseNamespace);
	}

	public void setResourceID(String resourceID) {
		getWrapped().setResourceID(resourceID);
		resetToString();
	}

	@Override
	public ResourceURL getWrapped() {
		return wrappedLiferayResourceURL;
	}
}
