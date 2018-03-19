/**
 * Copyright (c) 2000-2018 Liferay, Inc. All rights reserved.
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
package com.liferay.faces.bridge.ext.internal;

import javax.portlet.faces.Bridge;
import javax.portlet.faces.BridgeURLFactory;


/**
 * @author  Kyle Stiemann
 */
public abstract class BridgeURLFactoryLiferayCompatImpl extends BridgeURLFactory {

	public static boolean isHeaderOrRenderOrResourcePhase(Bridge.PortletPhase portletPhase) {
		return (Bridge.PortletPhase.HEADER_PHASE.equals(portletPhase) ||
				Bridge.PortletPhase.RENDER_PHASE.equals(portletPhase) ||
				Bridge.PortletPhase.RESOURCE_PHASE.equals(portletPhase));
	}
}
