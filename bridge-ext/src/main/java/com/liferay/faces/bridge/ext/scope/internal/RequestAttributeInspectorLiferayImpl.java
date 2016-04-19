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
package com.liferay.faces.bridge.ext.scope.internal;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortalContext;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.faces.Bridge;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import com.liferay.faces.bridge.scope.RequestAttributeInspector;
import com.liferay.faces.bridge.scope.RequestAttributeInspectorWrapper;
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
	private static final String EXCLUDED_NAMESPACE_JAVAX_FACES = "javax.faces";
	private static final String EXCLUDED_NAMESPACE_JAVAX_PORTLET = "javax.portlet";
	private static final String EXCLUDED_NAMESPACE_JAVAX_PORTLET_FACES = "javax.portlet.faces";
	private static final String EXCLUCED_NAMESPACE_JAVAX_SERVLET = "javax.servlet";
	private static final String EXCLUCED_NAMESPACE_JAVAX_SERVLET_INCLUDE = "javax.servlet.include";

	// Private Constants
	private static final String[] LIFERAY_ATTRIBUTE_NAMES;
	private static final String[] PORTLET_REQUEST_ATTRIBUTE_NAMES = new String[] {
			PortletRequest.CCPP_PROFILE, PortletRequest.LIFECYCLE_PHASE, PortletRequest.RENDER_HEADERS,
			PortletRequest.RENDER_MARKUP, PortletRequest.RENDER_PART, PortletRequest.USER_INFO
		};

	static {

		// Set the value of the LIFERAY_ATTRIBUTE_NAMES constant. Need to use reflection in order to determine all of
		// the public constants because different versions of the portal source have different sets of constants. This
		// approach minimizes diffs in the different source branches for the bridge.
		List<String> fieldList = new ArrayList<String>();
		Field[] fields = JavaConstants.class.getFields();

		for (Field field : fields) {

			String fieldName = field.getName();

			if ((fieldName != null) && fieldName.startsWith("JAVAX")) {

				try {
					Object value = field.get(null);

					if ((value != null) && (value instanceof String)) {
						fieldList.add((String) value);
					}
				}
				catch (Exception e) {
					logger.error(e);
				}
			}
		}

		LIFERAY_ATTRIBUTE_NAMES = fieldList.toArray(new String[fieldList.size()]);
	}

	// Private Data Members
	private RequestAttributeInspector wrappedRequestAttributeInspector;

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

		if (isNamespaceMatch(name, EXCLUDED_NAMESPACE_JAVAX_FACES) ||
				isNamespaceMatch(name, EXCLUCED_NAMESPACE_JAVAX_SERVLET) ||
				isNamespaceMatch(name, EXCLUCED_NAMESPACE_JAVAX_SERVLET_INCLUDE)) {

			// Always safe to exclude when running under Liferay Portal.
			excluded = true;
		}
		else if (isNamespaceMatch(name, EXCLUDED_NAMESPACE_JAVAX_PORTLET_FACES)) {

			if (!Bridge.PORTLET_LIFECYCLE_PHASE.equals(name)) {

				// The "javax.portlet.faces.phase" request attribute must never be excluded, as it is required by {@link
				// BridgeUtil#getPortletRequestPhase()}.
				excluded = true;
			}
		}
		else if (isNamespaceMatch(name, EXCLUDED_NAMESPACE_JAVAX_PORTLET)) {
			excluded = true;
		}

		if (excluded) {

			for (String liferayAttributeName : LIFERAY_ATTRIBUTE_NAMES) {

				if (liferayAttributeName.equals(name)) {
					excluded = false;

					break;
				}
			}
		}

		if (excluded) {

			for (String portletRequestAttributeName : PORTLET_REQUEST_ATTRIBUTE_NAMES) {

				if (portletRequestAttributeName.equals(name)) {
					excluded = false;

					break;
				}
			}
		}

		return excluded;
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

				// Liferay Portal includes request attribute named "javax.portlet.config" that must not be excluded. It
				// also includes an attribute named "javax.portlet.portlet" that is the current GenericFacesPortlet
				// (which extends GenericPortlet). But since GenericPortlet implements the PortletConfig interface, need
				// to prevent it from being excluded as well.
				if (!JavaConstants.JAVAX_PORTLET_CONFIG.equals(name) &&
						!JavaConstants.JAVAX_PORTLET_PORTLET.equals(name)) {
					excluded = true;
				}
			}
			else if (value instanceof PortletRequest) {

				// Liferay Portal includes request attribute named "javax.portlet.request" that must not be excluded.
				if (!JavaConstants.JAVAX_PORTLET_REQUEST.equals(name)) {
					excluded = true;
				}
			}
			else if (value instanceof PortletResponse) {

				// Liferay Portal includes request attribute named "javax.portlet.response" that must not be excluded.
				if (!JavaConstants.JAVAX_PORTLET_RESPONSE.equals(name)) {
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

	@Override
	public RequestAttributeInspector getWrapped() {
		return wrappedRequestAttributeInspector;
	}
}
