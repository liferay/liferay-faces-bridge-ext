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
package com.liferay.faces.bridge.ext.filter.internal;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;

import com.liferay.faces.util.helper.StringHelper;
import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;
import com.liferay.faces.util.product.Product;
import com.liferay.faces.util.product.ProductFactory;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.Portal;


/**
 * <p>This abstract class implements the {@link LiferayURLGenerator} contract for generating URLs that are compatible
 * with Liferay Portal. The main reason why this is necessary is because the Liferay Portal's
 * com.liferay.portlet.PortletURLImpl#toString() method returns different values depending on the portlet lifecycle
 * phase during which it is called. Additionally, it sometimes includes the public render parameters in the URL (which
 * are not required for JSF portlets). Another issue is related to ICEfaces, which uses a server-side DOM-diff algorithm
 * within it's {@link RenderKit}. When DOM-diff strategies are used, it is critical that URLs always be the same
 * regardless of which portlet lifecycle phase is executing. For example, a URL generated during the {@link
 * PortletRequest#RENDER_PHASE} of the portlet lifecycle must be identical to the one generated during the {@link
 * PortletRequest#RESOURCE_PHASE}, or else a DOM-diff will be detected.</p>
 *
 * <p>The constructors of this class receive a String-based parameter named "baseURL" which is expected to be the result
 * of calling the toString() method of a Liferay URL. The baseURL is then parsed and the URL parameters are cached so
 * that the {@link #generateURL(Map)} method can quickly generate a consistent URL.</p>
 *
 * @author  Neil Griffin
 */
public abstract class LiferayURLGeneratorBaseImpl implements LiferayURLGenerator {

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(LiferayURLGeneratorBaseImpl.class);

	// Private Constants
	private static final String CONTROL_PANEL_CATEGORY = "controlPanelCategory";
	private static final String DO_AS_GROUP_ID = "doAsGroupId";
	private static final String DO_AS_USER_ID = "doAsUserId";
	private static final String DO_AS_USER_LANGUAGE_ID = "doAsUserLanguageId";
	private static final int LIFERAY_BUILD_NUMBER = ProductFactory.getProduct(Product.Name.LIFERAY_PORTAL).getBuildId();
	private static final String P_AUTH = "p_auth";
	private static final String P_L_ID = "p_l_id";
	private static final String P_P_AUTH = "p_p_auth";
	private static final String P_P_CACHEABILITY = "p_p_cacheability";
	private static final String P_P_COL_ID = "p_p_col_id";
	private static final String P_P_COL_POS = "p_p_col_pos";
	private static final String P_P_COL_COUNT = "p_p_col_count";
	private static final String P_P_ID = "p_p_id";
	private static final String P_P_MODE = "p_p_mode";
	private static final String P_P_LIFECYCLE = "p_p_lifecycle";
	private static final String P_O_P_ID = "p_o_p_id";
	private static final String P_P_RESOURCE_ID = "p_p_resource_id";
	private static final String P_P_STATE = "p_p_state";
	private static final String P_P_STATE_RCV = "p_p_state_rcv";
	private static final String REFERER_GROUP_ID = "refererGroupId";
	private static final String REFERER_PLID = "refererPlid";

	// Protected Constants
	protected static final String LIFECYCLE_RESOURCE_PHASE_ID = "2";

	private static final ArrayList<String> LIFERAY_NON_NAMESPACED_PARAMS = new ArrayList<String>(5);

