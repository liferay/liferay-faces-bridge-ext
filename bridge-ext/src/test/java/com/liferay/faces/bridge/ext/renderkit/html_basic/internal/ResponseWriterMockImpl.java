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
package com.liferay.faces.bridge.ext.renderkit.html_basic.internal;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;


/**
 * @author  Kyle Stiemann
 */
public class ResponseWriterMockImpl extends ResponseWriter {

	// Private Data Members
	private StringWriter stringWriter;

	public ResponseWriterMockImpl(Writer writer) {
		this.stringWriter = (StringWriter) writer;
	}

	@Override
	public ResponseWriter cloneWithWriter(Writer writer) {
		return new ResponseWriterMockImpl(writer);
	}

	@Override
	public void close() throws IOException {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void endDocument() throws IOException {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void endElement(String name) throws IOException {
		// no-op
	}

	@Override
	public void flush() throws IOException {
		throw new UnsupportedOperationException("");
	}

	@Override
	public String getCharacterEncoding() {
		throw new UnsupportedOperationException("");
	}

	@Override
	public String getContentType() {
		throw new UnsupportedOperationException("");
	}

	public void resetStringWriter() {
		stringWriter.getBuffer().setLength(0);
	}

	@Override
	public void startDocument() throws IOException {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void startElement(String name, UIComponent component) throws IOException {

		stringWriter.append("<");
		stringWriter.append(name);
	}

	@Override
	public String toString() {
		return stringWriter.toString();
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void writeAttribute(String name, Object value, String property) throws IOException {

		stringWriter.append(" ");
		stringWriter.append(name);
		stringWriter.append("=\"");
		stringWriter.append((String) value);
		stringWriter.append("\"");
	}

	@Override
	public void writeComment(Object comment) throws IOException {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void writeText(Object text, String property) throws IOException {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void writeText(char[] text, int off, int len) throws IOException {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void writeURIAttribute(String name, Object value, String property) throws IOException {
		throw new UnsupportedOperationException("");
	}
}
