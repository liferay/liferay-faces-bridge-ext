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

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

import org.w3c.dom.Node;

import com.liferay.faces.bridge.ext.util.internal.XMLUtil;


/**
 * @author  Kyle Stiemann
 */
public class HeadResponseWriterLiferayMockImpl extends HeadResponseWriterLiferayImpl {

	// Private Data Members
	private String nodeAsString;

	public HeadResponseWriterLiferayMockImpl(ResponseWriter wrappedResponseWriter) {
		super(wrappedResponseWriter);
	}

	@Override
	protected void writeNodeToHeadSection(Node node, UIComponent componentResource) throws IOException {

		if (isElement(node)) {
			nodeAsString = XMLUtil.elementToString(node, false);
		}
		else {
			nodeAsString = XMLUtil.nodeToString(node);
		}
	}

	/* package-private */ String getLastNodeAsString() {
		return nodeAsString;
	}
}
