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
package com.liferay.faces.bridge.ext.application.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.faces.application.Resource;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.liferay.faces.util.application.FilteredResourceBase;
import com.liferay.faces.util.application.ResourceUtil;
import com.liferay.faces.util.cache.Cache;


/**
 * @author  Kyle Stiemann
 */
public class JSResourceWithDisabledAMDLoaderImpl extends FilteredResourceBase {

	// Private Members
	private Resource wrappedResource;

	public JSResourceWithDisabledAMDLoaderImpl(Resource wrappedResource) {
		this.wrappedResource = wrappedResource;
	}

	@Override
	public InputStream getInputStream() throws IOException {

		InputStream inputStream = null;
		Cache<String, String> filteredResourceCache = getFilteredResourceCache();

		if (filteredResourceCache != null) {

			String filteredResourceString = filteredResourceCache.getValue(getResourceId());

			if (filteredResourceString != null) {
				inputStream = ResourceUtil.toInputStream(filteredResourceString, getEncoding());
			}
		}

		if (inputStream == null) {

			// This method is implemented by FilteredResourceBase and calls the filter() method which caches the
			// filtered resource string.
			inputStream = super.getInputStream();
		}

		return inputStream;
	}

	@Override
	public Resource getWrapped() {
		return wrappedResource;
	}

	@Override
	protected String filter(String string) {

		String filteredResourceString = null;

		Cache<String, String> filteredResourceCache = getFilteredResourceCache();
		String resourceId = getResourceId();

		if (filteredResourceCache != null) {
			filteredResourceString = filteredResourceCache.getValue(resourceId);
		}

		if (filteredResourceString == null) {

			filteredResourceString = string.replaceAll("typeof\\s+define\\s*=(=+)\\s*[\"']function[\"']",
					"false&&typeof define=$1'function'").replaceAll("[\"']function[\"']\\s*=(=+)\\s*typeof\\s+define",
					"false&&'function'=$1typeof define");

			if (filteredResourceCache != null) {
				filteredResourceString = filteredResourceCache.putValueIfAbsent(resourceId, filteredResourceString);
			}
		}

		return filteredResourceString;
	}

	private Cache<String, String> getFilteredResourceCache() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		Map<String, Object> applicationMap = externalContext.getApplicationMap();

		return (Cache<String, String>) applicationMap.get(JSResourceWithDisabledAMDLoaderImpl.class.getName());
	}

	private String getResourceId() {

		Resource wrappedResource = getWrapped();
		String libraryName = wrappedResource.getLibraryName();
		String resourceName = wrappedResource.getResourceName();

		return ResourceUtil.getResourceId(libraryName, resourceName);
	}
}
