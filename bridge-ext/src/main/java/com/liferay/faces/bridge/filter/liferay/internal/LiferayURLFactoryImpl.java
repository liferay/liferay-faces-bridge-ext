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
package com.liferay.faces.bridge.filter.liferay.internal;

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.MimeResponse;
import javax.portlet.PortletURL;
import javax.portlet.ResourceURL;

import com.liferay.faces.bridge.filter.liferay.LiferayActionURL;
import com.liferay.faces.bridge.filter.liferay.LiferayRenderURL;
import com.liferay.faces.bridge.filter.liferay.LiferayResourceURL;
import com.liferay.faces.bridge.filter.liferay.LiferayURLFactory;
import com.liferay.faces.bridge.filter.liferay.LiferayURLGenerator;

import com.liferay.portal.kernel.portlet.FriendlyURLMapper;


/**
 * This class implements the {@link com.liferay.faces.bridge.filter.liferay.LiferayURLFactory} contract for creating
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
	public LiferayActionURL getLiferayActionURL(FacesContext facesContext) {

		ExternalContext externalContext = facesContext.getExternalContext();
		Map<String, Object> requestMap = externalContext.getRequestMap();
		LiferayURLGenerator liferayURLGenerator = (LiferayURLGenerator) requestMap.get(ACTION_URL_GENERATOR);

		if (liferayURLGenerator == null) {

			MimeResponse mimeResponse = (MimeResponse) externalContext.getResponse();
			PortletURL actionURL = mimeResponse.createActionURL();
			liferayURLGenerator = new LiferayURLGeneratorActionImpl(actionURL.toString(), actionURL.getPortletMode(),
					mimeResponse.getNamespace(), actionURL.getWindowState());
			requestMap.put(ACTION_URL_GENERATOR, liferayURLGenerator);
		}

		return new LiferayActionURLImpl(liferayURLGenerator);
	}

	@Override
	public LiferayRenderURL getLiferayRenderURL(FacesContext facesContext, boolean friendlyURLMapperEnabled) {

		LiferayRenderURL liferayRenderURL;
		ExternalContext externalContext = facesContext.getExternalContext();
		MimeResponse mimeResponse = (MimeResponse) externalContext.getResponse();

		if (friendlyURLMapperEnabled) {

			PortletURL renderURL = mimeResponse.createRenderURL();
			liferayRenderURL = new LiferayRenderURLFriendlyImpl(renderURL, mimeResponse.getNamespace());
		}
		else {

			Map<String, Object> requestMap = externalContext.getRequestMap();
			LiferayURLGenerator liferayURLGenerator = (LiferayURLGenerator) requestMap.get(RENDER_URL_GENERATOR);

			if (liferayURLGenerator == null) {

				PortletURL renderURL = mimeResponse.createRenderURL();
				liferayURLGenerator = new LiferayURLGeneratorRenderImpl(renderURL.toString(),
						renderURL.getPortletMode(), mimeResponse.getNamespace(), renderURL.getWindowState());
				requestMap.put(RENDER_URL_GENERATOR, liferayURLGenerator);
			}

			liferayRenderURL = new LiferayRenderURLImpl(liferayURLGenerator);
		}

		return liferayRenderURL;
	}

	@Override
	public LiferayResourceURL getLiferayResourceURL(FacesContext facesContext) {

		ExternalContext externalContext = facesContext.getExternalContext();
		Map<String, Object> requestMap = externalContext.getRequestMap();

		LiferayURLGenerator liferayURLGenerator = (LiferayURLGenerator) requestMap.get(RESOURCE_URL_GENERATOR);

		if (liferayURLGenerator == null) {

			MimeResponse mimeResponse = (MimeResponse) externalContext.getResponse();
			ResourceURL resourceURL = mimeResponse.createResourceURL();
			liferayURLGenerator = new LiferayURLGeneratorResourceImpl(resourceURL.toString(),
					mimeResponse.getNamespace());
			requestMap.put(RESOURCE_URL_GENERATOR, liferayURLGenerator);
		}

		return new LiferayResourceURLImpl(liferayURLGenerator);
	}

	@Override
	public LiferayURLFactory getWrapped() {

		// Since this is the factory instance provided by the bridge, it will never wrap another factory.
		return null;
	}

}