	static {
		LIFERAY_NON_NAMESPACED_PARAMS.add(P_AUTH);
		LIFERAY_NON_NAMESPACED_PARAMS.add(P_L_ID);
		LIFERAY_NON_NAMESPACED_PARAMS.add(P_P_AUTH);
		LIFERAY_NON_NAMESPACED_PARAMS.add(P_P_ID);
		LIFERAY_NON_NAMESPACED_PARAMS.add(P_P_STATE);
		LIFERAY_NON_NAMESPACED_PARAMS.add(P_P_STATE_RCV);
		LIFERAY_NON_NAMESPACED_PARAMS.add(P_P_MODE);
		LIFERAY_NON_NAMESPACED_PARAMS.add(P_P_CACHEABILITY);
		LIFERAY_NON_NAMESPACED_PARAMS.add(P_O_P_ID);
		LIFERAY_NON_NAMESPACED_PARAMS.add(DO_AS_USER_ID);
		LIFERAY_NON_NAMESPACED_PARAMS.add(DO_AS_USER_LANGUAGE_ID);
		LIFERAY_NON_NAMESPACED_PARAMS.add(DO_AS_GROUP_ID);
		LIFERAY_NON_NAMESPACED_PARAMS.add(REFERER_GROUP_ID);
		LIFERAY_NON_NAMESPACED_PARAMS.add(REFERER_PLID);
		LIFERAY_NON_NAMESPACED_PARAMS.add(CONTROL_PANEL_CATEGORY);
	}

	// Private Final Data Members
	private final String encoding;

	// Private Data Members
	private String baseURL;
	private Map<String, String> parameterMap;
	private PortletMode initialPortletMode;
	private String prefix;
	private String responseNamespace;
	private WindowState initialWindowState;
	private List<LiferayURLParameter> wsrpParameters;
	private String portletURLAnchor;

	/**
	 * Constructs a new instance.
	 *
	 * @param  baseURL            The String-based URL generated by Liferay Portal's PortletURLImpl#toString() method.
	 * @param  responseNamespace  The response namespace.
	 */
	public LiferayURLGeneratorBaseImpl(String baseURL, String responseNamespace, String encoding) {
		this(baseURL, responseNamespace, null, null, encoding);
	}

	/**
	 * Constructs a new instance.
	 *
	 * @param  portletURL         The String-based URL generated by Liferay Portal's
	 *                            com.liferay.portlet.PortletURLImpl#toString() method. method.
	 * @param  responseNamespace  The response namespace.
	 */
	public LiferayURLGeneratorBaseImpl(PortletURL portletURL, String responseNamespace, String encoding) {
		this(portletURL.toString(), responseNamespace, portletURL.getPortletMode(), portletURL.getWindowState(),
			encoding);
	}

	private LiferayURLGeneratorBaseImpl(String baseURL, String responseNamespace, PortletMode portletMode,
		WindowState windowState, String encoding) {

		this.baseURL = baseURL;
		this.responseNamespace = responseNamespace;
		this.initialPortletMode = portletMode;
		this.initialWindowState = windowState;
		this.encoding = encoding;
		parse();
	}

	public static String encodeParameterNameOrValue(String nameOrValue, String encoding) throws UnsupportedEncodingException {

		String encodedNameOrValue = nameOrValue;

		if (nameOrValue != null) {
			encodedNameOrValue = URLEncoder.encode(nameOrValue, encoding);
		}

		return encodedNameOrValue;
	}

	public String generateURL(Map<String, String[]> additionalParameterMap) {
		return generateURL(additionalParameterMap, null, null, null, null);
	}

	public String generateURL(Map<String, String[]> additionalParameterMap, String cacheability, String resourceId) {
		return generateURL(additionalParameterMap, cacheability, null, resourceId, null);
	}

	public String generateURL(Map<String, String[]> additionalParameterMap, PortletMode portletMode,
		WindowState windowState) {
		return generateURL(additionalParameterMap, null, portletMode, null, windowState);
	}

