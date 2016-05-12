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
package com.liferay.faces.bridge.ext.internal;

import com.liferay.faces.bridge.WindowStateValidator;
import com.liferay.faces.bridge.WindowStateValidatorFactory;


/**
 * @author  Neil Griffin
 */
public class WindowStateValidatorFactoryLiferayImpl extends WindowStateValidatorFactory {

	// Private Data Members
	private WindowStateValidatorFactory wrappedWindowStateValidatorFactory;

	public WindowStateValidatorFactoryLiferayImpl(WindowStateValidatorFactory windowStateValidatorFactory) {
		this.wrappedWindowStateValidatorFactory = windowStateValidatorFactory;
	}

	@Override
	public WindowStateValidator getWindowStateValidator() {

		WindowStateValidator wrappedWindowStateValidator = wrappedWindowStateValidatorFactory.getWindowStateValidator();

		return new WindowStateValidatorLiferayImpl(wrappedWindowStateValidator);
	}

	@Override
	public WindowStateValidatorFactory getWrapped() {
		return wrappedWindowStateValidatorFactory;
	}
}
