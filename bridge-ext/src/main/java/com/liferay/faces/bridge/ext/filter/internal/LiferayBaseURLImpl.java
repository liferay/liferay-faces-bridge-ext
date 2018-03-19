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
import java.util.LinkedHashMap;
import java.util.Map;

import javax.portlet.PortletSecurityException;

import com.liferay.faces.bridge.ext.util.internal.XMLUtil;


/**
 * @author  Neil Griffin
 */
public abstract class LiferayBaseURLImpl extends LiferayBaseURLCompatImpl {

	// Private Data Members
	private LiferayURLGenerator liferayURLGenerator;
	private Map<String, String[]> parameterMap;

	public LiferayBaseURLImpl(LiferayURLGenerator liferayURLGenerator) {
		this.liferayURLGenerator = liferayURLGenerator;
		this.parameterMap = new LinkedHashMap<String, String[]>();
	}

	@Override
	public void addProperty(String key, String value) {
		// no-op
	}

	public LiferayURLGenerator getLiferayURLGenerator() {
		return liferayURLGenerator;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return parameterMap;
	}

	@Override
	public void setParameter(String name, String value) {
		parameterMap.put(name, new String[] { value });
		resetToString();
	}

	@Override
	public void setParameter(String name, String[] values) {
		parameterMap.put(name, values);
		resetToString();
	}

	@Override
	public void setParameters(Map<String, String[]> parameters) {
		parameterMap.putAll(parameters);
		resetToString();
	}

	@Override
	public void setProperty(String key, String value) {
		// no-op
	}

	@Override
	public void setSecure(boolean secure) throws PortletSecurityException {
		// no-op
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

	protected abstract void resetToString();
}
