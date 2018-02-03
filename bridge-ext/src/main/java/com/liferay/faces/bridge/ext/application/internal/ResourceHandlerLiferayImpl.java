/**
 * Copyright (c) 2000-2019 Liferay, Inc. All rights reserved.
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.liferay.faces.bridge.ext.config.internal.LiferayPortletConfigParam;
import com.liferay.faces.util.factory.FactoryExtensionFinder;
import com.liferay.faces.util.product.Product;
import com.liferay.faces.util.product.ProductFactory;


/**
 * @author  Kyle Stiemann
 */
public class ResourceHandlerLiferayImpl extends ResourceHandlerWrapper {

	// Private Constants
	private static final Set<String> BOOTSFACES_JQUERY_PLUGIN_JS_RESOURCES;
	private static final Set<String> BUTTERFACES_DIST_BOWER_JQUERY_PLUGIN_JS_RESOURCES;
	private static final Set<String> BUTTERFACES_DIST_BUNDLE_JS_JQUERY_PLUGIN_JS_RESOURCES;
	private static final Set<String> BUTTERFACES_EXTERNAL_JQUERY_PLUGIN_JS_RESOURCES;
	private static final Set<String> PRIMEFACES_JQUERY_PLUGIN_JS_RESOURCES;

	static {

		// This list of resources was obtained by building BootsFaces and searching the target/ directory for js files
		// containg "typeof\\s+define\\s*=(=+)\\s*[\"']function[\"']|[\"']function[\"']\\s*=(=+)\\s*typeof\\s+define".
		Set<String> bootsFacesJQueryPluginResources = new HashSet<String>();
		bootsFacesJQueryPluginResources.add("jq/ui/i18n/datepicker-pt.js");
		bootsFacesJQueryPluginResources.add("jq/ui/i18n/datepicker-es.js");
		bootsFacesJQueryPluginResources.add("jq/ui/i18n/datepicker-hu.js");
		bootsFacesJQueryPluginResources.add("jq/jquery.js");
		bootsFacesJQueryPluginResources.add("jq/ui/mouse.js");
		bootsFacesJQueryPluginResources.add("jq/ui/slider.js");
		bootsFacesJQueryPluginResources.add("jq/ui/widget.js");
		bootsFacesJQueryPluginResources.add("jq/ui/i18n/datepicker-nl.js");
		bootsFacesJQueryPluginResources.add("jq/ui/i18n/datepicker-it.js");
		bootsFacesJQueryPluginResources.add("jq/ui/i18n/datepicker-pl.js");
		bootsFacesJQueryPluginResources.add("jq/ui/i18n/datepicker-de.js");
		bootsFacesJQueryPluginResources.add("jq/ui/i18n/datepicker-ru.js");
		bootsFacesJQueryPluginResources.add("jq/mobile/shake.js");
		bootsFacesJQueryPluginResources.add("jq/ui/core.js");
		bootsFacesJQueryPluginResources.add("jq/ui/i18n/datepicker-fr.js");
		bootsFacesJQueryPluginResources.add("jq/ui/datepicker.js");
		bootsFacesJQueryPluginResources.add("js/bootstrap-notify.min.js");
		bootsFacesJQueryPluginResources.add("js/jquery.blockUI.js");
		bootsFacesJQueryPluginResources.add("js/moment.min.js");
		bootsFacesJQueryPluginResources.add("js/typeahead.js");
		bootsFacesJQueryPluginResources.add("js/bootstrap-slider.min.js");
		bootsFacesJQueryPluginResources.add("js/fullcalendar-lang-all.js");
		bootsFacesJQueryPluginResources.add("js/jquery.minicolors.min.js");
		bootsFacesJQueryPluginResources.add("js/bootstrap-datetimepicker.min.js");
		bootsFacesJQueryPluginResources.add("js/fullcalendar.min.js");
		bootsFacesJQueryPluginResources.add("js/moment-with-locales.min.js");
		bootsFacesJQueryPluginResources.add("js/datatables.min.js");
		BOOTSFACES_JQUERY_PLUGIN_JS_RESOURCES = Collections.unmodifiableSet(bootsFacesJQueryPluginResources);

		// This list of resources was obtained by building ButterFaces and searching the target/ directory for js files
		// containg "typeof\\s+define\\s*=(=+)\\s*[\"']function[\"']|[\"']function[\"']\\s*=(=+)\\s*typeof\\s+define".
		Set<String> butterFacesJQueryPluginResources = new HashSet<String>();
		butterFacesJQueryPluginResources.add("prettify.js");
		butterFacesJQueryPluginResources.add("jquery.min.js");
		butterFacesJQueryPluginResources.add("jquery.js");
		BUTTERFACES_DIST_BOWER_JQUERY_PLUGIN_JS_RESOURCES = Collections.unmodifiableSet(
				butterFacesJQueryPluginResources);
		butterFacesJQueryPluginResources = new HashSet<String>();
		butterFacesJQueryPluginResources.add("butterfaces-all-with-bootstrap-bundle.min.js");
		butterFacesJQueryPluginResources.add("butterfaces-all-with-jquery-bundle.min.js");
		butterFacesJQueryPluginResources.add("butterfaces-all-bundle.min.js");
		butterFacesJQueryPluginResources.add("butterfaces-all-with-jquery-and-bootstrap-bundle.min.js");
		BUTTERFACES_DIST_BUNDLE_JS_JQUERY_PLUGIN_JS_RESOURCES = Collections.unmodifiableSet(
				butterFacesJQueryPluginResources);
		butterFacesJQueryPluginResources = new HashSet<String>();
		butterFacesJQueryPluginResources.add("mustache.min.js");
		butterFacesJQueryPluginResources.add("jquery.position.min.js");
		butterFacesJQueryPluginResources.add("to-markdown.js");
		butterFacesJQueryPluginResources.add("bootstrap-datetimepicker.min.js");
		butterFacesJQueryPluginResources.add("01-moment-with-locales.min.js");
		butterFacesJQueryPluginResources.add("trivial-components.min.js");
		BUTTERFACES_EXTERNAL_JQUERY_PLUGIN_JS_RESOURCES = Collections.unmodifiableSet(butterFacesJQueryPluginResources);

		// This list of resources was obtained by building Primefaces and searching the target/ directory for js files
		// containg "typeof\\s+define\\s*=(=+)\\s*[\"']function[\"']|[\"']function[\"']\\s*=(=+)\\s*typeof\\s+define".
		Set<String> primefacesJQueryPluginResources = new HashSet<String>();
		primefacesJQueryPluginResources.add("diagram/diagram.js");
		primefacesJQueryPluginResources.add("fileupload/0-jquery.iframe-transport.js");
		primefacesJQueryPluginResources.add("fileupload/1-jquery.fileupload.js");
		primefacesJQueryPluginResources.add("fileupload/fileupload.js");
		primefacesJQueryPluginResources.add("inputnumber/0-autoNumeric.js");
		primefacesJQueryPluginResources.add("inputnumber/inputnumber.js");
		primefacesJQueryPluginResources.add("jquery/jquery.autosize.js");
		primefacesJQueryPluginResources.add("jquery/jquery.cookie.js");
		primefacesJQueryPluginResources.add("jquery/jquery-plugins.js");
		primefacesJQueryPluginResources.add("jquery/jquery.js");
		primefacesJQueryPluginResources.add("jquery/jquery.maskedinput.js");
		primefacesJQueryPluginResources.add("jquery/jquery.ui.js");
		primefacesJQueryPluginResources.add("jquery/jquery.ui.timepicker.js");
		primefacesJQueryPluginResources.add("knob/1-jquery.knob.js");
		primefacesJQueryPluginResources.add("knob/knob.js");
		primefacesJQueryPluginResources.add("mobile/jquery-mobile.js");
		primefacesJQueryPluginResources.add("moment/moment.js");
		primefacesJQueryPluginResources.add("mousewheel/jquery.mousewheel.min.js");
		primefacesJQueryPluginResources.add("photocam/photocam.js");
		primefacesJQueryPluginResources.add("push/push.js");
		primefacesJQueryPluginResources.add("raphael/raphael.js");
		primefacesJQueryPluginResources.add("schedule/schedule.js");
		primefacesJQueryPluginResources.add("socket/0-atmosphere.js");
		primefacesJQueryPluginResources.add("texteditor/texteditor.js");
		primefacesJQueryPluginResources.add("touch/touchswipe.js");
		PRIMEFACES_JQUERY_PLUGIN_JS_RESOURCES = Collections.unmodifiableSet(primefacesJQueryPluginResources);
	}

