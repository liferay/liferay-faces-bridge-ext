/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferay.faces.bridge.renderkit.html_basic.liferay.internal;

import com.liferay.faces.util.client.AlloyScript;
import com.liferay.faces.util.client.Script;
import com.liferay.faces.util.client.ScriptEncoder;
import com.liferay.portal.kernel.servlet.taglib.aui.ScriptData;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Portlet;
import java.io.IOException;
import java.util.Map;

/**
 * @author  Kyle Stiemann
 */
public abstract class ScriptEncoderLiferayBaseImpl implements ScriptEncoder {

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

			scriptData.append(portletId, alloyScript.getSourceCode(), modulesStringBuilder.toString(), ScriptData.ModulesType.AUI);
		}
		else {
			scriptData.append(portletId, script.getSourceCode(), null, ScriptData.ModulesType.AUI);
		}
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
