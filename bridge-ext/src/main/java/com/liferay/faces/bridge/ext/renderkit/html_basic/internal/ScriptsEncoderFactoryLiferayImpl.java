/**
 * Copyright (c) 2000-2022 Liferay, Inc. All rights reserved.
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
package com.liferay.faces.bridge.ext.renderkit.html_basic.internal;

import java.io.Serializable;

import com.liferay.faces.util.client.ScriptsEncoder;
import com.liferay.faces.util.client.ScriptsEncoderFactory;


/**
 * @author  Neil Griffin
 */
public class ScriptsEncoderFactoryLiferayImpl extends ScriptsEncoderFactory implements Serializable {

	// serialVersionUID
	private static final long serialVersionUID = 6500965768943686133L;

	// Private Final Data Members
	private final ScriptsEncoder scriptsEncoder = new ScriptsEncoderLiferayImpl();
	private final ScriptsEncoderFactory wrappedScriptsEncoderFactory;

	public ScriptsEncoderFactoryLiferayImpl(ScriptsEncoderFactory ScriptsEncoderFactory) {
		this.wrappedScriptsEncoderFactory = ScriptsEncoderFactory;
	}

	@Override
	public ScriptsEncoder getScriptsEncoder() {
		return scriptsEncoder;
	}

	@Override
	public ScriptsEncoderFactory getWrapped() {
		return wrappedScriptsEncoderFactory;
	}
}
