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
import java.io.Writer;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;

import org.w3c.dom.Node;

import com.liferay.faces.bridge.ext.context.internal.JSPSupportServlet;
import com.liferay.faces.bridge.ext.util.internal.XMLUtil;
import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;

import com.liferay.portal.util.PortalUtil;

import com.liferay.taglib.util.HtmlTopTag;


/**
 * Custom {@link ResponseWriter} that has the ability to write to the &lt;head&gt;...&lt;/head&gt; section of the
 * liferay portal page.
 *
 * @author  Neil Griffin
 */
public class HeadResponseWriterLiferayImpl extends HeadResponseWriterLiferayCompatImpl {

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(HeadResponseWriterLiferayImpl.class);

	public HeadResponseWriterLiferayImpl(ResponseWriter wrappedResponseWriter) {
		super(wrappedResponseWriter);
	}

	@Override
	public Writer append(CharSequence csq) throws IOException {

		if (csq != null) {

			String text = csq.toString();

			if ("<![CDATA[".equalsIgnoreCase(text.toUpperCase(Locale.ENGLISH))) {
				startCDATA();
			}
			else if ("]]>".equalsIgnoreCase(text)) {
				endCDATA();
			}
			else {
				addNodeToHeadSection(Node.TEXT_NODE, csq);
			}
		}

		return this;
	}

	@Override
	public void endCDATA() throws IOException {

		Node currentNode = getCurrentNode();

		if (currentNode.getNodeType() != Node.CDATA_SECTION_NODE) {
			throw new IllegalArgumentException("ResponseWriter.endCDATA() called before startCDATA().");
		}

		Node parentNode = currentNode.getParentNode();

		if (parentNode != null) {
			setCurrentNode(parentNode);
		}
		else {

			writeNodeToHeadSection(currentNode, null);
			setCurrentNode(null);
		}
	}

	@Override
	public void startCDATA() throws IOException {

		Node currentNode = getCurrentNode();
		Node cdataNode = new NodeImpl(Node.CDATA_SECTION_NODE, currentNode);

		if (currentNode != null) {

			if (Node.CDATA_SECTION_NODE == currentNode.getNodeType()) {
				throw new IllegalStateException("CDATA cannot be nested.");
			}

			currentNode.appendChild(cdataNode);
		}

		setCurrentNode(cdataNode);
	}

	@Override
	public void writeComment(Object comment) throws IOException {

		if (comment != null) {
			addNodeToHeadSection(Node.COMMENT_NODE, XMLUtil.escapeXML(comment.toString()));
		}
	}

	@Override
	protected Node createElement(String nodeName) {
		return new ElementImpl(nodeName, getCurrentNode());
	}

	@Override
	protected void writeNodeToHeadSection(Node node, UIComponent componentResource) throws IOException {

		// Get the underlying HttpServletRequest and HttpServletResponse
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		PortletRequest portletRequest = (PortletRequest) externalContext.getRequest();
		HttpServletRequest httpServletRequest = PortalUtil.getHttpServletRequest(portletRequest);
		PortletResponse portletResponse = (PortletResponse) externalContext.getResponse();
		HttpServletResponse httpServletResponse = PortalUtil.getHttpServletResponse(portletResponse);

		// Invoke the Liferay HtmlTopTag class directly (rather than using liferay-util:html-top from a JSP).
		HtmlTopTag htmlTopTag = new HtmlTopTag();
		JspFactory jspFactory = JspFactory.getDefaultFactory();
		ServletContext servletContext = getServletContext(httpServletRequest);
		JSPSupportServlet jspSupportServlet = new JSPSupportServlet(servletContext);
		PageContext pageContext = jspFactory.getPageContext(jspSupportServlet, httpServletRequest, httpServletResponse,
				null, false, 0, false);
		htmlTopTag.setPageContext(pageContext);
		htmlTopTag.doStartTag();

		String nodeAsString;

		if (isElement(node)) {
			nodeAsString = XMLUtil.elementToString(node, true);
		}
		else {
			nodeAsString = XMLUtil.nodeToString(node);
		}

		BodyContent bodyContent = pageContext.pushBody();
		bodyContent.print(nodeAsString);
		htmlTopTag.setBodyContent(bodyContent);

		try {
			htmlTopTag.doEndTag();
		}
		catch (Exception e) {
			throw new IOException(e);
		}

		jspFactory.releasePageContext(pageContext);

		if (logger.isDebugEnabled()) {
			logger.debug("Added resource to Liferay's <head>...</head> section, node=[{0}]", getNodeInfo(node));
		}
	}

	private void addNodeToHeadSection(short nodeType, Object text) throws IOException {

		Node currentNode = getCurrentNode();

		if (currentNode != null) {
			currentNode.appendChild(new NodeImpl(nodeType, text.toString(), currentNode));
		}
		else {
			writeNodeToHeadSection(new NodeImpl(nodeType, text.toString(), null), null);
		}
	}
}
