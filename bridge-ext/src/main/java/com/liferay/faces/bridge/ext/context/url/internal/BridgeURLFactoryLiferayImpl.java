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
package com.liferay.faces.bridge.ext.context.url.internal;

import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.portlet.faces.BridgeException;

import com.liferay.faces.bridge.BridgeURL;
import com.liferay.faces.bridge.BridgeURLFactory;


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
	public BridgeURL getBridgeActionURL(FacesContext facesContext, String uri) throws BridgeException {
		return wrappedBridgeURLFactory.getBridgeActionURL(facesContext, uri);
	}

	@Override
	public BridgeURL getBridgeBookmarkableURL(FacesContext facesContext, String uri,
		Map<String, List<String>> parameters) throws BridgeException {
		return wrappedBridgeURLFactory.getBridgeBookmarkableURL(facesContext, uri, parameters);
	}

	@Override
	public BridgeURL getBridgePartialActionURL(FacesContext facesContext, String uri) throws BridgeException {
		return wrappedBridgeURLFactory.getBridgePartialActionURL(facesContext, uri);
	}

	@Override
	public BridgeURL getBridgeRedirectURL(FacesContext facesContext, String uri,
		Map<String, List<String>> parameters) throws BridgeException {

		BridgeURL wrappedBridgeRedirectURL = wrappedBridgeURLFactory.getBridgeRedirectURL(facesContext, uri,
				parameters);

		return new BridgeRedirectURLLiferayImpl(wrappedBridgeRedirectURL);
	}

	@Override
	public BridgeURL getBridgeResourceURL(FacesContext facesContext, String uri) throws BridgeException {
		return wrappedBridgeURLFactory.getBridgeResourceURL(facesContext, uri);
	}

	@Override
	public BridgeURLFactory getWrapped() {
		return wrappedBridgeURLFactory;
	}
}
