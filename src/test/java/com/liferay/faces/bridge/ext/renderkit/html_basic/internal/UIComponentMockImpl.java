/**
 * Copyright (c) 2000-2017 Liferay, Inc. All rights reserved.
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.render.Renderer;


/**
 * @author  Kyle Stiemann
 */
public class UIComponentMockImpl extends UIComponent {

	// Private Data Members
	private String componentFamily;
	private Map<String, Object> passThroughAttrs;
	private String rendererType;

	public UIComponentMockImpl(String rendererType) {
		this(rendererType, rendererType);
	}

	public UIComponentMockImpl(String componentFamily, String rendererType) {

		this.rendererType = rendererType;
		this.componentFamily = componentFamily;
	}

	public UIComponentMockImpl(String rendererType, boolean dataSennaOff) {

		this(rendererType);
		passThroughAttrs = new HashMap<String, Object>();
		passThroughAttrs.put("data-senna-off", Boolean.toString(dataSennaOff));
	}

	public UIComponentMockImpl(String componentFamily, String rendererType, boolean dataSennaOff) {

		this(rendererType, dataSennaOff);
		this.componentFamily = componentFamily;
	}

	@Override
	public void broadcast(FacesEvent event) throws AbortProcessingException {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void decode(FacesContext context) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void encodeChildren(FacesContext context) throws IOException {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException {
		throw new UnsupportedOperationException("");
	}

	@Override
	public UIComponent findComponent(String expr) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public Map<String, Object> getAttributes() {
		throw new UnsupportedOperationException("");
	}

	@Override
	public int getChildCount() {
		throw new UnsupportedOperationException("");
	}

	@Override
	public List<UIComponent> getChildren() {
		throw new UnsupportedOperationException("");
	}

	@Override
	public String getClientId(FacesContext context) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public UIComponent getFacet(String name) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public Map<String, UIComponent> getFacets() {
		throw new UnsupportedOperationException("");
	}

	@Override
	public Iterator<UIComponent> getFacetsAndChildren() {
		throw new UnsupportedOperationException("");
	}

	@Override
	public String getFamily() {
		return componentFamily;
	}

	@Override
	public String getId() {
		throw new UnsupportedOperationException("");
	}

	@Override
	public UIComponent getParent() {
		throw new UnsupportedOperationException("");
	}

	@Override
	public Map<String, Object> getPassThroughAttributes(boolean create) {
		return passThroughAttrs;
	}

	@Override
	public String getRendererType() {
		return rendererType;
	}

	@Override
	public boolean getRendersChildren() {
		throw new UnsupportedOperationException("");
	}

	@Override
	public ValueBinding getValueBinding(String name) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public boolean isRendered() {
		throw new UnsupportedOperationException("");
	}

	@Override
	public boolean isTransient() {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void processDecodes(FacesContext context) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void processRestoreState(FacesContext context, Object state) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public Object processSaveState(FacesContext context) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void processUpdates(FacesContext context) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void processValidators(FacesContext context) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void queueEvent(FacesEvent event) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void restoreState(FacesContext context, Object state) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public Object saveState(FacesContext context) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void setId(String id) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void setParent(UIComponent parent) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void setRendered(boolean rendered) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void setRendererType(String rendererType) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void setTransient(boolean newTransientValue) {
		throw new UnsupportedOperationException("");
	}

	@Override
	public void setValueBinding(String name, ValueBinding binding) {
		throw new UnsupportedOperationException("");
	}

	@Override
	protected void addFacesListener(FacesListener listener) {
		throw new UnsupportedOperationException("");
	}

	@Override
	protected FacesContext getFacesContext() {
		throw new UnsupportedOperationException("");
	}

	@Override
	protected FacesListener[] getFacesListeners(Class clazz) {
		throw new UnsupportedOperationException("");
	}

	@Override
	protected Renderer getRenderer(FacesContext context) {
		throw new UnsupportedOperationException("");
	}

	@Override
	protected void removeFacesListener(FacesListener listener) {
		throw new UnsupportedOperationException("");
	}
}
