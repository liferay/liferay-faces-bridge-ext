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
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

import com.liferay.faces.util.client.AlloyScript;
import com.liferay.faces.util.client.Script;

import com.liferay.portal.kernel.servlet.taglib.aui.ScriptData;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.util.PortalUtil;


/**
 * @author  Kyle Stiemann
 */
public class ScriptsEncoderLiferayImpl extends ScriptsEncoderLiferayCompatImpl {

	@Override
	public void encodeBodyScripts(FacesContext facesContext, List<Script> scripts) throws IOException {

		ExternalContext externalContext = facesContext.getExternalContext();
		Map<String, Object> requestMap = externalContext.getRequestMap();
		ScriptData scriptData = (ScriptData) requestMap.get(WebKeys.AUI_SCRIPT_DATA);

		if (scriptData == null) {

			scriptData = new ScriptData();
			requestMap.put(WebKeys.AUI_SCRIPT_DATA, scriptData);
		}

		scriptDataAppendScripts(scriptData, requestMap, scripts);
	}

	@Override
	public void encodeEvalScripts(FacesContext facesContext, List<Script> scripts) throws IOException {

		ScriptData scriptData = new ScriptData();
		ExternalContext externalContext = facesContext.getExternalContext();
		Map<String, Object> requestMap = externalContext.getRequestMap();
		scriptDataAppendScripts(scriptData, requestMap, scripts);

		ResponseWriter responseWriter = facesContext.getResponseWriter();
		responseWriter.write(scriptDataToString(scriptData, externalContext));
	}

	private void scriptDataAppendScripts(ScriptData scriptData, Map<String, Object> requestMap, List<Script> scripts)
		throws IOException {

		String portletId = "";
		Object portletObject = requestMap.get(WebKeys.RENDER_PORTLET);

		if ((portletObject != null) && (portletObject instanceof Portlet)) {

			Portlet portlet = (Portlet) portletObject;
			portletId = portlet.getPortletId();
		}

		for (Script script : scripts) {

			if (script instanceof AlloyScript) {

				AlloyScript alloyScript = (AlloyScript) script;
				StringBuilder modulesStringBuilder = new StringBuilder();
				final String[] modules = alloyScript.getModules();
				boolean firstModule = true;

				for (String module : modules) {

					if (!firstModule) {
						modulesStringBuilder.append(",");
					}

					modulesStringBuilder.append(module);
					firstModule = false;
				}

				scriptDataAppendScript(scriptData, portletId, alloyScript.getSourceCode(),
					modulesStringBuilder.toString());
			}
			else {
				scriptDataAppendScript(scriptData, portletId, script.getSourceCode(), null);
			}
		}
	}

	private String scriptDataToString(ScriptData scriptData, ExternalContext externalContext) throws IOException {

		PortletRequest portletRequest = (PortletRequest) externalContext.getRequest();
		HttpServletRequest httpServletRequest = PortalUtil.getHttpServletRequest(portletRequest);
		ScriptDataWriter scriptDataWriter = new ScriptDataWriter();
		scriptData.writeTo(httpServletRequest, scriptDataWriter);

		return scriptDataWriter.toString();
	}

	private class ScriptDataWriter extends StringWriter {

		@Override
		public void write(String string) {

			if (!(string.startsWith("<script") || string.endsWith("script>"))) {
				super.write(string);
			}
		}
	}
}
