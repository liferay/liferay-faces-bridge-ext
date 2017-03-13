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
package com.liferay.faces.bridge.ext.lifecycle.internal;

import javax.faces.context.FacesContext;
import javax.faces.lifecycle.ClientWindow;
import javax.faces.lifecycle.ClientWindowFactory;


/**
 * @author  Neil Griffin
 */
public class ClientWindowFactoryLiferayImpl extends ClientWindowFactory {

	// Private Data Members
	private ClientWindowFactory wrappedClientWindowFactory;

	public ClientWindowFactoryLiferayImpl(ClientWindowFactory clientWindowFactory) {
		this.wrappedClientWindowFactory = clientWindowFactory;
	}

	@Override
	public ClientWindow getClientWindow(FacesContext facesContext) {

		ClientWindow wrappedClientWindow = wrappedClientWindowFactory.getClientWindow(facesContext);

		return new ClientWindowLiferayImpl(wrappedClientWindow);
	}

	@Override
	public ClientWindowFactory getWrapped() {
		return wrappedClientWindowFactory;
	}
}
