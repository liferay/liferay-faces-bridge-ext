/**
 * Copyright (c) 2000-2015 Liferay, Inc. All rights reserved.
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
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.liferay.faces.util.client.Script;

import com.liferay.portal.kernel.servlet.taglib.aui.ScriptData;
import com.liferay.portal.kernel.util.WebKeys;


/**
 * @author  Neil Griffin
 */
public class ScriptEncoderLiferayImpl extends ScriptEncoderLiferayBaseImpl {

	@Override
	public void encodeScript(FacesContext facesContext, Script script) throws IOException {

		ExternalContext externalContext = facesContext.getExternalContext();
		Map<String, Object> requestMap = externalContext.getRequestMap();
		ScriptData scriptData = getScriptData(requestMap);
		String portletId = getPortletId(requestMap);
		scriptDataAppendScript(scriptData, portletId, script);
	}

	@Override
	public void encodeScript(FacesContext facesContext, String script) throws IOException {

		ExternalContext externalContext = facesContext.getExternalContext();
		Map<String, Object> requestMap = externalContext.getRequestMap();
		ScriptData scriptData = getScriptData(requestMap);
		String portletId = getPortletId(requestMap);
		scriptDataAppendScript(scriptData, portletId, script);
	}

	@Override
	public void encodeScripts(FacesContext facesContext, List<Script> scripts) throws IOException {

		ExternalContext externalContext = facesContext.getExternalContext();
		Map<String, Object> requestMap = externalContext.getRequestMap();
		ScriptData scriptData = getScriptData(requestMap);
		String portletId = getPortletId(requestMap);

		for (Script script : scripts) {
			scriptDataAppendScript(scriptData, portletId, script);
		}
	}

	private ScriptData getScriptData(Map<String, Object> requestMap) {

		ScriptData scriptData = (ScriptData) requestMap.get(WebKeys.AUI_SCRIPT_DATA);

		if (scriptData == null) {

			scriptData = new ScriptData();
			requestMap.put(WebKeys.AUI_SCRIPT_DATA, scriptData);
		}

		return scriptData;
	}
}
