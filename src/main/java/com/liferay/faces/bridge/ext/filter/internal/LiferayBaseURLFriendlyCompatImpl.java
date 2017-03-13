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

import javax.portlet.BaseURL;
import javax.portlet.filter.BaseURLWrapper;


/**
 * @author  Neil Griffin
 */
public abstract class LiferayBaseURLFriendlyCompatImpl extends BaseURLWrapper {

	LiferayBaseURLFriendlyCompatImpl(BaseURL baseURL) {
		super(baseURL);
	}

	@Override
	public void setParameter(String name, String value) {

		super.setParameter(name, value);
		resetToString();
	}

	protected abstract void resetToString();
}
