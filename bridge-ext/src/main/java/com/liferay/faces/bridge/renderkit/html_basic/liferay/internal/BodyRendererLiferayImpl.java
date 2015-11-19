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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.render.RendererWrapper;

import com.liferay.faces.util.client.Script;
import com.liferay.faces.util.client.ScriptsEncoder;
import com.liferay.faces.util.client.ScriptsEncoderFactory;
import com.liferay.faces.util.factory.FactoryExtensionFinder;


/**
 * The purpose of this class is to relocate the scripts written by Non-Liferay/3rd-Party component suites which are
 * meant to be written immediately before the closing &lt;/body&gt; tag. In a portlet environment, these scripts would
 * be written immediately before the closing &lt;/div&gt; of the portlet's body, but Liferay provides the ability to
 * write these scripts at the bottom of the page. Therefore, {@link BodyRendererLiferayImpl} captures the scripts
 * written in {@link BodyRendererLiferayImpl#encodeEnd(javax.faces.context.FacesContext,
 * javax.faces.component.UIComponent)} via a {@link ScriptCapturingResponseWriter} and relocates them to the bottom of
 * the page.
 *
 * @author  Kyle Stiemann
 */
public class BodyRendererLiferayImpl extends RendererWrapper {

	// Private Members
	private Renderer wrappedBodyRenderer;

	public BodyRendererLiferayImpl(Renderer wrappedBodyRenderer) {
		this.wrappedBodyRenderer = wrappedBodyRenderer;
	}

	@Override
	public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {

		// Capture the scripts and encode all other markup normally.
		ResponseWriter responseWriter = facesContext.getResponseWriter();
		ScriptCapturingResponseWriter scriptCapturingResponseWriter = new ScriptCapturingResponseWriter(responseWriter);
		facesContext.setResponseWriter(scriptCapturingResponseWriter);
		super.encodeEnd(facesContext, uiComponent);
		facesContext.setResponseWriter(responseWriter);

		List<Script> scripts = scriptCapturingResponseWriter.getScripts();

		// If any scripts have been captured, relocate them to the bottom of the portal page (immediately before the
		// closing </body> tag).
		if (!scripts.isEmpty()) {

			// Normally it would be incorrect to encode these scripts outside of the portlet body, but Liferay provides
			// a method to encode scripts in the bottom of the portal page. Since ScriptsEncoderLiferayImpl calls
			// Liferay's method, it can be safely called outside the portlet body.
			ScriptsEncoderFactory ScriptsEncoderFactory = (ScriptsEncoderFactory) FactoryExtensionFinder.getFactory(
					ScriptsEncoderFactory.class);
			ScriptsEncoder ScriptsEncoder = ScriptsEncoderFactory.getScriptsEncoder();
			ScriptsEncoder.encodeBodyScripts(facesContext, scripts);
		}
	}

	@Override
	public Renderer getWrapped() {
		return wrappedBodyRenderer;
	}
}
