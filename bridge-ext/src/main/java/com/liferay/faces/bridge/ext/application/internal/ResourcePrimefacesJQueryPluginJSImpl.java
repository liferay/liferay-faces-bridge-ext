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
package com.liferay.faces.bridge.ext.application.internal;

import javax.faces.application.Resource;

import com.liferay.faces.util.application.FilteredResourceBase;


/**
 * @author  Kyle Stiemann
 */
public class ResourcePrimefacesJQueryPluginJSImpl extends FilteredResourceBase {

	// Private Members
	private Resource wrappedResource;

	public ResourcePrimefacesJQueryPluginJSImpl(Resource wrappedResource) {
		this.wrappedResource = wrappedResource;
	}

	@Override
	protected String filter(String string) {
		return string.replaceAll("typeof\\s+define\\s*===\\s*[\"']function[\"']", "false&&typeof define==='function'");
	}

	@Override
	public Resource getWrapped() {
		return wrappedResource;
	}
}
