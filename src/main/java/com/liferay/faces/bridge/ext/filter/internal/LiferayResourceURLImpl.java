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

import javax.portlet.MutableResourceParameters;


/**
 * @author  Neil Griffin
 */
public class LiferayResourceURLImpl extends LiferayBaseURLImpl implements LiferayResourceURL {

	// Private Data Members
	private String cacheLevel;
	private String resourceId;
	private String toStringValue;

	public LiferayResourceURLImpl(LiferayURLGenerator liferayURLGenerator) {
		super(liferayURLGenerator);
	}

	@Override
	public String getCacheability() {
		return cacheLevel;
	}

	@Override
	public String getResourceID() {
		return resourceId;
	}

	@Override
	public MutableResourceParameters getResourceParameters() {
		return null; // no-op
	}

	@Override
	public void setCacheability(String cacheLevel) {
		this.cacheLevel = cacheLevel;
	}

	@Override
	public void setResourceID(String resourceID) {
		this.resourceId = resourceID;
		resetToString();
	}

	@Override
	public String toString() {

		if (toStringValue == null) {
			toStringValue = getLiferayURLGenerator().generateURL(getParameterMap(), cacheLevel, resourceId);
		}

		return toStringValue;
	}

	@Override
	protected void resetToString() {
		this.toStringValue = null;
	}
}
