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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
	private static final Set<String> PRIMEFACES_JQUERY_PLUGIN_JS_RESOURCES;

	static {

		// This list of resources (excepting jquery/jquery-plugins.js) was obtained via the following search of the
		// Primefaces github source: https://github.com/primefaces/primefaces/search?utf8=%E2%9C%93&q=amd
		Set<String> primefacesJQueryPluginResources = new HashSet<String>();
		primefacesJQueryPluginResources.add("inputnumber/0-autoNumeric.js");
		primefacesJQueryPluginResources.add("knob/1-jquery.knob.js");
		primefacesJQueryPluginResources.add("fileupload/fileupload.js");
		primefacesJQueryPluginResources.add("jquery/jquery-plugins.js");
		primefacesJQueryPluginResources.add("moment/moment.js");
		primefacesJQueryPluginResources.add("push/push.js");
		primefacesJQueryPluginResources.add("schedule/schedule.js");
		primefacesJQueryPluginResources.add("touch/touchswipe.js");
		PRIMEFACES_JQUERY_PLUGIN_JS_RESOURCES = Collections.unmodifiableSet(primefacesJQueryPluginResources);
	}

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
			((resourceLibrary == null) || resourceLibrary.equals("primefaces")) &&
			PRIMEFACES_JQUERY_PLUGIN_JS_RESOURCES.contains(resourceName);
	}

	@Override
	public ResourceHandler getWrapped() {
		return wrappedResourceHandler;
	}
}
