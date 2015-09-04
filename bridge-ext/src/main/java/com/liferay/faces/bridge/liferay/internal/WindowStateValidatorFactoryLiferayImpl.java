package com.liferay.faces.bridge.liferay.internal;


import com.liferay.faces.bridge.WindowStateValidator;
import com.liferay.faces.bridge.WindowStateValidatorFactory;


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
