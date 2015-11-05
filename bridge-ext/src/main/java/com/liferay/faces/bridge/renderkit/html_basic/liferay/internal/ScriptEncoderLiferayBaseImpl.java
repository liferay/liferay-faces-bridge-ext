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
/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferay.faces.bridge.renderkit.html_basic.liferay.internal;

import java.io.IOException;
import java.util.Map;

import com.liferay.faces.util.client.AlloyScript;
import com.liferay.faces.util.client.Script;

import com.liferay.portal.kernel.servlet.taglib.aui.ScriptData;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Portlet;


/**
 * @author  Kyle Stiemann
 */
public abstract class ScriptEncoderLiferayBaseImpl extends ScriptEncoderLiferayCompatImpl {

	protected void scriptDataAppendScript(ScriptData scriptData, String portletId, Script script) throws IOException {

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

			scriptDataAppendScript(scriptData, portletId, alloyScript.getSourceCode(), modulesStringBuilder.toString());
		}
		else {
			scriptDataAppendScript(scriptData, portletId, script.getSourceCode());
		}
	}

	protected void scriptDataAppendScript(ScriptData scriptData, String portletId, String script) {
		scriptDataAppendScript(scriptData, portletId, script, null);
	}

	protected String getPortletId(Map<String, Object> requestMap) {

		String portletId = "";
		Object portletObject = requestMap.get(WebKeys.RENDER_PORTLET);

		if ((portletObject != null) && (portletObject instanceof Portlet)) {

			Portlet portlet = (Portlet) portletObject;
			portletId = portlet.getPortletId();
		}

		return portletId;
	}
}