	// Private Final Data Members
	private final Set<String> disabledAMDLoaderResources;
	private final ResourceHandler wrappedResourceHandler;

	public ResourceHandlerLiferayImpl(ResourceHandler wrappedResourceHandler) {

		this.wrappedResourceHandler = wrappedResourceHandler;

		Set<String> disabledAMDLoaderResources = Collections.emptySet();
		FacesContext startupFacesContext = FacesContext.getCurrentInstance();

		if (startupFacesContext != null) {

			ExternalContext externalContext = startupFacesContext.getExternalContext();
			String configuredValue = LiferayPortletConfigParam.DisabledAMDLoaderResources.getStringValue(
					externalContext);

			if (configuredValue != null) {

				configuredValue = configuredValue.trim();

				String[] resourceIds = configuredValue.split(",");
				boolean first = true;

				for (String resourceId : resourceIds) {

					if ((resourceId != null) && (resourceId.length() > 0)) {

						if (first) {

							disabledAMDLoaderResources = new HashSet<String>();
							first = false;
						}

						resourceId = resourceId.trim();
						disabledAMDLoaderResources.add(resourceId);
					}
				}

				if (!disabledAMDLoaderResources.isEmpty()) {
					disabledAMDLoaderResources = Collections.unmodifiableSet(disabledAMDLoaderResources);
				}
			}
		}

		this.disabledAMDLoaderResources = disabledAMDLoaderResources;
	}

