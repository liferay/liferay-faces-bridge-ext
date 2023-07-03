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
package com.liferay.faces.bridge.ext.filter.internal;

import javax.portlet.MimeResponse;
import javax.portlet.PortletRequest;

import com.liferay.faces.util.helper.Wrapper;


/**
 * @author  Neil Griffin
 */
public abstract class LiferayURLFactory implements Wrapper<LiferayURLFactory> {

	public abstract LiferayActionURL getLiferayActionURL(PortletRequest portletRequest, MimeResponse mimeResponse,
		boolean friendlyURLMapperEnabled);

	public abstract LiferayRenderURL getLiferayRenderURL(PortletRequest portletRequest, MimeResponse mimeResponse,
		boolean friendlyURLMapperEnabled);

	public abstract LiferayResourceURL getLiferayResourceURL(PortletRequest portletRequest, MimeResponse mimeResponse,
		boolean friendlyURLMapperEnabled);
}
