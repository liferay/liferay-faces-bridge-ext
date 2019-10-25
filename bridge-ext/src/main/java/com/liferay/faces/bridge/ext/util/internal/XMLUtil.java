/**
 * Copyright (c) 2000-2019 Liferay, Inc. All rights reserved.
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
package com.liferay.faces.bridge.ext.util.internal;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.liferay.portal.kernel.util.HtmlUtil;


/**
 * @author  Kyle Stiemann
 */
public final class XMLUtil {

	private XMLUtil() {
		throw new AssertionError();
	}

	public static String elementToString(Node element) {
		return elementToString(element, true);
	}

	public static String elementToString(Node element, boolean escapeAttributeValues) {

		StringBuilder buf = new StringBuilder();
		appendElementRecurse(buf, element, escapeAttributeValues);

		return buf.toString();
	}

	public static String escapeXML(String text) {
		return HtmlUtil.escape(text);
	}

	public static String nodeToString(Node node) {

		String textContent = node.getTextContent();
		StringBuilder buf = new StringBuilder();

		if (textContent != null) {

			short nodeType = node.getNodeType();

			switch (nodeType) {

			case Node.TEXT_NODE: {
				buf.append(textContent);

				break;
			}

			case Node.COMMENT_NODE: {
				buf.append("<!--");
				buf.append(textContent);
				buf.append("-->");

				break;
			}

			case Node.CDATA_SECTION_NODE: {
				buf.append("<![CDATA[");
				buf.append(textContent);
				buf.append("]]>");

				break;
			}

			case Node.ELEMENT_NODE: {
				throw new IllegalArgumentException("Call XMLUtil.elementToString() to obtain an element as a string.");
			}

			default: {
				throw new IllegalArgumentException("Node type [" + nodeType + "] not supported.");
			}
			}
		}

		return buf.toString();
	}

	private static void appendElementRecurse(StringBuilder buf, Node element, boolean escapeAttributeValues) {

		buf.append("<");

		String nodeName = element.getNodeName();
		buf.append(nodeName);

		NamedNodeMap attributes = element.getAttributes();

		for (int i = 0; i < attributes.getLength(); i++) {

			Node attribute = attributes.item(i);
			buf.append(" ");
			buf.append(attribute.getNodeName());
			buf.append("=\"");

			String nodeValue = attribute.getNodeValue();

			if (nodeValue != null) {

				if (escapeAttributeValues) {
					buf.append(escapeXML(nodeValue));
				}
				else {
					buf.append(nodeValue);
				}
			}

			buf.append("\"");
		}

		buf.append(">");

		if (!nodeName.equals("link")) {

			if (element.hasChildNodes()) {

				NodeList childNodes = element.getChildNodes();

				for (int i = 0; i < childNodes.getLength(); i++) {

					Node node = childNodes.item(i);
					short nodeType = node.getNodeType();

					if (nodeType == Node.ELEMENT_NODE) {
						appendElementRecurse(buf, node, escapeAttributeValues);
					}
					else {
						buf.append(nodeToString(node));
					}
				}
			}
			else {

				String textContent = element.getTextContent();

				if (textContent != null) {
					buf.append(textContent);
				}
			}

			buf.append("</");
			buf.append(nodeName);
			buf.append(">");
		}
	}
}