	public String generateURL(Map<String, String[]> additionalParameterMap, String cacheability,
		PortletMode portletMode, String resourceId, WindowState windowState) {

		String toStringValue;

		if (baseURL.contains(Portal.FRIENDLY_URL_SEPARATOR)) {
			toStringValue = baseURL;
		}
		else {

			StringBuilder url = new StringBuilder();

			// Build up a new URL string based on the one returned by Liferay, but discard everything after the
			// question mark because it's filled with all kinds of unnecessary stuff.
			url.append(prefix);

			// Possibly add the p_auth parameter.
			boolean firstParameter = true;

			String portalAuthToken = StringHelper.toString(additionalParameterMap.get(P_AUTH),
					parameterMap.get(P_AUTH));

			if (portalAuthToken != null) {

				appendParameterToURL(firstParameter, P_AUTH, portalAuthToken, url);
				firstParameter = false;
			}

			// Possibly add the p_l_id parameter.
			String plid = StringHelper.toString(additionalParameterMap.get(P_L_ID), parameterMap.get(P_L_ID));

			if (plid != null) {

				appendParameterToURL(firstParameter, P_L_ID, plid, url);
				firstParameter = false;
			}

			// Possibly add the p_p_auth parameter.
			String portletAuthToken = StringHelper.toString(additionalParameterMap.get(P_P_AUTH),
					parameterMap.get(P_P_AUTH));

			if (portletAuthToken != null) {

				boolean addPortletAuthToken = true;

				if ((LIFERAY_BUILD_NUMBER < 6102) || ((LIFERAY_BUILD_NUMBER > 6102) && (LIFERAY_BUILD_NUMBER < 6130))) {

					// Versions of Liferay Portal prior to 6.1.2-CE/6.1.30-EE suffered from LPS-36481 which caused
					// PortletURLImpl.addPortletAuthToken(StringBundle, Key) method to add the p_p_auth parameter to
					// URLs for portlets when add-default-resource=false. It is therefore necessary to check that
					// add-default-resource=true before adding the p_p_auth parameter to the URL.
					FacesContext facesContext = FacesContext.getCurrentInstance();
					PortletRequest portletRequest = (PortletRequest) facesContext.getExternalContext().getRequest();
					String portletId = (String) portletRequest.getAttribute(WebKeys.PORTLET_ID);
					ThemeDisplay themeDisplay = (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);

					try {
						Portlet portlet = PortletLocalServiceUtil.getPortletById(themeDisplay.getCompanyId(),
								portletId);
						addPortletAuthToken = portlet.isAddDefaultResource();
					}
					catch (SystemException e) {
						logger.error(e);
					}
				}

				if (addPortletAuthToken) {
					appendParameterToURL(firstParameter, P_P_AUTH, portletAuthToken, url);
					firstParameter = false;
				}
			}

			// Always add the p_p_id parameter
			String parameterValue = StringHelper.toString(additionalParameterMap.get(P_P_ID), responseNamespace);

			if (parameterValue.startsWith("_")) {
				parameterValue = parameterValue.substring(1);
			}

			if (parameterValue.endsWith("_")) {
				parameterValue = parameterValue.substring(0, parameterValue.length() - 1);
			}

			appendParameterToURL(firstParameter, P_P_ID, parameterValue, url);

			firstParameter = false;

			// Always add the p_p_lifecycle parameter.
			String portletLifecycleId = getPortletLifecycleId();
			appendParameterToURL(P_P_LIFECYCLE, portletLifecycleId, url);

			// Add the p_p_state parameter.
			FacesContext facesContext = FacesContext.getCurrentInstance();
			Map<String, Object> applicationMap = facesContext.getExternalContext().getApplicationMap();

			WindowState urlWindowState = initialWindowState;

			if (additionalParameterMap.get(P_P_STATE) != null) {
				urlWindowState = new WindowState(additionalParameterMap.get(P_P_STATE)[0]);
			}

			if (windowState != null) {
				urlWindowState = windowState;
			}

			if (urlWindowState == null) {
				parameterValue = (String) applicationMap.get(responseNamespace + P_P_STATE);
			}
			else {
				parameterValue = urlWindowState.toString();
			}

			appendParameterToURL(P_P_STATE, parameterValue, url);

			// Possibly add the p_p_state_rcv parameter.
			String stateRestoreCurrentView = StringHelper.toString(additionalParameterMap.get(P_P_STATE_RCV),
					parameterMap.get(P_P_STATE_RCV));

			if (stateRestoreCurrentView != null) {
				appendParameterToURL(P_P_STATE_RCV, stateRestoreCurrentView, url);
			}

			// Add the p_p_mode parameter.
			PortletMode urlPortletMode = initialPortletMode;

			if (additionalParameterMap.get(P_P_MODE) != null) {
				urlPortletMode = new PortletMode(additionalParameterMap.get(P_P_MODE)[0]);
			}

			if (portletMode != null) {
				urlPortletMode = portletMode;
			}

			if (urlPortletMode == null) {
				parameterValue = (String) applicationMap.get(responseNamespace + P_P_MODE);
			}
			else {
				parameterValue = urlPortletMode.toString();
			}

			appendParameterToURL(P_P_MODE, parameterValue, url);

			// Possibly add the p_p_cacheability parameter
			if (LIFECYCLE_RESOURCE_PHASE_ID.equals(portletLifecycleId)) {

				String urlCacheability = null;

				if (cacheability != null) {
					urlCacheability = cacheability;
				}

				if (urlCacheability == null) {
					urlCacheability = StringHelper.toString(additionalParameterMap.get(P_P_CACHEABILITY),
							parameterMap.get(P_P_CACHEABILITY));
				}

				if (urlCacheability != null) {
					appendParameterToURL(P_P_CACHEABILITY, urlCacheability, url);
				}
			}

			// Always add the p_p_col_id parameter
			parameterValue = (String) applicationMap.get(responseNamespace + P_P_COL_ID);
			appendParameterToURL(P_P_COL_ID, parameterValue, url);

			// Possibly add the p_p_col_count parameter.
			parameterValue = (String) applicationMap.get(responseNamespace + P_P_COL_COUNT);
			appendParameterToURL(P_P_COL_COUNT, parameterValue, url);

			// Add the p_p_col_pos parameter if it is greater than zero (same logic as Liferay's
			// PortletURLImpl.toString())
			parameterValue = (String) applicationMap.get(responseNamespace + P_P_COL_POS);

			if ((parameterValue != null) && (parameterValue.length() > 0)) {

				try {
					int colPos = Integer.parseInt(parameterValue);

					if (colPos > 0) {
						appendParameterToURL(P_P_COL_POS, parameterValue, url);
					}
				}
				catch (NumberFormatException e) {
					// ignore
				}
			}

			// Possibly add the p_o_p_id parameter.
			String outerPortletId = StringHelper.toString(additionalParameterMap.get(P_O_P_ID),
					parameterMap.get(P_O_P_ID));

			if (outerPortletId != null) {
				appendParameterToURL(P_O_P_ID, outerPortletId, url);
			}

			// Possibly add the doAsUserId parameter.
			String doAsUserId = StringHelper.toString(additionalParameterMap.get(DO_AS_USER_ID),
					parameterMap.get(DO_AS_USER_ID));

			if (doAsUserId != null) {
				appendParameterToURL(DO_AS_USER_ID, doAsUserId, url);
			}

			// Possibly add the doAsUserLanguageId parameter.
			String doAsUserLanguageId = StringHelper.toString(additionalParameterMap.get(DO_AS_USER_LANGUAGE_ID),
					parameterMap.get(DO_AS_USER_LANGUAGE_ID));

			if (doAsUserLanguageId != null) {
				appendParameterToURL(DO_AS_USER_LANGUAGE_ID, doAsUserLanguageId, url);
			}

			// Possibly add the doAsGroupId parameter.
			String doAsGroupId = StringHelper.toString(additionalParameterMap.get(DO_AS_GROUP_ID),
					parameterMap.get(DO_AS_GROUP_ID));

			if (doAsGroupId != null) {
				appendParameterToURL(DO_AS_GROUP_ID, doAsGroupId, url);
			}

			// Possibly add the refererGroupId parameter.
			String refererGroupId = StringHelper.toString(additionalParameterMap.get(REFERER_GROUP_ID),
					parameterMap.get(REFERER_GROUP_ID));

			if (refererGroupId != null) {
				appendParameterToURL(REFERER_GROUP_ID, refererGroupId, url);
			}

			// Possibly add the refererPlid parameter.
			String refererPlid = StringHelper.toString(additionalParameterMap.get(REFERER_PLID),
					parameterMap.get(REFERER_PLID));

			if (refererPlid != null) {
				appendParameterToURL(REFERER_PLID, refererPlid, url);
			}

			// Possibly add the controlPanelCategory parameter.
			String controlPanelCategory = StringHelper.toString(additionalParameterMap.get(CONTROL_PANEL_CATEGORY),
					parameterMap.get(CONTROL_PANEL_CATEGORY));

			if (controlPanelCategory != null) {
				appendParameterToURL(CONTROL_PANEL_CATEGORY, controlPanelCategory, url);
			}

			// Add request parameters from the request parameter map.
			boolean namespaced = !responseNamespace.startsWith("wsrp");

			Set<Map.Entry<String, String[]>> mapEntries = additionalParameterMap.entrySet();

			if (mapEntries != null) {

				for (Map.Entry<String, String[]> mapEntry : mapEntries) {
					String[] parameterValues = mapEntry.getValue();

					if (parameterValues != null) {

						String parameterName = mapEntry.getKey();

						if (!LIFERAY_NON_NAMESPACED_PARAMS.contains(parameterName)) {

							for (String curParameterValue : parameterValues) {

								if (curParameterValue != null) {
									appendParameterToURL(firstParameter, namespaced, parameterName, curParameterValue,
										url);
								}
							}
						}
					}
				}
			}

			// Add WSRP URL parameters
			for (LiferayURLParameter wsrpParameter : wsrpParameters) {

				appendParameterToURL(wsrpParameter.getName(), wsrpParameter.getValue(), url);
			}

			// Possibly add the p_p_resource_id parameter.
			String urlResourceId = parameterMap.get(P_O_P_ID);

			if (resourceId != null) {
				urlResourceId = resourceId;
			}

			if (urlResourceId == null) {

				if (prefix.startsWith("wsrp")) {
					appendParameterToURL(P_P_RESOURCE_ID, "wsrp", url);
				}
			}
			else {
				appendParameterToURL(P_P_RESOURCE_ID, urlResourceId, url);
			}

			// Possibly add a Portlet URL Anchor
			if (portletURLAnchor != null) {
				url.append(portletURLAnchor);
			}

			toStringValue = url.toString();
		}

		return toStringValue;
	}

