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
package com.liferay.faces.bridge.context.liferay.internal;

import javax.faces.context.ResponseWriter;

import com.liferay.faces.bridge.context.BridgeContext;
import com.liferay.faces.bridge.context.HeadResponseWriter;
import com.liferay.faces.bridge.context.HeadResponseWriterFactory;


/**
 * @author  Neil Griffin
 */
public class HeadResponseWriterFactoryLiferayImpl extends HeadResponseWriterFactory {

	// Private Data Members
	private HeadResponseWriterFactory wrappedHeadResponseWriterFactory;

	public HeadResponseWriterFactoryLiferayImpl(HeadResponseWriterFactory headResponseWriterFactory) {
		this.wrappedHeadResponseWriterFactory = headResponseWriterFactory;
	}

	@Override
	public HeadResponseWriter getHeadResponseWriter(BridgeContext bridgeContext, ResponseWriter responseWriter) {
		return new HeadResponseWriterLiferayImpl(responseWriter);
	}

	@Override
	public HeadResponseWriterFactory getWrapped() {
		return wrappedHeadResponseWriterFactory;
	}
}
