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
package com.liferay.faces.bridge.ext.renderkit.html_basic.internal;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.liferay.faces.util.client.Script;

import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.servlet.taglib.aui.ScriptData;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;


/**
 * @author  Kyle Stiemann
 */
public class ScriptsEncoderLiferayImpl extends ScriptsEncoderLiferayCompatImpl {

	@Override
	public void encodeBodyScripts(FacesContext facesContext, List<Script> scripts) throws IOException {

		ExternalContext externalContext = facesContext.getExternalContext();
		Map<String, Object> requestMap = externalContext.getRequestMap();
		ThemeDisplay themeDisplay = (ThemeDisplay) requestMap.get(WebKeys.THEME_DISPLAY);

		// If the portlet is being rendered on its own, then render the scripts immediately before the closing <div>
		// of the portlet.
		if (themeDisplay.isStateExclusive() || themeDisplay.isIsolated()) {

			String scriptsString = getScriptsAsString(facesContext, scripts);
			ResponseWriter responseWriter = facesContext.getResponseWriter();
			responseWriter.write(scriptsString);
		}

		// Otherwise, allow Liferay to render the scripts at the bottom of the page before the closing <body> tag.
		else {

			ScriptData scriptData = (ScriptData) requestMap.get(WebKeys.AUI_SCRIPT_DATA);

			if (scriptData == null) {

				scriptData = new ScriptData();
				requestMap.put(WebKeys.AUI_SCRIPT_DATA, scriptData);
			}

			scriptDataAppendScripts(scriptData, requestMap, scripts);
		}
	}

	@Override
	public void encodeEvalScripts(FacesContext facesContext, List<Script> scripts) throws IOException {

		String scriptsString = getScriptsAsString(facesContext, scripts);

		// Strip off opening <script> and CDATA tags.
		int startCDATAIndex = scriptsString.indexOf("<![CDATA[");

		if (startCDATAIndex != -1) {
			scriptsString = scriptsString.substring(startCDATAIndex + "<![CDATA[".length());
		}

		// Strip off closing <script> and CDATA tags.
		int endCDATAIndex = scriptsString.indexOf("]]>");

		if (endCDATAIndex != -1) {
			scriptsString = scriptsString.substring(0, endCDATAIndex);
		}

		ResponseWriter responseWriter = facesContext.getResponseWriter();
		responseWriter.write(scriptsString);
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

			Script.Type type = script.getType();

			if (Script.Type.ALLOY.equals(type)) {

				StringBuilder modulesStringBuilder = new StringBuilder();
				String[] modules = script.getModules();

				if (modules != null) {

					boolean firstModule = true;

					for (String module : modules) {

						if (!firstModule) {
							modulesStringBuilder.append(",");
						}

						modulesStringBuilder.append(module);
						firstModule = false;
					}
				}

				scriptDataAppendScript(scriptData, portletId, script.getSourceCode(), modulesStringBuilder.toString());
			}
			else {
				scriptDataAppendScript(scriptData, portletId, script.getSourceCode(), null);
			}
		}
	}

	private String getScriptsAsString(FacesContext facesContext, List<Script> scripts) throws IOException {

		ScriptData scriptData = new ScriptData();
		ExternalContext externalContext = facesContext.getExternalContext();
		Map<String, Object> requestMap = externalContext.getRequestMap();
		scriptDataAppendScripts(scriptData, requestMap, scripts);

		return scriptDataToString(externalContext, scriptData);
	}
}
