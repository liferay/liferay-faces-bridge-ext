/**
 * Copyright (c) 2000-2019 Liferay, Inc. All rights reserved.
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

import javax.faces.context.ExternalContext;

import com.liferay.faces.util.factory.FactoryExtensionFinder;
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

	public static void verify(ExternalContext externalContext) {

		ProductFactory productFactory = (ProductFactory) FactoryExtensionFinder.getFactory(externalContext,
				ProductFactory.class);
		final Product LIFERAY_PORTAL = productFactory.getProductInfo(Product.Name.LIFERAY_PORTAL);
		Package bridgeExtPackage = BridgeExtDependencyVerifier.class.getPackage();
		String implementationTitle = bridgeExtPackage.getImplementationTitle();
		String implementationVersion = bridgeExtPackage.getImplementationVersion();
		final int LIFERAY_PORTAL_MAJOR_VERSION = LIFERAY_PORTAL.getMajorVersion();
		final int LIFERAY_PORTAL_MINOR_VERSION = LIFERAY_PORTAL.getMinorVersion();

		if (!((LIFERAY_PORTAL_MAJOR_VERSION == 7) && (LIFERAY_PORTAL_MINOR_VERSION <= 2))) {
			logger.error("{0} {1} is designed to be used with Liferay Portal 7.0.x/7.1.x/7.2.x but detected {2}.{3}",
				implementationTitle, implementationVersion, LIFERAY_PORTAL_MAJOR_VERSION, LIFERAY_PORTAL_MINOR_VERSION);
		}

		final Product JSF = productFactory.getProductInfo(Product.Name.JSF);
		final int JSF_MAJOR_VERSION = JSF.getMajorVersion();
		final int JSF_MINOR_VERSION = JSF.getMinorVersion();

		if (!((JSF_MAJOR_VERSION == 2) && (JSF_MINOR_VERSION == 2))) {
			logger.error("{0} {1} is designed to be used with JSF 2.2 but detected {2}.{3}", implementationTitle,
				implementationVersion, JSF_MAJOR_VERSION, JSF_MINOR_VERSION);
		}
	}
}
