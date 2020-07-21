/**
 * Copyright (c) 2000-2020 Liferay, Inc. All rights reserved.
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
package com.liferay.faces.bridge.ext.i18n.internal;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.liferay.faces.util.i18n.I18n;
import com.liferay.faces.util.i18n.I18nUtil;
import com.liferay.faces.util.i18n.I18nWrapper;

import com.liferay.portal.kernel.language.LanguageUtil;


/**
 * @author  Neil Griffin
 */
public class I18nLiferayImpl extends I18nWrapper implements Serializable {

	// serialVersionUID
	private static final long serialVersionUID = 2328392557312272111L;

	// Private Final Data Members
	private final I18n wrappedI18n;

	public I18nLiferayImpl(I18n i18n) {
		this.wrappedI18n = i18n;
	}

	@Override
	public FacesMessage getFacesMessage(FacesContext facesContext, Locale locale, FacesMessage.Severity severity,
		String messageId) {
		return I18nUtil.getFacesMessage(this, facesContext, locale, severity, messageId);
	}

	@Override
	public FacesMessage getFacesMessage(FacesContext facesContext, Locale locale, FacesMessage.Severity severity,
		String messageId, Object... arguments) {
		return I18nUtil.getFacesMessage(this, facesContext, locale, severity, messageId, arguments);
	}

	@Override
	public String getMessage(FacesContext facesContext, Locale locale, String messageId) {

		String value = LanguageUtil.get(locale, messageId);

		if ((value == null) || value.equals(messageId)) {
			value = super.getMessage(facesContext, locale, messageId);
		}

		return value;
	}

	@Override
	public String getMessage(FacesContext facesContext, Locale locale, String messageId, Object... arguments) {

		String value = LanguageUtil.format(locale, messageId, arguments);

		if ((value == null) || value.equals(messageId)) {
			value = super.getMessage(facesContext, locale, messageId, arguments);
		}

		return value;
	}

	@Override
	public I18n getWrapped() {
		return wrappedI18n;
	}
}
