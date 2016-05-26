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
package com.liferay.faces.bridge.ext.filter.internal;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.PortletSecurityException;
import javax.portlet.RenderParameters;
import javax.portlet.WindowState;


/**
 * @author  Neil Griffin
 */
public abstract class LiferayBaseURLImpl implements LiferayBaseURL {

	// Private Data Members
	private LiferayURLGenerator liferayURLGenerator;
	private Map<String, String[]> parameterMap;

	public LiferayBaseURLImpl(LiferayURLGenerator liferayURLGenerator) {
		this.liferayURLGenerator = liferayURLGenerator;
		this.parameterMap = new LinkedHashMap<String, String[]>();
	}

	public void addProperty(String key, String value) {
		// no-op
	}

	@Override
	public Appendable append(Appendable out) throws IOException {
		return append(out, true);
	}

	@Override
	public Appendable append(Appendable out, boolean escapeXML) throws IOException {

		if (escapeXML) {
			return out.append(escapeXML(toString()));
		}
		else {
			return out.append(toString());
		}
	}

	public LiferayURLGenerator getLiferayURLGenerator() {
		return liferayURLGenerator;
	}

	public Map<String, String[]> getParameterMap() {
		return parameterMap;
	}

	@Override
	public PortletMode getPortletMode() {
		return null; // no-op
	}

	@Override
	public RenderParameters getRenderParameters() {
		return null; // no-op
	}

	@Override
	public WindowState getWindowState() {
		return null; // no-op
	}

	public void setParameter(String name, String value) {
		parameterMap.put(name, new String[] { value });
		resetToString();
	}

	public void setParameter(String name, String[] values) {
		parameterMap.put(name, values);
		resetToString();
	}

	public void setParameters(Map<String, String[]> parameters) {
		parameterMap.putAll(parameters);
		resetToString();
	}

	public void setProperty(String key, String value) {
		// no-op
	}

	public void setSecure(boolean secure) throws PortletSecurityException {
		// no-op
	}

	public void write(Writer writer) throws IOException {
		writer.write(toString());
	}

	public void write(Writer writer, boolean escapeXML) throws IOException {

		String valueAsString = toString();

		if (escapeXML) {

			valueAsString = escapeXML(valueAsString);
		}

		writer.write(valueAsString);
	}

	protected abstract void resetToString();

	private String escapeXML(String uri) {

		char[] tokens = new char[] { '<', '>', '&', '"', '\'', '\u00bb', '\u2013', '\u2014' };
		String[] replacements = new String[] {
				"&lt;", "&gt;", "&amp;", "&#034;", "&#039;", "&#187;", "&#x2013;", "&#x2014;"
			};

		for (int i = 0; i < tokens.length; i++) {
			String token = new String(new char[] { tokens[i] });
			String replacement = replacements[i];
			uri = uri.replace(token, replacement);
		}

		return uri;
	}
}
