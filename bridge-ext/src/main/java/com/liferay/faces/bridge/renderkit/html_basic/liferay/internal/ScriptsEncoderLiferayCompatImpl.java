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
package com.liferay.faces.bridge.renderkit.html_basic.liferay.internal;

import java.io.IOException;
import java.io.StringWriter;

import javax.faces.context.ExternalContext;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

import com.liferay.faces.util.client.ScriptsEncoder;

import com.liferay.portal.kernel.servlet.taglib.aui.ScriptData;
import com.liferay.portal.util.PortalUtil;


/**
 * @author  Kyle Stiemann
 */
public abstract class ScriptsEncoderLiferayCompatImpl implements ScriptsEncoder {

	protected void scriptDataAppendScript(ScriptData scriptData, String portletId, String script, String modules) {
		scriptData.append(portletId, script, modules);
	}

	protected final String scriptDataToString(ExternalContext externalContext, ScriptData scriptData)
		throws IOException {

		PortletRequest portletRequest = (PortletRequest) externalContext.getRequest();
		HttpServletRequest httpServletRequest = PortalUtil.getHttpServletRequest(portletRequest);
		StringWriter stringWriter = new StringWriter();
		scriptData.writeTo(httpServletRequest, stringWriter);

		return stringWriter.toString();
	}
}
