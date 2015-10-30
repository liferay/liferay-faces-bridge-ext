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

import javax.el.ELContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import com.liferay.faces.bridge.client.liferay.internal.ScriptDataUtil;
import com.liferay.faces.bridge.context.liferay.internal.ScriptTagUtil;
import com.liferay.faces.util.client.Script;
import com.liferay.faces.util.client.ScriptEncoder;
import com.liferay.faces.util.context.FacesRequestContext;
import com.liferay.faces.util.factory.FactoryExtensionFinder;
import com.liferay.faces.util.jsp.JspAdapterFactory;
import com.liferay.faces.util.jsp.JspWriterWrapper;

import com.liferay.portal.kernel.servlet.taglib.aui.ScriptData;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.util.PortalUtil;


/**
 * @author  Kyle Stiemann
 */
public class ScriptEncoderLiferayAjaxImpl implements ScriptEncoder {

	@Override
	public void encodeScripts(ResponseWriter responseWriter) throws IOException {

		FacesRequestContext facesRequestContext = FacesRequestContext.getCurrentInstance();
		List<Script> scripts = facesRequestContext.getScripts();
		ScriptData scriptData = new ScriptData();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		Map<String, Object> requestMap = externalContext.getRequestMap();
		ScriptDataUtil.scriptDataAppendScripts(scriptData, requestMap, scripts);

		ScriptData savedScriptData = (ScriptData) requestMap.get(WebKeys.AUI_SCRIPT_DATA);
		requestMap.put(WebKeys.AUI_SCRIPT_DATA, scriptData);

		PortletRequest portletRequest = (PortletRequest) externalContext.getRequest();
		HttpServletRequest httpServletRequest = PortalUtil.getHttpServletRequest(portletRequest);
		PortletResponse portletResponse = (PortletResponse) externalContext.getResponse();
		HttpServletResponse httpServletResponse = PortalUtil.getHttpServletResponse(portletResponse);
		ELContext elContext = facesContext.getELContext();
		JspAdapterFactory jspAdapterFactory = (JspAdapterFactory) FactoryExtensionFinder.getFactory(
				JspAdapterFactory.class);
		JspWriter stringJspWriter = jspAdapterFactory.getStringJspWriter();
		ScriptEncoderLiferayAjaxImpl.ScriptDataWriter scriptDataWriter =
			new ScriptEncoderLiferayAjaxImpl.ScriptDataWriter(stringJspWriter);
		PageContext stringPageContext = jspAdapterFactory.getStringPageContext(httpServletRequest, httpServletResponse,
				elContext, scriptDataWriter);
		ScriptTagUtil.flushScriptData(stringPageContext);
		requestMap.put(WebKeys.AUI_SCRIPT_DATA, savedScriptData);
		responseWriter.write(scriptDataWriter.toString());
	}

	private class ScriptDataWriter extends JspWriterWrapper {

		// Private Data Members
		private JspWriter wrappedStringJspWriter;

		public ScriptDataWriter(JspWriter stringJspWriter) {
			super(0, true);
			this.wrappedStringJspWriter = stringJspWriter;
		}

		@Override
		public void write(String string) throws IOException {

			if (!(string.startsWith("<script") || string.endsWith("script>"))) {
				super.write(string);
			}
		}

		@Override
		public JspWriter getWrapped() {
			return wrappedStringJspWriter;
		}
	}
}
