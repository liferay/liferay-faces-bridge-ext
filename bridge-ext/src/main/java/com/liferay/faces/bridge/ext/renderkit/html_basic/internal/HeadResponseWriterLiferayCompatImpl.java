/**
 * Copyright (c) 2000-2022 Liferay, Inc. All rights reserved.
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

import javax.faces.context.ResponseWriter;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


/**
 * @author  Kyle Stiemann
 */
public abstract class HeadResponseWriterLiferayCompatImpl extends HeadResponseWriterBase {

	public HeadResponseWriterLiferayCompatImpl(ResponseWriter wrappedResponseWriter) {
		super(wrappedResponseWriter);
	}

	public ServletContext getServletContext(HttpServletRequest httpServletRequest) {
		return httpServletRequest.getServletContext();
	}
}