	protected void appendParameterToURL(String parameterName, String parameterValue, StringBuilder url) {
		appendParameterToURL(false, false, parameterName, parameterValue, url);
	}

	protected void appendParameterToURL(boolean firstParameter, String parameterName, String parameterValue,
		StringBuilder url) {
		appendParameterToURL(firstParameter, false, parameterName, parameterValue, url);
	}

	protected void appendParameterToURL(boolean firstParameter, boolean namespaced, String parameterName,
		String parameterValue, StringBuilder url) {

		try {

			parameterName = encodeParameterNameOrValue(parameterName, encoding);

			if (!firstParameter) {
				url.append("&");
			}

			if (namespaced) {
				url.append(responseNamespace);
			}

			parameterValue = encodeParameterNameOrValue(parameterValue, encoding);

			url.append(parameterName);
			url.append("=");
			url.append(parameterValue);

			logger.debug("Appended param to URL name=[{0}] parameterValue=[{1}]", parameterName, parameterValue);
		}
		catch (UnsupportedEncodingException e) {

			logger.error("Unable to encode parameter name=\"{0}\" and value=\"{1}\" with encoding \"{2}\".",
				parameterName, parameterValue, encoding);
			logger.error(e);
		}
	}

	protected void parse() {

		parameterMap = new HashMap<String, String>();
		wsrpParameters = new ArrayList<LiferayURLParameter>();

		String queryString = baseURL;
		int queryPos = baseURL.indexOf("?");

		if (queryPos > 0) {
			prefix = baseURL.substring(0, queryPos + 1);
			queryString = baseURL.substring(queryPos + 1);
		}

		String[] nameValuePairs = queryString.split("[&]");

		if (nameValuePairs != null) {

			for (String nameValuePair : nameValuePairs) {

				int equalsPos = nameValuePair.indexOf("=");

				if (equalsPos > 0) {

					String name = nameValuePair.substring(0, equalsPos);
					String value = nameValuePair.substring(equalsPos + 1);

					if (nameValuePair.startsWith("wsrp")) {
						LiferayURLParameter liferayUrlParameter = new LiferayURLParameter(name, value);
						wsrpParameters.add(liferayUrlParameter);
					}
					else {
						parameterMap.put(name, value);
					}
				}
			}
		}

		int pos = baseURL.indexOf("#");

		if (pos > 0) {
			portletURLAnchor = baseURL.substring(pos);
		}

	}
}
