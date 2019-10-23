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
package com.liferay.faces.bridge.ext.renderkit.html_basic.internal;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


/**
 * @author  Kyle Stiemann
 */
/* package-private */ class NamedNodeMapImpl extends LinkedHashMap<String, Attr> implements NamedNodeMap {

	// serialVersionUID
	private static final long serialVersionUID = 3793547295675017484L;

	private Iterator<Attr> iterator;
	private int iteratorIndex = 0;

	@Override
	public int getLength() {

		if (iterator == null) {

			Collection<Attr> values = values();
			iterator = values.iterator();
			iteratorIndex = 0;
		}

		return size();
	}

	@Override
	public Node getNamedItem(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node getNamedItemNS(String namespaceURI, String localName) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node item(int index) {

		if (index < 0) {
			throw new IndexOutOfBoundsException("Invalid negative index: " + index);
		}

		if (index != iteratorIndex) {
			throw new UnsupportedOperationException("Random access is not supported. Please iterate in order.");
		}

		if ((index == 0) && (iterator == null)) {

			Collection<Attr> values = values();
			iterator = values.iterator();
		}

		if (!iterator.hasNext()) {
			throw new IndexOutOfBoundsException("Invalid index: " + index);
		}

		Attr node = iterator.next();

		if (iterator.hasNext()) {
			iteratorIndex++;
		}
		else {

			iterator = null;
			iteratorIndex = 0;
		}

		return node;
	}

	@Override
	public Node removeNamedItem(String name) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node setNamedItem(Node arg) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node setNamedItemNS(Node arg) throws DOMException {
		throw new UnsupportedOperationException();
	}
}
