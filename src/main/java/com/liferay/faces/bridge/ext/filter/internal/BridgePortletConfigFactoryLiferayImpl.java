/**
 * Copyright (c) 2000-2017 Liferay, Inc. All rights reserved.
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
package com.liferay.faces.bridge.ext.filter.internal;

import com.liferay.faces.bridge.filter.BridgePortletConfigFactory;

import java.io.Serializable;

import javax.portlet.PortletConfig;


/**
 * @author  Neil Griffin
 */
public class BridgePortletConfigFactoryLiferayImpl extends BridgePortletConfigFactory implements Serializable {

	// serialVersionUID
	private static final long serialVersionUID = 1452826393887411234L;

	// Private Data Members
	private BridgePortletConfigFactory wrappedBridgePortletConfigFactory;

	public BridgePortletConfigFactoryLiferayImpl(BridgePortletConfigFactory bridgePortletConfigFactory) {
		this.wrappedBridgePortletConfigFactory = bridgePortletConfigFactory;
	}

	@Override
	public PortletConfig getPortletConfig(PortletConfig portletContext) {

		PortletConfig wrappedPortletConfig = wrappedBridgePortletConfigFactory.getPortletConfig(portletContext);

		return new BridgePortletConfigLiferayImpl(wrappedPortletConfig);
	}

	@Override
	public BridgePortletConfigFactory getWrapped() {
		return wrappedBridgePortletConfigFactory;
	}
}
