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

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;


/**
 * @author  Kyle Stiemann
 */
/* package-private */ class AttrImpl extends NodeImpl implements Attr {

	// Private Data Members
	private String name;
	private String value;

	public AttrImpl(String name, String value, Node parentNode) {

		super(Node.ATTRIBUTE_NODE, parentNode);
		this.name = name;
		this.value = value;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getNodeName() {
		return getName();
	}

	@Override
	public String getNodeValue() throws DOMException {
		return getValue();
	}

	@Override
	public Element getOwnerElement() {
		return (Element) getParentNode();
	}

	@Override
	public TypeInfo getSchemaTypeInfo() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getSpecified() {
		return true;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public boolean isId() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValue(String value) throws DOMException {
		this.value = value;
	}
}
