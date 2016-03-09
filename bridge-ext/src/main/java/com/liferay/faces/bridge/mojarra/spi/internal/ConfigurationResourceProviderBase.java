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
package com.liferay.faces.bridge.mojarra.spi.internal;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletContext;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;

import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;

import com.sun.faces.spi.ConfigurationResourceProvider;


/**
 * @author  Kyle Stiemann
 */
public abstract class ConfigurationResourceProviderBase implements ConfigurationResourceProvider {

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(ConfigurationResourceProviderBase.class);

	protected Collection<URI> getResources(ServletContext servletContext, String resourceFilePattern) {

		Bundle portletBundle = FrameworkUtil.getBundle(ConfigurationResourceProviderBase.class);
		BundleWiring bundleWiring = portletBundle.adapt(BundleWiring.class);
		Collection<String> resourceFilePaths = bundleWiring.listResources("META-INF/", resourceFilePattern,
				BundleWiring.LISTRESOURCES_RECURSE);
		List<URI> resourceURIs = new ArrayList<URI>();

		if (!resourceFilePaths.isEmpty()) {

			ClassLoader classLoader = servletContext.getClassLoader();

			for (String resourceFilePath : resourceFilePaths) {

				try {

					URL resourceURL = classLoader.getResource(resourceFilePath);

					if (resourceURL != null) {

						URI resrouceURI = resourceURL.toURI();
						resourceURIs.add(resrouceURI);
					}
					else {
						logger.warn("URL for resource file path \"{0}\" is null.", resourceFilePath);
					}
				}
				catch (URISyntaxException e) {
					logger.error(e);
				}
			}
		}

		return resourceURIs;
	}
}
