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

import com.liferay.faces.util.product.Product;
import com.liferay.faces.util.product.ProductFactory;


/**
 * @author  Kyle Stiemann
 */
public class ResourceHandlerLiferayImpl extends ResourceHandlerWrapper {

	// Private Constants
	private static final boolean PRIMEFACES_DETECTED = ProductFactory.getProduct(Product.Name.PRIMEFACES).isDetected();
	private static final Set<String> PRIMEFACES_JQUERY_PLUGIN_JS_RESOURCES;
	private static final boolean RICHFACES_DETECTED = ProductFactory.getProduct(Product.Name.RICHFACES).isDetected();

	static {

		// This list of resources was obtained by building Primefaces and searching the target/ directory for js files
		// containg "amd".
		Set<String> primefacesJQueryPluginResources = new HashSet<String>();
		primefacesJQueryPluginResources.add("fileupload/fileupload.js");
		primefacesJQueryPluginResources.add("inputnumber/0-autoNumeric.js");
		primefacesJQueryPluginResources.add("inputnumber/inputnumber.js");
		primefacesJQueryPluginResources.add("jquery/jquery-plugins.js");
		primefacesJQueryPluginResources.add("jquery/jquery.js");
		primefacesJQueryPluginResources.add("knob/1-jquery.knob.js");
		primefacesJQueryPluginResources.add("knob/knob.js");
		primefacesJQueryPluginResources.add("mobile/jquery-mobile.js");
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

		if (isJQueryPluginJSResource(resource, null, resourceName)) {
			resource = new ResourceJQueryPluginJSImpl(resource);
		}

		return resource;
	}

	@Override
	public Resource createResource(String resourceName, String libraryName) {

		Resource resource = super.createResource(resourceName, libraryName);

		if (isJQueryPluginJSResource(resource, libraryName, resourceName)) {
			resource = new ResourceJQueryPluginJSImpl(resource);
		}

		return resource;
	}

	@Override
	public Resource createResource(String resourceName, String libraryName, String contentType) {

		Resource resource = super.createResource(resourceName, libraryName, contentType);

		if (isJQueryPluginJSResource(resource, libraryName, resourceName)) {
			resource = new ResourceJQueryPluginJSImpl(resource);
		}

		return resource;
	}

	@Override
	public ResourceHandler getWrapped() {
		return wrappedResourceHandler;
	}

	private boolean isJQueryPluginJSResource(Resource resource, String resourceLibrary, String resourceName) {

		boolean primeFacesJQueryPluginJSResource = PRIMEFACES_DETECTED &&
			((resourceLibrary == null) || resourceLibrary.equals("primefaces")) &&
			PRIMEFACES_JQUERY_PLUGIN_JS_RESOURCES.contains(resourceName);

		boolean richFacesJQueryPluginJSResource = false;

		if (RICHFACES_DETECTED) {

			boolean richfacesResourceLibrary = ("org.richfaces.resource".equals(resourceLibrary) ||
					"org.richfaces.staticResource".equals(resourceLibrary) || "org.richfaces".equals(resourceLibrary));

			richFacesJQueryPluginJSResource = ((resourceLibrary == null) || richfacesResourceLibrary) &&
				(resourceName.endsWith("packed.js") || resourceName.endsWith("jquery.js"));
		}

		return (resource != null) && (primeFacesJQueryPluginJSResource || richFacesJQueryPluginJSResource);
	}
}
