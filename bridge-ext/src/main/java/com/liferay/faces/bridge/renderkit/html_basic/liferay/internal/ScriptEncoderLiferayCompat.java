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

import com.liferay.faces.util.client.ScriptEncoder;

import com.liferay.portal.kernel.servlet.taglib.aui.ScriptData;


/**
 * @author  Kyle Stiemann
 */
public abstract class ScriptEncoderLiferayCompat implements ScriptEncoder {

	protected void scriptDataAppendScript(ScriptData scriptData, String portletId, String script, String modules) {
		scriptData.append(portletId, script, modules);
	}
}
