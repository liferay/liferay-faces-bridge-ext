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
package com.liferay.faces.bridge.ext.renderkit.html_basic.internal;

import com.liferay.faces.util.client.ScriptsEncoder;
import com.liferay.faces.util.client.ScriptsEncoderFactory;


/**
 * @author  Neil Griffin
 */
public class ScriptsEncoderFactoryLiferayImpl extends ScriptsEncoderFactory {

	// Private Data Members
	private ScriptsEncoderFactory wrappedScriptsEncoderFactory;

	public ScriptsEncoderFactoryLiferayImpl(ScriptsEncoderFactory ScriptsEncoderFactory) {
		this.wrappedScriptsEncoderFactory = ScriptsEncoderFactory;
	}

	@Override
	public ScriptsEncoder getScriptsEncoder() {
		return new ScriptsEncoderLiferayImpl();
	}

	@Override
	public ScriptsEncoderFactory getWrapped() {
		return wrappedScriptsEncoderFactory;
	}
}
