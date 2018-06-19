/**
 * Copyright (c) 2000-2019 Liferay, Inc. All rights reserved.
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
package com.liferay.faces.bridge.ext;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.faces.GenericFacesPortlet;

import org.osgi.annotation.versioning.ConsumerType;

import com.liferay.portal.kernel.portlet.LiferayPortletMode;


/**
 * This class provides useful extensions to the {@link GenericFacesPortlet} found in the Bridge API.
 *
 * @author  Neil Griffin
 */
@ConsumerType
public class GenericLiferayFacesPortlet extends GenericFacesPortlet {

	@Override
	protected void doDispatch(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException,
		IOException {

		PortletMode portletMode = renderRequest.getPortletMode();

		if (portletMode.equals(PortletMode.VIEW) || portletMode.equals(PortletMode.EDIT) ||
				portletMode.equals(PortletMode.HELP)) {
			super.doDispatch(renderRequest, renderResponse);
		}
		else if (portletMode.equals(LiferayPortletMode.ABOUT)) {
			doView(renderRequest, renderResponse);
		}
		else if (portletMode.equals(LiferayPortletMode.CONFIG)) {
			doView(renderRequest, renderResponse);
		}
		else if (portletMode.equals(LiferayPortletMode.EDIT_DEFAULTS)) {
			doView(renderRequest, renderResponse);
		}
		else if (portletMode.equals(LiferayPortletMode.EDIT_GUEST)) {
			doView(renderRequest, renderResponse);
		}
		else if (portletMode.equals(LiferayPortletMode.PREVIEW)) {
			doView(renderRequest, renderResponse);
		}
		else if (portletMode.equals(LiferayPortletMode.PRINT)) {
			doView(renderRequest, renderResponse);
		}
		else {
			super.doDispatch(renderRequest, renderResponse);
		}
	}
}
