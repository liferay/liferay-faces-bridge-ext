/**
 * Copyright (c) 2000-2016 Liferay, Inc. All rights reserved.
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
import javax.portlet.PortletURL;
import javax.portlet.ResourceURL;

import com.liferay.faces.bridge.ext.filter.LiferayActionURL;
import com.liferay.faces.bridge.ext.filter.LiferayRenderURL;
import com.liferay.faces.bridge.ext.filter.LiferayResourceURL;
import com.liferay.faces.bridge.ext.filter.LiferayURLFactory;
import com.liferay.faces.bridge.ext.filter.LiferayURLGenerator;
import com.liferay.portal.kernel.portlet.FriendlyURLMapper;


/**
 * This class implements the {@link com.liferay.faces.bridge.ext.filter.LiferayURLFactory} contract for creating
 * Liferay-compatible URLs. The design provides a performance optimization that was first introduced in FACES-220 and
 * FACES-245. The optimization prevents repetitive calls to Liferay Portal's {@link PortletURL#toString()} method by
 * ensuring that the toString() method of {@link MimeResponse#createActionURL()}, {@link
 * MimeResponse#createRenderURL()}, and {@link MimeResponse#createResourceURL()} are called only once during the JSF
 * lifecycle, and that the pertinent parts of the String are cached. However, the optimization is only usable for
 * portlets that do not have an associated Liferay {@link FriendlyURLMapper}. For more info, see FACES-257.
 *
 * @author  Neil Griffin
 */
public class LiferayURLFactoryImpl extends LiferayURLFactory {

	private static final String ACTION_URL_GENERATOR = LiferayURLFactoryImpl.class.getName() +
		"com.liferay.faces.bridge.container.liferay.ACTION_URL_GENERATOR";
	private static final String RENDER_URL_GENERATOR =
		"com.liferay.faces.bridge.container.liferay.RENDER_URL_GENERATOR";
	private static final String RESOURCE_URL_GENERATOR =
		"com.liferay.faces.bridge.container.liferay.RESOURCE_URL_GENERATOR";

	@Override
	public LiferayActionURL getLiferayActionURL(PortletRequest portletRequest, MimeResponse mimeResponse) {

		LiferayURLGenerator liferayURLGenerator = (LiferayURLGenerator) portletRequest.getAttribute(
				ACTION_URL_GENERATOR);

		if (liferayURLGenerator == null) {

			PortletURL actionURL = mimeResponse.createActionURL();
			liferayURLGenerator = new LiferayURLGeneratorActionImpl(actionURL.toString(), actionURL.getPortletMode(),
					mimeResponse.getNamespace(), actionURL.getWindowState());
			portletRequest.setAttribute(ACTION_URL_GENERATOR, liferayURLGenerator);
		}

		return new LiferayActionURLImpl(liferayURLGenerator);
	}

	@Override
	public LiferayRenderURL getLiferayRenderURL(PortletRequest portletRequest, MimeResponse mimeResponse,
		boolean friendlyURLMapperEnabled) {

		if (friendlyURLMapperEnabled) {

			PortletURL renderURL = mimeResponse.createRenderURL();

			return new LiferayRenderURLFriendlyImpl(renderURL, mimeResponse.getNamespace());
		}
		else {

			LiferayURLGenerator liferayURLGenerator = (LiferayURLGenerator) portletRequest.getAttribute(
					RENDER_URL_GENERATOR);

			if (liferayURLGenerator == null) {

				PortletURL renderURL = mimeResponse.createRenderURL();
				liferayURLGenerator = new LiferayURLGeneratorRenderImpl(renderURL.toString(),
						renderURL.getPortletMode(), mimeResponse.getNamespace(), renderURL.getWindowState());
				portletRequest.setAttribute(RENDER_URL_GENERATOR, liferayURLGenerator);
			}

			return new LiferayRenderURLImpl(liferayURLGenerator);
		}

	}

	@Override
	public LiferayResourceURL getLiferayResourceURL(PortletRequest portletRequest, MimeResponse mimeResponse) {

		LiferayURLGenerator liferayURLGenerator = (LiferayURLGenerator) portletRequest.getAttribute(
				RESOURCE_URL_GENERATOR);

		if (liferayURLGenerator == null) {

			ResourceURL resourceURL = mimeResponse.createResourceURL();
			liferayURLGenerator = new LiferayURLGeneratorResourceImpl(resourceURL.toString(),
					mimeResponse.getNamespace());
			portletRequest.setAttribute(RESOURCE_URL_GENERATOR, liferayURLGenerator);
		}

		return new LiferayResourceURLImpl(liferayURLGenerator);
	}

	@Override
	public LiferayURLFactory getWrapped() {

		// Since this is the factory instance provided by the bridge, it will never wrap another factory.
		return null;
	}
}
