/**
 * Copyright (c) 2000-2025 Liferay, Inc. All rights reserved.
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
package com.liferay.faces.bridge.ext.scope.internal;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.portlet.PortalContext;
import jakarta.portlet.PortletConfig;
import jakarta.portlet.PortletContext;
import jakarta.portlet.PortletPreferences;
import jakarta.portlet.PortletRequest;
import jakarta.portlet.PortletResponse;
import jakarta.portlet.PortletSession;
import jakarta.portlet.faces.Bridge;
import jakarta.portlet.faces.RequestAttributeInspector;
import jakarta.portlet.faces.RequestAttributeInspectorWrapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpSession;

import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;

import com.liferay.portal.kernel.util.JavaConstants;


/**
 * @author  Neil Griffin
 */
public class RequestAttributeInspectorLiferayImpl extends RequestAttributeInspectorWrapper implements Serializable {

	// serialVersionUID
	private static final long serialVersionUID = 1876589389377771517L;

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(RequestAttributeInspectorLiferayImpl.class);

	// Private Constants for EXCLUDED namespaces listed in Section 5.1.2 of the JSR 329 Spec
	private static final String EXCLUDED_NAMESPACE_JAKARTA_FACES = "jakarta.faces";
	private static final String EXCLUDED_NAMESPACE_JAKARTA_PORTLET = "jakarta.portlet";
	private static final String EXCLUDED_NAMESPACE_JAKARTA_PORTLET_FACES = "jakarta.portlet.faces";
	private static final String EXCLUCED_NAMESPACE_JAKARTA_SERVLET = "jakarta.servlet";
	private static final String EXCLUCED_NAMESPACE_JAKARTA_SERVLET_INCLUDE = "jakarta.servlet.include";

	// Private Constants
	private static final List<String> LIFERAY_ATTRIBUTE_NAMES;

	//J-
	private static final List<String> PORTLET_REQUEST_ATTRIBUTE_NAMES = Collections.unmodifiableList(Arrays.asList(
		PortletRequest.CCPP_PROFILE,
		PortletRequest.LIFECYCLE_PHASE,
		PortletRequest.RENDER_HEADERS,
		PortletRequest.RENDER_MARKUP,
		PortletRequest.RENDER_PART,
		PortletRequest.USER_INFO
	));
	//J+

	static {

		// Set the value of the LIFERAY_ATTRIBUTE_NAMES constant. Need to use reflection in order to determine all of
		// the public constants because different versions of the portal source have different sets of constants. This
		// approach minimizes diffs in the different source branches for the bridge.
		List<String> liferayAttributeNames = new ArrayList<String>();
		Field[] fields = JavaConstants.class.getFields();

		for (Field field : fields) {

			String fieldName = field.getName();

			if ((fieldName != null) && fieldName.startsWith("JAKARTA")) {

				try {
					Object value = field.get(null);

					if ((value != null) && (value instanceof String)) {
						liferayAttributeNames.add((String) value);
					}
				}
				catch (Exception e) {
					logger.error(e);
				}
			}
		}

		LIFERAY_ATTRIBUTE_NAMES = Collections.unmodifiableList(liferayAttributeNames);
	}

	// Private Final Data Members
	private final RequestAttributeInspector wrappedRequestAttributeInspector;

	public RequestAttributeInspectorLiferayImpl(RequestAttributeInspector requestAttributeInspector) {
		this.wrappedRequestAttributeInspector = requestAttributeInspector;
	}

