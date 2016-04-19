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
import com.liferay.faces.bridge.WindowStateValidatorWrapper;

import com.liferay.portal.kernel.portlet.LiferayWindowState;


/**
 * @author  Neil Griffin
 */
public class WindowStateValidatorLiferayImpl extends WindowStateValidatorWrapper {

	// Private Constants
	private static final String[] LIFERAY_WINDOW_STATES = new String[] {
			LiferayWindowState.EXCLUSIVE.toString(), LiferayWindowState.POP_UP.toString()
		};

	// Private Data Members
	private WindowStateValidator wrappedWindowStateValidator;

	public WindowStateValidatorLiferayImpl(WindowStateValidator windowStateValidator) {
		this.wrappedWindowStateValidator = windowStateValidator;
	}

	@Override
	public boolean isValid(String windowState) {

		boolean valid = wrappedWindowStateValidator.isValid(windowState);

		if (!valid) {

			for (String curLiferayWindowState : LIFERAY_WINDOW_STATES) {

				if (curLiferayWindowState.equals(windowState)) {
					valid = true;

					break;
				}
			}
		}

		return valid;
	}

	@Override
	public WindowStateValidator getWrapped() {
		return wrappedWindowStateValidator;
	}
}
