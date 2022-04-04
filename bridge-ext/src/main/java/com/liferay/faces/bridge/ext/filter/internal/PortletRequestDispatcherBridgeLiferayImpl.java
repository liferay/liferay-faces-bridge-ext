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
package com.liferay.faces.bridge.ext.filter.internal;

import java.io.IOException;

import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.faces.BridgeFactoryFinder;
import javax.portlet.filter.PortletRequestDispatcherWrapper;
import javax.portlet.filter.PortletRequestWrapper;
import javax.portlet.filter.PortletResponseWrapper;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServletResponseWrapper;

import com.liferay.faces.bridge.ext.config.internal.LiferayPortletConfigParam;
import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;
import com.liferay.faces.util.product.Product;
import com.liferay.faces.util.product.ProductFactory;


/**
 * @author  Neil Griffin
 */
public class PortletRequestDispatcherBridgeLiferayImpl extends PortletRequestDispatcherWrapper {

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(PortletRequestDispatcherBridgeLiferayImpl.class);

	// Private Data Members
	private final String path;

	public PortletRequestDispatcherBridgeLiferayImpl(PortletRequestDispatcher portletRequestDispatcher, String path) {
		super(portletRequestDispatcher);
		this.path = path;
	}

	@Override
	public void forward(PortletRequest portletRequest, PortletResponse portletResponse) throws PortletException,
		IOException {

		PortletConfig portletConfig = (PortletConfig) portletRequest.getAttribute(PortletConfig.class.getName());

		if (LiferayPortletConfigParam.RequestDispatcherForwardEnabled.getBooleanValue(portletConfig)) {

			if ((path != null) && path.endsWith(".jspx")) {

				// Workaround https://issues.liferay.com/browse/LPS-71904
				logger.debug("Diverting JSPX=[{0}] to PortletRequestDispatcher.include(PortletRequest,PortletResponse)",
					path);
				include(portletRequest, portletResponse);
			}
			else {
				logger.debug("Delegating JSP=[{0}] to PortletRequestDispatcher.forward(PortletRequest,PortletResponse)",
					path);
				super.forward(portletRequest, portletResponse);
			}
		}
		else {

			// Liferay Portal's implementation of PortletRequestDispatcher.forward(PortletRequest,PortletResponse) is
			// not compatible with the requirements of the JSF Portlet Bridge and causes failures in the Bridge TCK
			// dispatchUsesForwardTest and bridgeSetsContentTypeTest. As a workaround, call
			// PortletRequestDispatcher.include(PortletRequest,PortletResponse).
			logger.debug("Diverting JSP/JSPX=[{0}] to PortletRequestDispatcher.include(PortletRequest,PortletResponse)",
				path);
			include(portletRequest, portletResponse);
		}
	}

	@Override
	public void include(PortletRequest portletRequest, PortletResponse portletResponse) throws PortletException,
		IOException {

		boolean unwrapRequest = false;
		PortletSession portletSession = portletRequest.getPortletSession();
		PortletContext portletContext = portletSession.getPortletContext();
		ProductFactory productFactory = (ProductFactory) BridgeFactoryFinder.getFactory(portletContext,
				ProductFactory.class);
		final Product LIFERAY_PORTAL = productFactory.getProductInfo(Product.Name.LIFERAY_PORTAL);
		final int LIFERAY_PORTAL_MAJOR_VERSION = LIFERAY_PORTAL.getMajorVersion();

		// Versions of Liferay Portal older than 6.0.0 throw a ClassCastException when RenderRequestWrapper is used.
		// For more info, see https://issues.liferay.com/browse/LPS-3311
		if (LIFERAY_PORTAL_MAJOR_VERSION < 6) {
			unwrapRequest = true;
		}

		// Versions of Liferay Portal older than 6.1.2 (CE) and 6.1.30 (EE) will throw a ClassCastException when a
		// PortletRequestWrapper is specified. For more info, see https://issues.liferay.com/browse/LPS-36713 and
		// https://github.com/liferay/liferay-portal/commit/093dabbb252e2bba5404cddbcb600d787ef0b010
		else if (LIFERAY_PORTAL_MAJOR_VERSION == 6) {

			final int LIFERAY_PORTAL_MINOR_VERSION = LIFERAY_PORTAL.getMinorVersion();

			if (LIFERAY_PORTAL_MINOR_VERSION == 0) {
				unwrapRequest = true;
			}
			else if (LIFERAY_PORTAL_MINOR_VERSION == 1) {

				final int LIFERAY_PORTAL_PATCH_VERSION = LIFERAY_PORTAL.getPatchVersion();

				// CE
				if (LIFERAY_PORTAL_PATCH_VERSION < 10) {

					unwrapRequest = (LIFERAY_PORTAL_PATCH_VERSION < 2);
				}

				// EE
				else if (LIFERAY_PORTAL_PATCH_VERSION < 30) {
					unwrapRequest = true;
				}
			}
		}

		if (unwrapRequest) {
			portletRequest = unwrapPortletRequest(portletRequest);
		}

		// If the specified portletResponse implements HttpServletResponseWrapper then the bridge implementation might
		// be trying to overcome a Servlet API dependency in the JSF implementation by wrapping the portletResponse with
		// HttpServletResponseRenderAdapter or HttpServletResponseResourceAdapter. If that's the case then Liferay
		// Portal's PortletRequestDispatcher.include(PortletRequest,PortletResponse) method will be unable to unwrap the
		// portletResponse since those classes do not extend PortletResponseWrapper. As a workaround, unwrap the
		// portletResponse to a point such that Liferay's PortletResponseImpl is decorated only by instances of
		// PortletResponseWrapper.
		if ((portletResponse instanceof HttpServletResponseWrapper) ||
				(portletResponse instanceof PortletResponseWrapper)) {
			portletResponse = unwrapPortletResponse(portletResponse);
		}

		super.include(portletRequest, portletResponse);
	}

	protected PortletRequest unwrapPortletRequest(PortletRequest portletRequest) {

		if (portletRequest instanceof PortletRequestWrapper) {

			PortletRequestWrapper portletRequestWrapper = (PortletRequestWrapper) portletRequest;
			portletRequest = portletRequestWrapper.getRequest();

			return unwrapPortletRequest(portletRequest);
		}
		else {
			return portletRequest;
		}
	}

	protected PortletResponse unwrapPortletResponse(PortletResponse portletResponse) {

		if (portletResponse instanceof ServletResponse) {

			PortletResponse unwrappedServletResponse = unwrapServletResponse((ServletResponse) portletResponse);

			if (unwrappedServletResponse != null) {
				return unwrappedServletResponse;
			}
			else {
				return portletResponse;
			}
		}
		else if (portletResponse instanceof PortletResponseWrapper) {

			PortletResponseWrapper portletResponseWrapper = (PortletResponseWrapper) portletResponse;
			portletResponse = portletResponseWrapper.getResponse();

			return unwrapPortletResponse(portletResponse);
		}
		else {
			return portletResponse;
		}
	}

	protected PortletResponse unwrapServletResponse(ServletResponse servletResponse) {

		if (servletResponse instanceof ServletResponseWrapper) {

			ServletResponseWrapper servletResponseWrapper = (ServletResponseWrapper) servletResponse;
			servletResponse = servletResponseWrapper.getResponse();

			return unwrapServletResponse(servletResponse);
		}
		else if (servletResponse instanceof PortletResponse) {
			return (PortletResponse) servletResponse;
		}
		else {
			return null;
		}
	}
}