	/**
	 * This is a method-override that provides specific behavior for Liferay Portal. Specifically, since Liferay Portal
	 * does not implement the POST-REDIRECT-GET design pattern, not all instance types listed in Section 5.1.2 of the
	 * Spec can be candidates for exclusion.
	 */
	@Override
	public boolean containsExcludedNamespace(String name) {
		boolean excluded = false;

		if (isNamespaceMatch(name, EXCLUDED_NAMESPACE_JAKARTA_FACES) ||
				isNamespaceMatch(name, EXCLUCED_NAMESPACE_JAKARTA_SERVLET) ||
				isNamespaceMatch(name, EXCLUCED_NAMESPACE_JAKARTA_SERVLET_INCLUDE)) {

			// Always safe to exclude when running under Liferay Portal.
			excluded = true;
		}
		else if (isNamespaceMatch(name, EXCLUDED_NAMESPACE_JAKARTA_PORTLET_FACES) &&
				!Bridge.PORTLET_LIFECYCLE_PHASE.equals(name)) {

			// The "jakarta.portlet.faces.phase" request attribute must never be excluded, as it is required by {@link
			// BridgeUtil#getPortletRequestPhase()}.
			excluded = true;
		}
		else if (isNamespaceMatch(name, EXCLUDED_NAMESPACE_JAKARTA_PORTLET)) {
			excluded = true;
		}

		if (excluded && (LIFERAY_ATTRIBUTE_NAMES.contains(name) || PORTLET_REQUEST_ATTRIBUTE_NAMES.contains(name))) {
			excluded = false;
		}

		return excluded;
	}

	@Override
	public RequestAttributeInspector getWrapped() {
		return wrappedRequestAttributeInspector;
	}

	/**
	 * This is a method-override that provides specific behavior for Liferay Portal. Specifically, since Liferay Portal
	 * does not implement the POST-REDIRECT-GET design pattern, not all instance types listed in Section 5.1.2 of the
	 * Spec can be candidates for exclusion.
	 */
	@Override
	public boolean isExcludedByType(String name, Object value) {

		boolean excluded = false;

		if (value != null) {

			if ((value instanceof ExternalContext) || (value instanceof FacesContext)) {

				// Always safe to exclude when running under Liferay Portal.
				excluded = true;
			}
			else if (value instanceof PortletConfig) {

				// Liferay Portal includes request attribute named "jakarta.portlet.config" that must not be excluded. It
				// also includes an attribute named "jakarta.portlet.portlet" that is the current GenericFacesPortlet
				// (which extends GenericPortlet). But since GenericPortlet implements the PortletConfig interface, need
				// to prevent it from being excluded as well.
				if (!JavaConstants.JAKARTA_PORTLET_CONFIG.equals(name) &&
						!JavaConstants.JAKARTA_PORTLET_PORTLET.equals(name)) {
					excluded = true;
				}
			}
			else if (value instanceof PortletRequest) {

				// Liferay Portal includes request attribute named "jakarta.portlet.request" that must not be excluded.
				if (!JavaConstants.JAKARTA_PORTLET_REQUEST.equals(name)) {
					excluded = true;
				}
			}
			else if (value instanceof PortletResponse) {

				// Liferay Portal includes request attribute named "jakarta.portlet.response" that must not be excluded.
				if (!JavaConstants.JAKARTA_PORTLET_RESPONSE.equals(name)) {
					excluded = true;
				}
			}
			else if ((value instanceof PortalContext) || (value instanceof PortletContext) ||
					(value instanceof PortletPreferences) || (value instanceof PortletSession)) {

				excluded = true;
			}
			else if ((value instanceof HttpSession) || (value instanceof ServletConfig) ||
					(value instanceof ServletContext) || (value instanceof ServletRequest) ||
					(value instanceof ServletResponse)) {

				// Can only exclude attributes that are not Liferay objects. For example, Liferay Portal includes
				// a request attribute named "com.liferay.portal.kernel.servlet.PortletServletRequest" that must not be
				// excluded.
				if (!value.getClass().getName().startsWith("com.liferay")) {
					excluded = true;
				}
			}
		}

		return excluded;
	}

	protected boolean isNamespaceMatch(String attributeName, String namespace) {

		boolean match = false;

		String attributeNamespace = attributeName;
		int dotPos = attributeNamespace.lastIndexOf(".");

		if (dotPos > 0) {
			attributeNamespace = attributeNamespace.substring(0, dotPos);
		}

		if (namespace.equals(attributeNamespace)) {
			match = true;
		}

		return match;
	}
}
