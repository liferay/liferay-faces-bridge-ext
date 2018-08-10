/**
 * Copyright (c) 2000-2018 Liferay, Inc. All rights reserved.
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
import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitWrapper;
import javax.faces.render.Renderer;
import javax.faces.render.RendererWrapper;

import com.liferay.faces.bridge.ext.config.internal.LiferayPortletConfigParam;
import com.liferay.faces.util.lang.NameValuePair;
import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;


/**
 * @author  Kyle Stiemann
 */
@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public class ResourceRendererLiferayImpl extends RendererWrapper implements ComponentSystemEventListener, StateHolder {

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(ResourceRendererLiferayImpl.class);

	// Package-Private Constants
	/* package-private */ static final String DATA_SENNA_TRACK = "data-senna-track";

	// Private Data Members
	private String componentFamily;
	private String primeFacesCSSDefaultDataSennaTrackValue;
	private String rendererType;
	private boolean renderHeadResourceIds;
	private boolean transientFlag;
	private Renderer wrappedRenderer;

	/**
	 * This zero-arg constructor is required by the {@link javax.faces.component.StateHolderSaver} class during the
	 * RESTORE_VIEW phase of the JSF lifecycle. The reason why this class is involved in restoring state is because the
	 * {@link javax.faces.component.UIComponent.ComponentSystemEventListenerAdapter} implements {@link
	 * javax.faces.component.StateHolder} and will attempt to restore the state of any class in the restored view that
	 * implements {@link ComponentSystemEventListener}. It does this first by instantiating the class with a zero-arg
	 * constructor, and then calls the {@link #restoreState(FacesContext, Object)} method.
	 */
	public ResourceRendererLiferayImpl() {
		// Defer initialization of wrappedRenderer until restoreState(FacesContext, Object) is called.
	}

	public ResourceRendererLiferayImpl(Renderer wrappedRenderer, String componentFamily, String rendererType,
		boolean renderHeadResourceIds, String primeFacesCSSDefaultDataSennaTrackValue) {

		this.wrappedRenderer = wrappedRenderer;
		this.componentFamily = componentFamily;
		this.primeFacesCSSDefaultDataSennaTrackValue = primeFacesCSSDefaultDataSennaTrackValue;
		this.rendererType = rendererType;
		this.renderHeadResourceIds = renderHeadResourceIds;
	}

	/* package-private */ static NameValuePair<String, Object> getDataSennaTrack(UIComponent componentResource, boolean isCSS,
		String resourceLibrary, String primeFacesCSSDefaultDataSennaTrackValue) {

		NameValuePair<String, Object> dataSennaTrack = null;

		Map<String, Object> passThroughAttributes = componentResource.getPassThroughAttributes(false);

		if ((passThroughAttributes != null) && passThroughAttributes.containsKey(DATA_SENNA_TRACK)) {

			Object dataSennaTrackValue = passThroughAttributes.get(DATA_SENNA_TRACK);
			dataSennaTrack = new NameValuePair<String, Object>(DATA_SENNA_TRACK, dataSennaTrackValue);
		}
		else if (isCSS && (resourceLibrary != null) && resourceLibrary.startsWith("primefaces") &&
				!LiferayPortletConfigParam.NO_DEFAULT_VALUE.equals(primeFacesCSSDefaultDataSennaTrackValue)) {
			dataSennaTrack = new NameValuePair<String, Object>(DATA_SENNA_TRACK,
					primeFacesCSSDefaultDataSennaTrackValue);
		}

		return dataSennaTrack;
	}

	private static boolean isLiferayFacesBridgeInlineScript(UIComponent componentResource, String resourceId) {

		boolean liferayBridgeInlineScript = false;

		if ((resourceId != null) && resourceId.toLowerCase(Locale.ENGLISH).contains("inlinescript")) {

			Class<? extends UIComponent> clazz = componentResource.getClass();
			String className = clazz.getName();
			liferayBridgeInlineScript = className.startsWith("com.liferay.faces.bridge");
		}

		return liferayBridgeInlineScript;
	}

	private static boolean isRichFacesReslibResource(String resourceName, String resourceLibrary) {
		return ((resourceName != null) && resourceName.endsWith("reslib")) &&
			((resourceLibrary != null) && resourceLibrary.startsWith("org.richfaces"));
	}

	private static boolean isScriptResource(String resourceName, String resourceLibrary) {
		return ((resourceName != null) && (resourceName.endsWith("js") || resourceName.contains(".js?"))) ||
			isRichFacesReslibResource(resourceName, resourceLibrary);
	}

	private static boolean isStyleSheetResource(String resourceName) {
		return (resourceName != null) && (resourceName.endsWith("css") || resourceName.contains(".css?"));
	}

	@Override
	public void encodeEnd(FacesContext facesContext, UIComponent uiComponentResource) throws IOException {

		ResponseWriter responseWriter = null;

		if (HeadRendererLiferayImpl.isRenderingHeadSection(facesContext)) {

			Map<String, Object> componentResourceAttributes = uiComponentResource.getAttributes();
			String resourceName = (String) componentResourceAttributes.get("name");
			String resourceLibrary = (String) componentResourceAttributes.get("library");
			boolean cssResource = isStyleSheetResource(resourceName);
			NameValuePair<String, Object> dataSennaTrack = null;

			if ((cssResource || isScriptResource(resourceName, resourceLibrary)) &&
					!isLiferayFacesBridgeInlineScript(uiComponentResource, resourceName)) {

				dataSennaTrack = getDataSennaTrack(uiComponentResource, cssResource, resourceLibrary,
						primeFacesCSSDefaultDataSennaTrackValue);

				if (!renderHeadResourceIds) {

					resourceName = null;
					resourceLibrary = null;
				}
			}

			if ((resourceName != null) || (dataSennaTrack != null)) {

				responseWriter = facesContext.getResponseWriter();
				facesContext.setResponseWriter(new ResponseWriterHeadResourceLiferayImpl(responseWriter, dataSennaTrack,
						resourceName, resourceLibrary));
			}
		}

		// Ask the wrapped renderer to encode the resource.
		super.encodeEnd(facesContext, uiComponentResource);

		if (responseWriter != null) {

			// Restore the original response writer.
			facesContext.setResponseWriter(responseWriter);
		}
	}

	@Override
	public Renderer getWrapped() {
		return wrappedRenderer;
	}

	@Override
	public boolean isTransient() {
		return transientFlag;
	}

	/**
	 * Since the Mojarra {@link com.sun.faces.renderkit.html_basic.ScriptStyleBaseRenderer} class implements {@link
	 * ComponentSystemEventListener}, this class must implement that interface too, since this is a wrapper type of
	 * class. Mojarra uses this method to intercept {@link PostAddToViewEvent} in order to add script and link resources
	 * to the head (if the target attribute has a value of "head").
	 */
	@Override
	public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {

		// If the wrapped renderer has the ability to listen to component system events, then invoke the event
		// processing on the wrapped renderer. This is necessary when wrapping the Mojarra ScriptRenderer or
		// StylesheetRenderer because they extend ScriptStyleBaseRenderer which attempts to add the component
		// associated with the specified event as a resource on the view root.
		if (wrappedRenderer instanceof ComponentSystemEventListener) {

			ComponentSystemEventListener wrappedListener = (ComponentSystemEventListener) wrappedRenderer;
			wrappedListener.processEvent(event);
		}
		else {
			logger.debug("Wrapped renderer=[{0}] does not implement ComponentSystemEventListener", wrappedRenderer);
		}
	}

	@Override
	public void restoreState(FacesContext facesContext, Object state) {

		RendererInfo rendererInfo = (RendererInfo) state;
		this.renderHeadResourceIds = rendererInfo.renderHeadResourceIds;
		this.primeFacesCSSDefaultDataSennaTrackValue = rendererInfo.primeFacesCSSDefaultDataSennaTrackValue;

		if (wrappedRenderer == null) {

			RenderKit renderKit = facesContext.getRenderKit();

			while ((renderKit != null) && (renderKit instanceof RenderKitWrapper)) {

				RenderKitWrapper renderKitWrapper = (RenderKitWrapper) renderKit;
				renderKit = renderKitWrapper.getWrapped();

				if (renderKit instanceof RenderKitLiferayImpl) {

					renderKitWrapper = (RenderKitLiferayImpl) renderKit;
					renderKit = renderKitWrapper.getWrapped();

					break;
				}
			}

			if (renderKit != null) {
				this.wrappedRenderer = renderKit.getRenderer(rendererInfo.componentFamily, rendererInfo.rendererType);
			}
			else {
				logger.debug("Unable to restore wrapped renderer.");
			}
		}
	}

	@Override
	public Object saveState(FacesContext facesContext) {
		return new RendererInfo(componentFamily, rendererType, renderHeadResourceIds,
				primeFacesCSSDefaultDataSennaTrackValue);
	}

	@Override
	public void setTransient(boolean newTransientValue) {
		this.transientFlag = newTransientValue;
	}

	private static final class RendererInfo implements Serializable {

		private static final long serialVersionUID = 689219045319172269L;

		// Private Final Data Members
		private final String componentFamily;
		private final String primeFacesCSSDefaultDataSennaTrackValue;
		private final String rendererType;
		private final boolean renderHeadResourceIds;

		public RendererInfo(String componentFamily, String rendererType, boolean renderHeadResourceIds,
			String primeFacesCSSDefaultDataSennaTrackValue) {
			this.componentFamily = componentFamily;
			this.primeFacesCSSDefaultDataSennaTrackValue = primeFacesCSSDefaultDataSennaTrackValue;
			this.rendererType = rendererType;
			this.renderHeadResourceIds = renderHeadResourceIds;
		}
	}
}
