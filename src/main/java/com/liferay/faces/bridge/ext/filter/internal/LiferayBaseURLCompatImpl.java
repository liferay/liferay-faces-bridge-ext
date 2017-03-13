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
package com.liferay.faces.bridge.ext.filter.internal;

import java.io.IOException;

import javax.portlet.PortletMode;
import javax.portlet.RenderParameters;
import javax.portlet.WindowState;

import com.liferay.portal.kernel.util.HtmlUtil;


/**
 * This class provides a compatibility layer that isolates differences between different versions of the Portlet API.
 *
 * @author  Neil Griffin
 */
public abstract class LiferayBaseURLCompatImpl implements LiferayBaseURL {

	@Override
	public Appendable append(Appendable out) throws IOException {
		return append(out, true);
	}

	@Override
	public Appendable append(Appendable out, boolean escapeXML) throws IOException {

		if (escapeXML) {
			return out.append(HtmlUtil.escape(toString()));
		}
		else {
			return out.append(toString());
		}
	}

	@Override
	public PortletMode getPortletMode() {
		return null; // no-op
	}

	@Override
	public RenderParameters getRenderParameters() {
		return null; // no-op
	}

	@Override
	public WindowState getWindowState() {
		return null; // no-op
	}
}
