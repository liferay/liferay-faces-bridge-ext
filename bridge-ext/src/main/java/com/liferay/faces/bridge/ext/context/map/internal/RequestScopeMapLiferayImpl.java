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
package com.liferay.faces.bridge.ext.context.map.internal;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletRequest;


/**
 * @author  Neil Griffin
 */
public class RequestScopeMapLiferayImpl implements Map<String, Object> {

	// Private Constants
	private static final String REQUEST_SCOPED_FQCN = "javax.faces.bean.RequestScoped";

	// Private Data Members
	private boolean distinctRequestScopedManagedBeans;
	private PortletRequest portletRequest;
	private String responseNamespace;
	private Map<String, Object> wrappedRequestScopeMap;

	/**
	 * @param  requestScopeMap                    The wrapped map of request attributes.
	 * @param  portletRequest                     The current portlet request.
	 * @param  distinctRequestScopedManagedBeans  Determines whether or not JSF @ManagedBean classes annotated with
	 *                                            &#064;RequestScoped should be distinct for each portlet when running
	 *                                            under Liferay Portal.
	 */
	public RequestScopeMapLiferayImpl(Map<String, Object> requestScopeMap, PortletRequest portletRequest,
		String responseNamespace, boolean distinctRequestScopedManagedBeans) {
		this.wrappedRequestScopeMap = requestScopeMap;
		this.portletRequest = portletRequest;
		this.responseNamespace = responseNamespace;
		this.distinctRequestScopedManagedBeans = distinctRequestScopedManagedBeans;
	}

	@Override
	public void clear() {
		wrappedRequestScopeMap.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return wrappedRequestScopeMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return wrappedRequestScopeMap.containsValue(value);
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return wrappedRequestScopeMap.entrySet();
	}

	@Override
	public boolean equals(Object o) {
		return wrappedRequestScopeMap.equals(o);
	}

	@Override
	public Object get(Object key) {

		Object attributeValue = wrappedRequestScopeMap.get(key);

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
					Object namespacedAttributeValue = portletRequest.getAttribute(responseNamespace + key);

					if (namespacedAttributeValue != attributeValue) {
						attributeValue = null;
					}
				}
			}
		}

		return attributeValue;
	}

	@Override
	public int hashCode() {
		return wrappedRequestScopeMap.hashCode();
	}

	@Override
	public Set<String> keySet() {
		return wrappedRequestScopeMap.keySet();
	}

	@Override
	public Object put(String key, Object value) {
		return wrappedRequestScopeMap.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ?> m) {
		wrappedRequestScopeMap.putAll(m);
	}

	@Override
	public Object remove(Object key) {
		return wrappedRequestScopeMap.remove(key);
	}

	@Override
	public int size() {
		return wrappedRequestScopeMap.size();
	}

	@Override
	public Collection<Object> values() {
		return wrappedRequestScopeMap.values();
	}

	@Override
	public boolean isEmpty() {
		return wrappedRequestScopeMap.isEmpty();
	}
}
