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

import java.util.Collections;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Set;


/**
 * @author  Neil Griffin
 */
public class EmptyResourceBundle extends ResourceBundle {

	// Private Data Members
	private Set<String> keys = Collections.emptySet();

	@Override
	public boolean containsKey(String key) {
		return false;
	}

	@Override
	public Enumeration<String> getKeys() {
		return Collections.enumeration(keys);
	}

	@Override
	protected Object handleGetObject(String key) {
		return null;
	}
}
