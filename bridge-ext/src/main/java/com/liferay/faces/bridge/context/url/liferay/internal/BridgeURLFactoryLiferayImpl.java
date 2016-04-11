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
package com.liferay.faces.bridge.context.url.liferay.internal;

import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import com.liferay.faces.bridge.context.url.BridgeURI;
import com.liferay.faces.bridge.context.url.BridgeURL;
import com.liferay.faces.bridge.context.url.BridgeURLFactory;


/**
 * @author  Neil Griffin
 */
public class BridgeURLFactoryLiferayImpl extends BridgeURLFactory {

	// Private Data Members
	private BridgeURLFactory wrappedBridgeURLFactory;

	public BridgeURLFactoryLiferayImpl(BridgeURLFactory bridgeURLFactory) {
		this.wrappedBridgeURLFactory = bridgeURLFactory;
	}

	@Override
	public BridgeURL getBridgeActionURL(FacesContext facesContext, BridgeURI bridgeURI, String viewId) {
		return wrappedBridgeURLFactory.getBridgeActionURL(facesContext, bridgeURI, viewId);
	}

	@Override
	public BridgeURL getBridgeBookmarkableURL(FacesContext facesContext, BridgeURI bridgeURI,
		Map<String, List<String>> parameters, String viewId) {
		return wrappedBridgeURLFactory.getBridgeBookmarkableURL(facesContext, bridgeURI, parameters, viewId);
	}

	@Override
	public BridgeURL getBridgePartialActionURL(FacesContext facesContext, BridgeURI bridgeURI, String viewId) {
		return wrappedBridgeURLFactory.getBridgePartialActionURL(facesContext, bridgeURI, viewId);
	}

	@Override
	public BridgeURL getBridgeRedirectURL(FacesContext facesContext, BridgeURI bridgeURI,
		Map<String, List<String>> parameters, String viewId) {

		BridgeURL wrappedBridgeRedirectURL = wrappedBridgeURLFactory.getBridgeRedirectURL(facesContext, bridgeURI,
				parameters, viewId);

		return new BridgeRedirectURLLiferayImpl(wrappedBridgeRedirectURL, facesContext, viewId);
	}

	@Override
	public BridgeURL getBridgeResourceURL(FacesContext facesContext, BridgeURI bridgeURI, String viewId) {
		return wrappedBridgeURLFactory.getBridgeResourceURL(facesContext, bridgeURI, viewId);
	}

	@Override
	public BridgeURLFactory getWrapped() {
		return wrappedBridgeURLFactory;
	}
}
