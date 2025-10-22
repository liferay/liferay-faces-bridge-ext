/**
 * Copyright (c) 2000-2025 Liferay, Inc. All rights reserved.
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
package com.liferay.faces.bridge.ext.util.internal;

/**
 * @author  Kyle Stiemann
 */
public final class TCCLUtil {

	private TCCLUtil() {
		throw new AssertionError();
	}

	public static ClassLoader getThreadContextClassLoaderOrDefault(Class<?> callingClass) {

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		if (classLoader == null) {
			classLoader = callingClass.getClassLoader();
		}

		return classLoader;
	}

	public static Class<?> loadClassFromContext(Class<?> callingClass, String className) throws ClassNotFoundException {

		ClassLoader classLoader = getThreadContextClassLoaderOrDefault(callingClass);

		return classLoader.loadClass(className);
	}
}
