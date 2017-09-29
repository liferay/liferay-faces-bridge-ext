/**
 * Copyright (c) 2000-2017 Liferay, Inc. All rights reserved.
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

import java.util.ArrayList;

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


/**
 * @author  Kyle Stiemann
 */
public class NamedNodeMapImpl extends ArrayList<AttrImpl> implements NamedNodeMap {

	// serialVersionUID
	private static final long serialVersionUID = 3793547295675017484L;

	@Override
	public int getLength() {
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
		return get(index);
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
