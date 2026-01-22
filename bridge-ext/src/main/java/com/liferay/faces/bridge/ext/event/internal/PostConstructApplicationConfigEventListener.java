/**
 * Copyright (c) 2000-2025 Liferay, Inc. All rights reserved.
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
package com.liferay.faces.bridge.ext.event.internal;

import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.SystemEvent;
import jakarta.faces.event.SystemEventListener;

import com.liferay.faces.bridge.ext.application.internal.JSResourceWithDisabledAMDLoaderImpl;
import com.liferay.faces.bridge.ext.internal.BridgeExtDependencyVerifier;
import com.liferay.faces.util.config.ApplicationConfig;


/**
 * @author  Kyle Stiemann
 */
public class PostConstructApplicationConfigEventListener implements SystemEventListener {

	@Override
	public boolean isListenerForSource(Object source) {
		return source instanceof ApplicationConfig;
	}

	@Override
	public void processEvent(SystemEvent systemEvent) throws AbortProcessingException {

		FacesContext startupFacesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = startupFacesContext.getExternalContext();
		BridgeExtDependencyVerifier.verify(externalContext);
		JSResourceWithDisabledAMDLoaderImpl.initFilteredResourceCache(startupFacesContext);
	}
}
