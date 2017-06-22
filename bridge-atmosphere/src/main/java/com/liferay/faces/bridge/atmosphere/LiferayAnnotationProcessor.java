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
package com.liferay.faces.bridge.atmosphere;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.annotation.HandlesTypes;

import org.atmosphere.config.AtmosphereAnnotation;

import org.atmosphere.cpr.AnnotationHandler;
import org.atmosphere.cpr.AnnotationProcessor;
import org.atmosphere.cpr.AnnotationScanningServletContainerInitializer;
import org.atmosphere.cpr.AtmosphereConfig;
import org.atmosphere.cpr.AtmosphereFramework;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;

import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;


/**
 * @author  Kyle Stiemann
 */
public class LiferayAnnotationProcessor implements AnnotationProcessor {

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(LiferayAnnotationProcessor.class);

	// Private Constants
	private static final Set<Class<?>> ANNOTATIONS_HANDLED_BY_ATMOPSHERE;

	static {

		final Set<Class<?>> annotationsHandledByAtmosphere = new HashSet<Class<?>>();

		try {

			Class<?> annotationScanningServletContainerInitializerClass = Class.forName(
					AnnotationScanningServletContainerInitializer.class.getName());
			HandlesTypes handledTypes = annotationScanningServletContainerInitializerClass.getAnnotation(
					HandlesTypes.class);
			Class[] annotationsHandledByAtmoshpereArray = handledTypes.value();
			annotationsHandledByAtmosphere.addAll(Arrays.<Class<?>>asList(annotationsHandledByAtmoshpereArray));
		}
		catch (ClassNotFoundException e) {
			logger.error(e);
		}
		catch (NoClassDefFoundError e) {
			logger.error(e);
		}

		if (!annotationsHandledByAtmosphere.isEmpty()) {
			ANNOTATIONS_HANDLED_BY_ATMOPSHERE = Collections.unmodifiableSet(annotationsHandledByAtmosphere);
		}
		else {
			ANNOTATIONS_HANDLED_BY_ATMOPSHERE = Collections.emptySet();
		}
	}

	// Private Data
	private AnnotationHandler annotationHandler = new AnnotationHandler();
	private AtmosphereFramework atmosphereFramework;
	Set<Class<?>> customAtmosphereAnnotationClasses = new HashSet<Class<?>>();
	private boolean firstScan = true;

	@Override
	public void configure(AtmosphereConfig atmosphereConfig) {
		atmosphereFramework = atmosphereConfig.framework();
	}

	@Override
	public void destroy() {

		customAtmosphereAnnotationClasses.clear();
		customAtmosphereAnnotationClasses = null;
		annotationHandler.destroy();
		annotationHandler = null;
		atmosphereFramework = null;
	}

	@Override
	public AnnotationProcessor scan(File file) throws IOException {

		String directoryToScan = file.getPath();
		scanDirectory(directoryToScan, false);

		return this;
	}

	@Override
	public AnnotationProcessor scan(String packageToScan) throws IOException {

		String directoryToScan = getPackageDirectory(packageToScan);
		scanDirectory(directoryToScan, false);

		return this;
	}

	@Override
	public AnnotationProcessor scanAll() throws IOException {

		scanDirectory("/", true);

		return this;
	}

	private String getPackageDirectory(String packageString) {

		String packageDirectory = null;

		if (packageString != null) {
			packageDirectory = "/" + packageString.replace(".", "/") + "/";
		}

		return packageDirectory;
	}

	private void handleAnnotations(Set<Class<?>> annotationsToHandle, Class<?> clazz) {
		handleAnnotations(annotationsToHandle, clazz, null);
	}

	private void handleAnnotations(Set<Class<?>> annotationsToHandle, Class<?> clazz,
		Set<Class<?>> customAtmosphereAnnotationClasses) {

		Annotation[] classAnnotations = clazz.getAnnotations();

		if (classAnnotations != null) {

			for (Annotation annotation : classAnnotations) {

				Class<? extends Annotation> annotationType = annotation.annotationType();

				if (AtmosphereAnnotation.class.equals(annotationType)) {

					if (customAtmosphereAnnotationClasses != null) {

						AtmosphereAnnotation atmosphereAnnotation = (AtmosphereAnnotation) annotation;
						customAtmosphereAnnotationClasses.add(atmosphereAnnotation.value());
					}

					annotationHandler.handleProcessor(clazz);
				}

				if (annotationsToHandle.contains(annotationType)) {
					annotationHandler.handleAnnotation(atmosphereFramework, annotation.annotationType(), clazz);
				}
			}
		}
	}

	private void scanDirectory(String directoryToScan, boolean recurseIntoSubdirectories) throws IOException {

		// Find all Atmosphere built-in annotations before scanning for other annotations.
		if (firstScan) {

			String orgAtmospherePackageDirectory = getPackageDirectory("org.atmosphere");
			scanDirectory(ANNOTATIONS_HANDLED_BY_ATMOPSHERE, orgAtmospherePackageDirectory, true);
			firstScan = false;
		}

		scanDirectory(ANNOTATIONS_HANDLED_BY_ATMOPSHERE, directoryToScan, recurseIntoSubdirectories,
			customAtmosphereAnnotationClasses);
		scanDirectory(customAtmosphereAnnotationClasses, directoryToScan, recurseIntoSubdirectories);
	}

	private void scanDirectory(Set<Class<?>> annotationsToScanFor, String directoryToScan,
		boolean recurseIntoSubdirectories) throws IOException {
		scanDirectory(annotationsToScanFor, directoryToScan, recurseIntoSubdirectories, null);
	}

	private void scanDirectory(Set<Class<?>> annotationsToScanFor, String directoryToScan,
		boolean recurseIntoSubdirectories, Set<Class<?>> customAtmosphereAnnotationClasses) throws IOException {

		Bundle portletBundle = FrameworkUtil.getBundle(LiferayAnnotationProcessor.class);
		BundleWiring bundleWiring = portletBundle.adapt(BundleWiring.class);

		int bundleWiringListResourcesOption = BundleWiring.LISTRESOURCES_LOCAL;

		if (recurseIntoSubdirectories) {
			bundleWiringListResourcesOption = BundleWiring.LISTRESOURCES_RECURSE;
		}

		Collection<String> classResources = bundleWiring.listResources(directoryToScan, "*.class",
				bundleWiringListResourcesOption);

		for (String classResource : classResources) {

			try {

				URL urlClassResource = portletBundle.getResource(classResource);

				if (urlClassResource == null) {
					continue;
				}

				Class<?> clazz = portletBundle.loadClass(classResource.replaceAll("\\.class$", "").replace("/", "."));
				handleAnnotations(annotationsToScanFor, clazz, customAtmosphereAnnotationClasses);

				Field[] classDeclaredFields = clazz.getDeclaredFields();

				if (classDeclaredFields != null) {

					for (Field classDeclaredField : classDeclaredFields) {
						handleAnnotations(annotationsToScanFor, classDeclaredField.getType());
					}
				}
			}
			catch (ClassNotFoundException e) {
				// no-op
			}
			catch (NoClassDefFoundError e) {
				// no-op
			}
		}
	}
}
