/**
 * Copyright (c) 2000-2015 Liferay, Inc. All rights reserved.
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

import com.liferay.faces.bridge.context.BridgeContext;
import com.liferay.faces.bridge.context.url.BridgeResourceURL;
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
	public BridgeURL getBridgeActionURL(BridgeContext bridgeContext, BridgeURI bridgeURI, String viewId) {
		return wrappedBridgeURLFactory.getBridgeActionURL(bridgeContext, bridgeURI, viewId);
	}

	@Override
	public BridgeURL getBridgeBookmarkableURL(BridgeContext bridgeContext, BridgeURI bridgeURI,
		Map<String, List<String>> parameters, String viewId) {
		return wrappedBridgeURLFactory.getBridgeBookmarkableURL(bridgeContext, bridgeURI, parameters, viewId);
	}

	@Override
	public BridgeURL getBridgePartialActionURL(BridgeContext bridgeContext, BridgeURI bridgeURI, String viewId) {
		return wrappedBridgeURLFactory.getBridgePartialActionURL(bridgeContext, bridgeURI, viewId);
	}

	@Override
	public BridgeURL getBridgeRedirectURL(BridgeContext bridgeContext, BridgeURI bridgeURI,
		Map<String, List<String>> parameters, String redirectViewId) {

		BridgeURL wrappedBridgeRedirectURL = wrappedBridgeURLFactory.getBridgeRedirectURL(bridgeContext, bridgeURI,
				parameters, redirectViewId);

		return new BridgeRedirectURLLiferayImpl(bridgeContext, bridgeURI, parameters, redirectViewId,
				wrappedBridgeRedirectURL);
	}

	@Override
	public BridgeResourceURL getBridgeResourceURL(BridgeContext bridgeContext, BridgeURI bridgeURI, String viewId) {
		return wrappedBridgeURLFactory.getBridgeResourceURL(bridgeContext, bridgeURI, viewId);
	}

	@Override
	public BridgeURLFactory getWrapped() {
		return wrappedBridgeURLFactory;
	}
}
