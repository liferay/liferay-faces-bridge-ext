/**
 * Copyright (c) 2000-2016 Liferay, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.liferay.faces.bridge.ext.application.internal;

import javax.faces.application.Resource;

import com.liferay.faces.util.application.FilteredResourceBase;


/**
 * @author  Kyle Stiemann
 */
public class ResourcePrimefacesJQueryPluginJSImpl extends FilteredResourceBase {

	// Private Members
	private Resource wrappedResource;

	public ResourcePrimefacesJQueryPluginJSImpl(Resource wrappedResource) {
		this.wrappedResource = wrappedResource;
	}

	@Override
	protected String filter(String string) {
		return string.replaceAll("typeof\\s+define\\s*=(=+)\\s*[\"']function[\"']", "false&&typeof define=$1'function'")
			.replaceAll("[\"']function[\"']\\s*=(=+)\\s*typeof\\s+define", "false&&'function'=$1typeof define");
	}

	@Override
	public Resource getWrapped() {
		return wrappedResource;
	}
}
