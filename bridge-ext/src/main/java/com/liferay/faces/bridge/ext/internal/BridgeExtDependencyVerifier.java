/**
 * Copyright (c) 2000-2023 Liferay, Inc. All rights reserved.
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
package com.liferay.faces.bridge.ext.internal;

import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;
import com.liferay.faces.util.product.Product;
import com.liferay.faces.util.product.ProductFactory;


/**
 * @author  Neil Griffin
 */
public class BridgeExtDependencyVerifier {

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(BridgeExtDependencyVerifier.class);

	public static void verify() {

		Product liferayPortal = ProductFactory.getProduct(Product.Name.LIFERAY_PORTAL);
		Package bridgeExtPackage = BridgeExtDependencyVerifier.class.getPackage();
		String implementationTitle = bridgeExtPackage.getImplementationTitle();
		String implementationVersion = bridgeExtPackage.getImplementationVersion();
		int liferayPortalMajorVersion = liferayPortal.getMajorVersion();
		int liferayPortalMinorVersion = liferayPortal.getMinorVersion();

		if (!((liferayPortalMajorVersion == 7) && (liferayPortalMinorVersion == 4))) {
			logger.error("{0} {1} is designed to be used with Liferay Portal 7.4 but detected {2}.{3}",
				implementationTitle, implementationVersion, liferayPortalMajorVersion, liferayPortalMinorVersion);
		}

		Product jsf = ProductFactory.getProduct(Product.Name.JSF);
		int jsfMajorVersion = jsf.getMajorVersion();
		int jsfMinorVersion = jsf.getMinorVersion();

		if (!((jsfMajorVersion == 1) && (jsfMinorVersion == 2))) {
			logger.error("{0} {1} is designed to be used with JSF 1.2 but detected {2}.{3}", implementationTitle,
				implementationVersion, jsfMajorVersion, jsfMinorVersion);
		}
	}
}
