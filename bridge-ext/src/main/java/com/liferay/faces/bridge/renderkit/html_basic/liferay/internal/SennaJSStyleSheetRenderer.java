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

import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.render.Renderer;
import javax.faces.render.RendererWrapper;

import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;


/**
 * @author  Kyle Stiemann
 */
@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public class SennaJSStyleSheetRenderer extends RendererWrapper implements ComponentSystemEventListener, StateHolder {

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(SennaJSStyleSheetRenderer.class);

	// Private Members
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
	public SennaJSStyleSheetRenderer() {
		// Defer initialization of wrappedRenderer until restoreState(FacesContext, Object) is called.
	}

	public SennaJSStyleSheetRenderer(Renderer wrappedRenderer) {
		this.wrappedRenderer = wrappedRenderer;
	}

	@Override
	public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {

		ResponseWriter responseWriter = facesContext.getResponseWriter();
		ResponseWriter sennaJSDisablingResponseWriter = new SennaJSStyleSheetResponseWriterImpl(responseWriter);
		facesContext.setResponseWriter(sennaJSDisablingResponseWriter);
		super.encodeBegin(facesContext, uiComponent);
		facesContext.setResponseWriter(responseWriter);
	}

	@Override
	public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {

		ResponseWriter responseWriter = facesContext.getResponseWriter();
		ResponseWriter sennaJSDisablingResponseWriter = new SennaJSStyleSheetResponseWriterImpl(responseWriter);
		facesContext.setResponseWriter(sennaJSDisablingResponseWriter);
		super.encodeChildren(facesContext, uiComponent);
		facesContext.setResponseWriter(responseWriter);
	}

	@Override
	public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {

		ResponseWriter responseWriter = facesContext.getResponseWriter();
		ResponseWriter sennaJSDisablingResponseWriter = new SennaJSStyleSheetResponseWriterImpl(responseWriter);
		facesContext.setResponseWriter(sennaJSDisablingResponseWriter);
		super.encodeEnd(facesContext, uiComponent);
		facesContext.setResponseWriter(responseWriter);
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

		if (wrappedRenderer == null) {

			try {
				String wrappedRendererClassName = (String) state;
				Class<?> wrappedRendererClass = Class.forName(wrappedRendererClassName);
				wrappedRenderer = (Renderer) wrappedRendererClass.newInstance();
			}
			catch (Exception e) {
				logger.error(e);
			}
		}
	}

	@Override
	public Object saveState(FacesContext facesContext) {
		return wrappedRenderer.getClass().getName();
	}

	@Override
	public boolean isTransient() {
		return transientFlag;
	}

	@Override
	public void setTransient(boolean transientFlag) {
		this.transientFlag = transientFlag;
	}

	@Override
	public Renderer getWrapped() {
		return wrappedRenderer;
	}
}
