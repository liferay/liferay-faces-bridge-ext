/**
 * Copyright (c) 2000-2018 Liferay, Inc. All rights reserved.
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

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.portlet.PortletSecurityException;

import com.liferay.faces.bridge.ext.util.internal.XMLUtil;


/**
 * @author  Neil Griffin
 */
public abstract class LiferayBaseURLFriendlyImpl extends LiferayBaseURLFriendlyCompatImpl implements LiferayBaseURL {

	// Protected Final Data Members
	protected final String encoding;

	// Private Data Members
	private String toStringValue;

	protected LiferayBaseURLFriendlyImpl(String encoding) {
		this.encoding = encoding;
	}

	@Override
	public void addProperty(String key, String value) {
		super.addProperty(key, value);
		resetToString();
	}

	@Override
	public void setParameter(String name, String[] values) {
		super.setParameter(name, values);
		resetToString();
	}

	@Override
	public void setParameters(Map<String, String[]> parameters) {
		super.setParameters(parameters);
		resetToString();
	}

	@Override
	public void setProperty(String key, String value) {
		super.setProperty(key, value);
		resetToString();
	}

	@Override
	public void setSecure(boolean secure) throws PortletSecurityException {
		super.setSecure(secure);
		resetToString();
	}

	@Override
	public String toString() {

		if (toStringValue == null) {
			LiferayURLGenerator liferayURLGenerator = getLiferayURLGenerator();
			toStringValue = liferayURLGenerator.generateURL(getParameterMap());
		}

		return toStringValue;
	}

	@Override
	public void write(Writer writer) throws IOException {
		write(writer, true);
	}

	@Override
	public void write(Writer writer, boolean escapeXML) throws IOException {

		String valueAsString = toString();

		if (escapeXML) {
			valueAsString = XMLUtil.escapeXML(valueAsString);
		}

		writer.write(valueAsString);
	}

	protected abstract LiferayURLGenerator getLiferayURLGenerator();

	protected void resetToString() {
		toStringValue = null;
	}
}
