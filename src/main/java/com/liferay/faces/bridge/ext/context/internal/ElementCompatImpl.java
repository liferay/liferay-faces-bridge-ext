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
package com.liferay.faces.bridge.ext.context.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.render.Renderer;

import org.w3c.dom.Element;


/**
 * @author  Kyle Stiemann
 */
public abstract class ElementCompatImpl implements Element {

	protected Map<String, String> getAttributes(UIComponent uiComponent) {

		HashMap<String, String> attributes = new HashMap<String, String>();

		if (uiComponent != null) {

			Map<String, Object> passThroughAttributes = uiComponent.getPassThroughAttributes(false);

			if ((passThroughAttributes != null) && !passThroughAttributes.isEmpty()) {

				Set<Map.Entry<String, Object>> entrySet = passThroughAttributes.entrySet();

				for (Map.Entry<String, Object> attributeEntry : entrySet) {

					String attributeName = attributeEntry.getKey();

					if (!attributeName.equals(Renderer.PASSTHROUGH_RENDERER_LOCALNAME_KEY)) {

						Object attributeValue = attributeEntry.getValue();

						if (attributeValue == null) {
							attributeValue = "";
						}

						attributes.put(attributeName, attributeValue.toString());
					}
				}
			}
		}

		return attributes;
	}
}
