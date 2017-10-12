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
package com.liferay.faces.bridge.ext.mojarra.spi.internal;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
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

	// Private Constants
	private static final boolean FRAMEWORK_UTIL_DETECTED;

	static {

		boolean frameworkUtilDetected = false;

		try {

			Class.forName("org.osgi.framework.FrameworkUtil");
			frameworkUtilDetected = true;
		}
		catch (Throwable t) {

			if (!((t instanceof NoClassDefFoundError) || (t instanceof ClassNotFoundException))) {

				logger.error("An unexpected error occurred when attempting to detect OSGi:");
				logger.error(t);
			}
		}

		FRAMEWORK_UTIL_DETECTED = frameworkUtilDetected;
	}

	@Override
	public abstract Collection<URI> getResources(ServletContext context);

	protected Collection<URI> getResourcesPattern(String resourceFilePattern) {

		List<URI> resourceURIs;

		if (FRAMEWORK_UTIL_DETECTED) {

			Bundle portletBundle = FrameworkUtil.getBundle(ConfigurationResourceProviderBase.class);
			BundleWiring bundleWiring = portletBundle.adapt(BundleWiring.class);
			Collection<String> resourceFilePaths = bundleWiring.listResources("META-INF/", resourceFilePattern,
					BundleWiring.LISTRESOURCES_RECURSE);
			resourceURIs = new ArrayList<URI>();

			for (String resourceFilePath : resourceFilePaths) {

				Enumeration<URL> resourceURLs = null;

				try {

					// FACES-2650 Because there may be multiple jars in our bundle, some resources may have exactly the
					// same reourceFilePath. We need to find all the resources with this resourceFilePath in all jars.
					resourceURLs = portletBundle.getResources(resourceFilePath);
				}
				catch (IOException ioe) {
					logger.error(ioe);
				}

				if (resourceURLs != null) {

					while (resourceURLs.hasMoreElements()) {

						try {

							URL resourceURL = resourceURLs.nextElement();

							if (resourceURL != null) {

								URI resourceURI = resourceURL.toURI();
								resourceURIs.add(resourceURI);
							}
							else {
								logger.warn("URL for resourceFilePath=[{0}] is null.", resourceFilePath);
							}
						}
						catch (URISyntaxException e) {
							logger.error(e);
						}
					}
				}
			}
		}
		else {

			// FACES-3233 Bridge Ext not working outside OSGI context
			resourceURIs = Collections.<URI>emptyList();
		}

		return resourceURIs;
	}
}
