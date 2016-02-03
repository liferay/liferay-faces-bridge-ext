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
package com.liferay.faces.bridge.application.liferay.internal;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;

import com.liferay.faces.util.product.ProductConstants;
import com.liferay.faces.util.product.ProductMap;


/**
 * @author  Kyle Stiemann
 */
public class ResourceHandlerLiferayImpl extends ResourceHandlerWrapper {

	// Private Constants
	private static final boolean PRIMEFACES_DETECTED = ProductMap.getInstance().get(ProductConstants.PRIMEFACES)
		.isDetected();

	// Private Members
	private ResourceHandler wrappedResourceHandler;

	public ResourceHandlerLiferayImpl(ResourceHandler wrappedResourceHandler) {
		this.wrappedResourceHandler = wrappedResourceHandler;
	}

	@Override
	public Resource createResource(String resourceName) {

		Resource resource = super.createResource(resourceName);

		if (isPrimeFacesJQueryPluginJSResource(resource, null, resourceName)) {
			resource = new ResourcePrimefacesJQueryPluginJSImpl(resource);
		}

		return resource;
	}

	@Override
	public Resource createResource(String resourceName, String libraryName) {

		Resource resource = super.createResource(resourceName, libraryName);

		if (isPrimeFacesJQueryPluginJSResource(resource, libraryName, resourceName)) {
			resource = new ResourcePrimefacesJQueryPluginJSImpl(resource);
		}

		return resource;
	}

	@Override
	public Resource createResource(String resourceName, String libraryName, String contentType) {

		Resource resource = super.createResource(resourceName, libraryName, contentType);

		if (isPrimeFacesJQueryPluginJSResource(resource, libraryName, resourceName)) {
			resource = new ResourcePrimefacesJQueryPluginJSImpl(resource);
		}

		return resource;
	}

	private boolean isPrimeFacesJQueryPluginJSResource(Resource resource, String resourceLibrary, String resourceName) {
		return (resource != null) && PRIMEFACES_DETECTED &&
			((resourceLibrary == null) || resourceLibrary.equals("primefaces")) && resourceName.endsWith(".js");
	}

	@Override
	public ResourceHandler getWrapped() {
		return wrappedResourceHandler;
	}
}
