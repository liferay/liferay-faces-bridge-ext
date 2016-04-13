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
package com.liferay.faces.bridge.ext.mojarra.spi.internal;

import java.net.URI;
import java.util.Collection;

import javax.servlet.ServletContext;

import com.sun.faces.spi.FaceletConfigResourceProvider;


/**
 * This class implements the Mojarra {@link com.sun.faces.spi.ConfigurationResourceProvider} SPI in order to enable the
 * discovery of resources within the OSGi bundle that match the "*.taglib.xml" wildcard.
 *
 * @author  Kyle Stiemann
 */
public class FaceletConfigResourceProviderLiferayImpl extends ConfigurationResourceProviderBase
	implements FaceletConfigResourceProvider {

	/**
	 * Returns the list of resources matching the "*.taglib.xml" wildcard found within the OSGi bundle. For more
	 * information, see {@link com.sun.faces.spi.ConfigurationResourceProvider#getResources(ServletContext)}.
	 */
	@Override
	public Collection<URI> getResources(ServletContext servletContext) {
		return getResourcesPattern("*.taglib.xml");
	}
}