	@Override
	public Resource createResource(String resourceName) {

		Resource resource = super.createResource(resourceName);

		if (isJavaScriptResource(resource) && !(resource instanceof JSResourceWithDisabledAMDLoaderImpl) &&
				(isJQueryPluginJSResource(null, resourceName) || !isAMDLoaderEnabledForResource(null, resourceName))) {
			resource = new JSResourceWithDisabledAMDLoaderImpl(resource);
		}

		return resource;
	}

	@Override
	public Resource createResource(String resourceName, String libraryName) {

		Resource resource = super.createResource(resourceName, libraryName);

		if (isJavaScriptResource(resource) && !(resource instanceof JSResourceWithDisabledAMDLoaderImpl) &&
				(isJQueryPluginJSResource(libraryName, resourceName) ||
					!isAMDLoaderEnabledForResource(libraryName, resourceName))) {
			resource = new JSResourceWithDisabledAMDLoaderImpl(resource);
		}

		return resource;
	}

	@Override
	public Resource createResource(String resourceName, String libraryName, String contentType) {

		Resource resource = super.createResource(resourceName, libraryName, contentType);

		if (isJavaScriptResource(resource) && !(resource instanceof JSResourceWithDisabledAMDLoaderImpl) &&
				(isJQueryPluginJSResource(libraryName, resourceName) ||
					!isAMDLoaderEnabledForResource(libraryName, resourceName))) {
			resource = new JSResourceWithDisabledAMDLoaderImpl(resource);
		}

		return resource;
	}

	@Override
	public ResourceHandler getWrapped() {
		return wrappedResourceHandler;
	}

	private boolean isAMDLoaderEnabledForResource(String libraryName, String resourceName) {

		String resourceId = resourceName;

		if ((libraryName != null) && (libraryName.length() > 0)) {
			resourceId = libraryName + ":" + resourceName;
		}

		return !disabledAMDLoaderResources.contains(resourceId);
	}

	private boolean isJavaScriptResource(Resource resource) {

		if (resource != null) {

			String resourceName = resource.getResourceName();
			String contentType = resource.getContentType();

			return (resourceName.endsWith(".js") || "application/javascript".equals(contentType) ||
					"text/javascript".equals(contentType));
		}
		else {
			return false;
		}
	}

	private boolean isJQueryPluginJSResource(String resourceLibrary, String resourceName) {

		FacesContext startupFacesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = startupFacesContext.getExternalContext();
		ProductFactory productFactory = (ProductFactory) FactoryExtensionFinder.getFactory(externalContext,
				ProductFactory.class);
		final Product BOOTSFACES = productFactory.getProductInfo(Product.Name.BOOTSFACES);
		boolean bootsFacesJQueryPluginJSResource = BOOTSFACES.isDetected() && "bsf".equals(resourceLibrary) &&
			BOOTSFACES_JQUERY_PLUGIN_JS_RESOURCES.contains(resourceName);

		boolean butterFacesJQueryPluginJSResource = false;
		final Product BUTTERFACES = productFactory.getProductInfo(Product.Name.BUTTERFACES);

		if (BUTTERFACES.isDetected() && (resourceLibrary != null)) {

			butterFacesJQueryPluginJSResource = (resourceLibrary.equals("butterfaces-dist-bower") &&
					BUTTERFACES_DIST_BOWER_JQUERY_PLUGIN_JS_RESOURCES.contains(resourceName)) ||
				(resourceLibrary.equals("butterfaces-dist-bundle-js") &&
					BUTTERFACES_DIST_BUNDLE_JS_JQUERY_PLUGIN_JS_RESOURCES.contains(resourceName)) ||
				(resourceLibrary.equals("butterfaces-external") &&
					BUTTERFACES_EXTERNAL_JQUERY_PLUGIN_JS_RESOURCES.contains(resourceName));

		}

		final Product PRIMEFACES = productFactory.getProductInfo(Product.Name.PRIMEFACES);
		boolean primeFacesJQueryPluginJSResource = PRIMEFACES.isDetected() &&
			((resourceLibrary == null) || resourceLibrary.equals("primefaces")) &&
			PRIMEFACES_JQUERY_PLUGIN_JS_RESOURCES.contains(resourceName);

		boolean richFacesJQueryPluginJSResource = false;
		final Product RICHFACES = productFactory.getProductInfo(Product.Name.RICHFACES);

		if (RICHFACES.isDetected()) {

			boolean richfacesResourceLibrary = ("org.richfaces.resource".equals(resourceLibrary) ||
					"org.richfaces.staticResource".equals(resourceLibrary) || "org.richfaces".equals(resourceLibrary));

			richFacesJQueryPluginJSResource = ((resourceLibrary == null) || richfacesResourceLibrary) &&
				(resourceName.endsWith("packed.js") || resourceName.endsWith("jquery.js"));
		}

		return (bootsFacesJQueryPluginJSResource || butterFacesJQueryPluginJSResource ||
				primeFacesJQueryPluginJSResource || richFacesJQueryPluginJSResource);
	}
}
