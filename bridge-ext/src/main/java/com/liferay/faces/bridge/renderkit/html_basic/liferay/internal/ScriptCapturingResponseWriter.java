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

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;

import com.liferay.faces.util.client.Script;
import com.liferay.faces.util.client.ScriptFactory;
import com.liferay.faces.util.factory.FactoryExtensionFinder;


/**
 * The purpose of this class is to capture the text of scripts and return a list of captured scripts via {@link
 * ScriptCapturingResponseWriter#getScripts()}. All other functionality is delegated to the wrapped {@link
 * ResponseWriter}, so it should be rendered normally.
 *
 * @author  Kyle Stiemann
 */
public class ScriptCapturingResponseWriter extends ResponseWriterWrapper {

	// Private Members
	private ResponseWriter wrappedResponseWriter;
	private boolean writingScript;
	private StringWriter scriptStringWriter;
	private List<Script> scripts;

	public ScriptCapturingResponseWriter(ResponseWriter wrappedResponseWriter) {
		this.wrappedResponseWriter = wrappedResponseWriter;
		scriptStringWriter = new StringWriter();
		scripts = new ArrayList<Script>();
	}

	@Override
	public void endElement(String name) throws IOException {

		if (writingScript && "script".equals(name)) {

			String scriptSource = scriptStringWriter.toString();
			ScriptFactory scriptFactory = (ScriptFactory) FactoryExtensionFinder.getFactory(ScriptFactory.class);
			Script script = scriptFactory.getScript(scriptSource);
			scripts.add(script);
			StringBuffer scriptStringWriterBuffer = scriptStringWriter.getBuffer();
			scriptStringWriterBuffer.setLength(0);
			writingScript = false;
		}
		else {
			super.endElement(name);
		}
	}

	@Override
	public void startElement(String name, UIComponent uiComponent) throws IOException {

		if ("script".equals(name)) {
			writingScript = true;
		}
		else {
			super.startElement(name, uiComponent);
		}
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {

		if (writingScript) {
			scriptStringWriter.write(cbuf, off, len);
		}
		else {
			super.write(cbuf, off, len);
		}
	}

	@Override
	public void writeAttribute(String name, Object value, String property) throws IOException {

		if (!writingScript) {
			super.writeAttribute(name, value, property);
		}
	}

	public List<Script> getScripts() {
		return Collections.unmodifiableList(scripts);
	}

	@Override
	public ResponseWriter getWrapped() {
		return wrappedResponseWriter;
	}
}
