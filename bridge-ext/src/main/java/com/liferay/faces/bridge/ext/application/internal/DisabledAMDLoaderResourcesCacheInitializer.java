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

import java.util.Map;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import com.liferay.faces.bridge.ext.config.internal.LiferayPortletConfigParam;
import com.liferay.faces.util.cache.Cache;
import com.liferay.faces.util.cache.CacheFactory;
import com.liferay.faces.util.config.ApplicationConfig;


/**
 * This class initializes the filtered resource cache for {@link ResourceHandlerLiferayImpl}. The initialization cannot
 * be performed in the ResourceHandlerLiferayImpl constructor since this {@link javax.faces.application.ResourceHandler}
 * are initialized before the {@link CacheFactory} has been created.
 *
 * @author  Kyle Stiemann
 */
public class DisabledAMDLoaderResourcesCacheInitializer implements SystemEventListener {

	@Override
	public boolean isListenerForSource(Object source) {
		return (source instanceof ApplicationConfig);
	}

	@Override
	public void processEvent(SystemEvent systemEvent) throws AbortProcessingException {

		FacesContext startupFacesContext = FacesContext.getCurrentInstance();

		if ((startupFacesContext != null) && !startupFacesContext.isProjectStage(ProjectStage.Development)) {

			ExternalContext externalContext = startupFacesContext.getExternalContext();
			Map<String, Object> applicationMap = externalContext.getApplicationMap();
			Cache<String, Resource> filteredResourceCache;
			int initialCacheCapacity = LiferayPortletConfigParam.DisabledAMDLoaderResourcesInitialCacheCapacity
				.getIntegerValue(externalContext);
			int maxCacheCapacity = LiferayPortletConfigParam.DisabledAMDLoaderResourcesMaxCacheCapacity.getIntegerValue(
					externalContext);

			if (maxCacheCapacity > -1) {
				filteredResourceCache = CacheFactory.getConcurrentLRUCacheInstance(externalContext,
						initialCacheCapacity, maxCacheCapacity);
			}
			else {
				filteredResourceCache = CacheFactory.getConcurrentCacheInstance(externalContext, initialCacheCapacity);
			}

			applicationMap.put(JSResourceWithDisabledAMDLoaderImpl.class.getName(), filteredResourceCache);
		}
	}
}
