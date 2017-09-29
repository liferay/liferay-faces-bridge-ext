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

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;


/**
 * @author  Kyle Stiemann
 */
public class ElementImpl extends NodeImpl implements Element {

	// Private Data Members
	private NamedNodeMapImpl attributes;
	private String nodeName;

	public ElementImpl(String nodeName, Node parentNode) {

		super(Node.ELEMENT_NODE, parentNode);
		this.attributes = new NamedNodeMapImpl();
		this.nodeName = nodeName;
	}

	@Override
	public String getAttribute(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attr getAttributeNode(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attr getAttributeNodeNS(String namespaceURI, String localName) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getAttributeNS(String namespaceURI, String localName) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public NamedNodeMap getAttributes() {
		return attributes;
	}

	@Override
	public NodeList getElementsByTagName(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public NodeList getElementsByTagNameNS(String namespaceURI, String localName) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getNodeName() {
		return nodeName;
	}

	@Override
	public TypeInfo getSchemaTypeInfo() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getTagName() {
		return getNodeName();
	}

	@Override
	public boolean hasAttribute(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasAttributeNS(String namespaceURI, String localName) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeAttribute(String name) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAttribute(String name, String value) throws DOMException {
		attributes.add(new AttrImpl(name, value, this));
	}

	@Override
	public Attr setAttributeNode(Attr newAttr) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setIdAttribute(String name, boolean isId) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException {
		throw new UnsupportedOperationException();
	}
}
