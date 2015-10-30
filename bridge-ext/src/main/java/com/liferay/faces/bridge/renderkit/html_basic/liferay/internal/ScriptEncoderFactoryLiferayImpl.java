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
package com.liferay.faces.bridge.renderkit.html_basic.liferay.internal;

import javax.faces.context.FacesContext;

import com.liferay.faces.util.client.ScriptEncoder;
import com.liferay.faces.util.client.ScriptEncoderFactory;


/**
 * @author  Neil Griffin
 */
public class ScriptEncoderFactoryLiferayImpl extends ScriptEncoderFactory {

	// Private Data Members
	private ScriptEncoderFactory wrappedScriptEncoderFactory;

	public ScriptEncoderFactoryLiferayImpl(ScriptEncoderFactory scriptEncoderFactory) {
		this.wrappedScriptEncoderFactory = scriptEncoderFactory;
	}

	@Override
	public ScriptEncoder getScriptEncoder() {

		ScriptEncoder scriptEncoder;
		FacesContext facesContext = FacesContext.getCurrentInstance();

		if (facesContext.getPartialViewContext().isAjaxRequest()) {
			scriptEncoder = new ScriptEncoderLiferayAjaxImpl();
		}
		else {
			scriptEncoder = new ScriptEncoderLiferayImpl();
		}

		return scriptEncoder;
	}

	@Override
	public ScriptEncoderFactory getWrapped() {
		return wrappedScriptEncoderFactory;
	}
}
