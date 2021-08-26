/**
 * Copyright (c) 2000-2021 Liferay, Inc. All rights reserved.
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

import org.junit.Assert;
import org.junit.Test;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;


/**
 * @author  Kyle Stiemann
 */
public class ElementTest {

	@Test
	public void testOverwriteElementAttribute() {

		Element element = new ElementImpl("script", null);
		element.setAttribute("id", "my_id");

		NamedNodeMap attributes = element.getAttributes();
		Assert.assertEquals("my_id", attributes.item(0).getNodeValue());
		element.setAttribute("id", "my_id_again");
		Assert.assertEquals(1, attributes.getLength());
		Assert.assertEquals("my_id_again", attributes.item(0).getNodeValue());
	}
}
