/**
 * Copyright (c) 2000-2016 Liferay, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
