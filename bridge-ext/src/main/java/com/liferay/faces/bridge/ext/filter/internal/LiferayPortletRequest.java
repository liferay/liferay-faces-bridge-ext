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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.portlet.filter.PortletRequestWrapper;
import javax.servlet.http.HttpServletRequest;

import com.liferay.faces.bridge.ext.config.internal.LiferayPortletConfigParam;
import com.liferay.faces.util.HttpHeaders;
import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;

import com.liferay.portal.kernel.model.Portlet;


/**
 * This class wraps an instance of com.liferay.portlet.PortletRequestImpl and provides decorator methods that access the
 * wrapped instance via reflection in order to avoid a compile-time dependency.
 */
public class LiferayPortletRequest {

	// Private Constants
	private static final String METHOD_NAME_GET_ORIGINAL_HTTP_SERVLET_REQUEST = "getOriginalHttpServletRequest";
	private static final String METHOD_NAME_GET_PORTLET = "getPortlet";
	private static final String REQUEST_SCOPED_FQCN = "javax.faces.bean.RequestScoped";

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(LiferayPortletRequest.class);

	// Private Data Members
	private boolean distinctRequestScopedManagedBeans;
	private LiferayHttpServletRequest liferayHttpServletRequest;
	private Portlet portlet;
	private List<String> propertyNameList;
	private String responseNamespace;
	private PortletRequest wrappedPortletRequest;

	public LiferayPortletRequest(PortletRequest portletRequest, String responseNamespace, PortletConfig portletConfig) {

		if (portletRequest != null) {

			while (portletRequest instanceof PortletRequestWrapper) {
				PortletRequestWrapper portletRequestWrapper = (PortletRequestWrapper) portletRequest;
				portletRequest = portletRequestWrapper.getRequest();
			}
		}

		this.wrappedPortletRequest = portletRequest;

		try {
			Method method = wrappedPortletRequest.getClass().getMethod(METHOD_NAME_GET_PORTLET, (Class[]) null);

			this.portlet = (Portlet) method.invoke(wrappedPortletRequest, (Object[]) null);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		try {
			Method method = wrappedPortletRequest.getClass().getMethod(METHOD_NAME_GET_ORIGINAL_HTTP_SERVLET_REQUEST,
					(Class[]) null);
			this.liferayHttpServletRequest = new LiferayHttpServletRequest((HttpServletRequest) method.invoke(
						wrappedPortletRequest, (Object[]) null));
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		this.propertyNameList = new ArrayList<String>();

		boolean foundIfModifiedSince = false;
		boolean foundUserAgent = false;
		Enumeration<String> propertyNames = portletRequest.getPropertyNames();

		while (propertyNames.hasMoreElements() && !foundUserAgent) {
			String propertyName = propertyNames.nextElement();
			propertyNameList.add(propertyName);

			if (HttpHeaders.IF_MODIFIED_SINCE.equals(propertyName)) {
				foundIfModifiedSince = true;
			}
			else if (HttpHeaders.USER_AGENT.equals(propertyName)) {
				foundUserAgent = true;
			}
		}

		if (!foundIfModifiedSince) {

			Enumeration<String> headerNames = liferayHttpServletRequest.getHeaderNames();

			while (headerNames.hasMoreElements() && !foundIfModifiedSince) {
				String headerName = headerNames.nextElement();
				foundIfModifiedSince = (HttpHeaders.IF_MODIFIED_SINCE.equalsIgnoreCase(headerName));
			}

			if (foundIfModifiedSince) {
				propertyNameList.add(HttpHeaders.IF_MODIFIED_SINCE);
			}
		}

		if (!foundUserAgent) {

			Enumeration<String> headerNames = liferayHttpServletRequest.getHeaderNames();

			while (headerNames.hasMoreElements() && !foundUserAgent) {
				String headerName = headerNames.nextElement();
				foundUserAgent = (HttpHeaders.USER_AGENT.equalsIgnoreCase(headerName));
			}

			if (foundUserAgent) {
				propertyNameList.add(HttpHeaders.USER_AGENT);
			}
		}

		this.distinctRequestScopedManagedBeans = LiferayPortletConfigParam.DistinctRequestScopedManagedBeans
			.getBooleanValue(portletConfig);
		this.responseNamespace = responseNamespace;
	}

	public Object getAttribute(String name) {

		// If the specified name is a Servlet-API reserved attribute name, then the JSF runtime is attempting to
		// determine the viewId for a webapp environment. Because of this, it is necessary to return null so that the
		// JSF runtime will attempt to determine the viewId a different way, namely by calling
		// ExternalContext#getRequestPathInfo() or ExternalContext#getRequestServletPath().
		if ("javax.servlet.include.path_info".equals(name) || "javax.servlet.include.servlet_path".equals(name)) {
			return null;
		}
		else {
			Object attributeValue = wrappedPortletRequest.getAttribute(name);

			// FACES-1446: Strictly enforce Liferay Portal's private-request-attribute feature so that each portlet
			// will have its own managed-bean instance.
			if (distinctRequestScopedManagedBeans) {

				if (attributeValue != null) {

					boolean requestScopedBean = false;
					Annotation[] annotations = attributeValue.getClass().getAnnotations();

					if (annotations != null) {

						for (Annotation annotation : annotations) {

							if (annotation.annotationType().getName().equals(REQUEST_SCOPED_FQCN)) {
								requestScopedBean = true;

								break;
							}
						}
					}

					if (requestScopedBean) {

						// If the private-request-attribute feature is enabled in WEB-INF/liferay-portlet.xml, then the
						// NamespaceServletRequest.getAttribute(String) method first tries to get the attribute value by
						// prepending the portlet namespace. If the value is null, then it attempts to get it WITHOUT
						// the prepending the portlet namespace. But that causes a problem for @RequestScoped
						// managed-beans if the same name is used in a different portlet. In the case that the JSF
						// runtime is trying to resolve an EL-expression like "#{backingBean}", then this method must
						// return null if the bean has not yet been created (for this portlet) by the JSF managed-bean
						// facility.
						Object namespacedAttributeValue = wrappedPortletRequest.getAttribute(responseNamespace + name);

						if (namespacedAttributeValue != attributeValue) {
							attributeValue = null;
						}
					}
				}
			}

			return attributeValue;
		}
	}

	public Portlet getPortlet() {
		return portlet;
	}

	public Enumeration<String> getProperties(String name) {

		Enumeration<String> properties = wrappedPortletRequest.getProperties(name);

		if (!properties.hasMoreElements() &&
				(HttpHeaders.USER_AGENT.equals(name) || HttpHeaders.IF_MODIFIED_SINCE.equals(name))) {
			properties = liferayHttpServletRequest.getHeaders(name);
		}

		return properties;
	}

	public String getProperty(String name) {

		String property = wrappedPortletRequest.getProperty(name);

		if (property == null) {
			property = liferayHttpServletRequest.getHeader(name);
		}

		return property;
	}

	public Enumeration<String> getPropertyNames() {
		return Collections.enumeration(propertyNameList);
	}
}
