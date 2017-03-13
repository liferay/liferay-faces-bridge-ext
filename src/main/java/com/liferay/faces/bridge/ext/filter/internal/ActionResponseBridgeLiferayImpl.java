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
package com.liferay.faces.bridge.ext.filter.internal;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.filter.ActionResponseWrapper;


/**
 * @author  Neil Griffin
 */
public class ActionResponseBridgeLiferayImpl extends ActionResponseWrapper {

	// Private Data Members
	private String namespace;
	private String namespaceWSRP;

	public ActionResponseBridgeLiferayImpl(ActionResponse response) {
		super(response);
	}

	@Override
	public String getNamespace() {

		if (namespace == null) {

			namespace = super.getNamespace();

			if (namespace.startsWith("wsrp_rewrite")) {

				namespace = getNamespaceWSRP();
			}
		}

		return namespace;
	}

	protected String getNamespaceWSRP() {

		if (namespaceWSRP == null) {

			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			PortletRequest portletRequest = (PortletRequest) externalContext.getRequest();
			namespaceWSRP = LiferayPortalUtil.getPortletId(portletRequest);
		}

		return namespaceWSRP;
	}
}
