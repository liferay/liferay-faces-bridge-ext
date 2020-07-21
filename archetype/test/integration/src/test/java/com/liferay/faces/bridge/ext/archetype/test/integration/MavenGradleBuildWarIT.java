/**
 * Copyright (c) 2000-2020 Liferay, Inc. All rights reserved.
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
package com.liferay.faces.bridge.ext.archetype.test.integration;

import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.apache.maven.shared.utils.cli.CommandLineException;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

import org.junit.Assert;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author  Kyle Stiemann
 */
public class MavenGradleBuildWarIT {

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(MavenGradleBuildWarIT.class);

	// Private Constants
	private static final Set<String> IGNORED_MAVEN_WAR_ENTRY_NAMES = Collections.unmodifiableSet(new HashSet<String>(
				Arrays.asList("pom.properties", "pom.xml")));
	private static final Set<String> IGNORED_GRADLE_WAR_ENTRY_NAMES = Collections.emptySet();
	//J-
	private static final String META_INF_MANIFEST_MF_CONTENTS_LOG_MESSAGE =
		"{} war META-INF/MANIFEST.MF file contents:\n" +
		"--------------------------------------------------------------------------------\n" +
		"{}\n" +
		"--------------------------------------------------------------------------------\n";
	//J+

	private static void addCleanUpHook(final CleanUpHook cleanUpHook) {

		Runtime runtime = Runtime.getRuntime();
		runtime.addShutdownHook(new Thread() {

				@Override
				public void run() {
					cleanUpHook.cleanUp();
				}
			});
	}

	private static boolean areZipEntriesEqual(String zipEntryName, ZipEntry zipEntry1, ZipEntry zipEntry2) {

		//J-
		return (!zipEntry1.isDirectory() && !zipEntry2.isDirectory()) &&
			(zipEntry1.getSize() > - 1 && (zipEntry1.getSize() == zipEntry2.getSize())) &&
			(isZipFile(zipEntryName) ||
				(zipEntry1.getCompressedSize() > -1 &&
					(zipEntry1.getCompressedSize() == zipEntry2.getCompressedSize()))) &&
			(zipEntry1.getCrc() > -1 && (zipEntry1.getCrc() == zipEntry2.getCrc()));
		//J+
	}

	private static void close(Closeable closeable) {

		if (closeable != null) {

			try {
				closeable.close();
			}
			catch (IOException e) {
				logger.error("Failed to close " + closeable.getClass(), e);
			}
		}
	}

	private static String getMETA_INF_MANIFEST_MFContents(ZipFile zipFile, ZipEntry manifestZipEntry)
		throws IOException {

		String contents = "";
		InputStream inputStream = null;
		Scanner scanner = null;

		try {

			inputStream = zipFile.getInputStream(manifestZipEntry);
			scanner = new Scanner(inputStream, StandardCharsets.UTF_8.toString());
			scanner.useDelimiter("\\A");

			if (scanner.hasNext()) {
				contents = scanner.next();
			}
		}
		finally {

			close(inputStream);
			close(scanner);
		}

		return contents;
	}

	private static String getTestArchetypeVersion(String defaultVersion, String archetypeType) {

		String version = System.getProperty("it." + archetypeType + ".archetype.version");

		if ((version == null) || "".equals(version)) {

			version = System.getProperty("it.archetype.version");

			if ((version == null) || "".equals(version)) {
				version = defaultVersion;
			}
		}

		return version;
	}

	private static Map<String, ZipEntry> getZipEntriesFromWar(ZipFile war) {

		Enumeration<? extends ZipEntry> warZipEntriesEnumeration = war.entries();
		Map<String, ZipEntry> warZipEntries = new HashMap<String, ZipEntry>();

		while (warZipEntriesEnumeration.hasMoreElements()) {

			ZipEntry warZipEntry = warZipEntriesEnumeration.nextElement();

			if (!warZipEntry.isDirectory()) {

				String warZipEntryName = warZipEntry.getName();
				warZipEntries.put(warZipEntryName, warZipEntry);
			}
		}

		return Collections.unmodifiableMap(warZipEntries);
	}

	private static ZipFile getZipFileFromWar(String generatedProjectName, File generatedProjectDirectory,
		String... warParentFolders) throws ZipException, ZipException, IOException {

		if (warParentFolders.length == 0) {
			throw new IllegalArgumentException("Please provide at least one war parent folder.");
		}

		File buildFolder = generatedProjectDirectory;

		for (String warParentFolder : warParentFolders) {
			buildFolder = new File(buildFolder, warParentFolder);
		}

		File[] builtWars = buildFolder.listFiles(new FilenameFilterBuiltWarImpl(generatedProjectName));
		Assert.assertEquals(1, builtWars.length);

		return new ZipFile(builtWars[0]);
	}

	private static boolean isZipFile(String name) {

		String lowerCaseName = name.toLowerCase(Locale.ENGLISH);

		return lowerCaseName.endsWith(".jar") || lowerCaseName.endsWith(".war") || lowerCaseName.endsWith(".ear") ||
			lowerCaseName.endsWith(".zip");
	}

	private static String toString(Set<String> set) {

		StringBuilder stringBuilder = new StringBuilder();

		for (String string : set) {

			if (stringBuilder.length() > 0) {
				stringBuilder.append(", ");
			}

			stringBuilder.append(string);
		}

		return stringBuilder.toString();
	}

	private static void verifyNoExtraFilesInWar(String warType, Set<String> minuend, Set<String> subtrahend,
		String archetypeProjectDirectoryName) {

		boolean mavenWar = warType.equals("maven");
		boolean gradleWar = warType.equals("gradle");
		Set<String> mutableWarZipEntryNames = new HashSet<String>(minuend);
		mutableWarZipEntryNames.removeAll(subtrahend);

		Iterator<String> iterator = mutableWarZipEntryNames.iterator();

		while (iterator.hasNext()) {

			String warZipEntryName = iterator.next();
			int indexOfFileNameWithoutPath = warZipEntryName.lastIndexOf("/") + 1;
			String warZipEntryNameWithoutPath = warZipEntryName.substring(indexOfFileNameWithoutPath);

			if ((mavenWar && IGNORED_MAVEN_WAR_ENTRY_NAMES.contains(warZipEntryNameWithoutPath)) ||
					(gradleWar && IGNORED_GRADLE_WAR_ENTRY_NAMES.contains(warZipEntryNameWithoutPath))) {
				iterator.remove();
				logger.info("Ignoring {} in {} war", warZipEntryName, warType);
			}
		}

		if (!mutableWarZipEntryNames.isEmpty()) {
			throw new AssertionError("The following file(s) were found only in the " + archetypeProjectDirectoryName +
				" " + warType + " war: " + toString(mutableWarZipEntryNames));
		}
	}

	@Test
	public void testMavenGradleGenerationDiff() throws IOException, XmlPullParserException, MavenInvocationException,
		CommandLineException {

		Reference<ProjectConnection> projectConnectionReference = new Reference<ProjectConnection>();
		Reference<ZipFile> mavenWarReference = new Reference<ZipFile>();
		Reference<ZipFile> gradleWarReference = new Reference<ZipFile>();
		Reference<Path> temporaryDirectoryReference = new Reference<Path>();
		temporaryDirectoryReference.set(Files.createTempDirectory("lfta"));
		addCleanUpHook(new CleanUpHook(projectConnectionReference, mavenWarReference, gradleWarReference,
				temporaryDirectoryReference));

		File temporaryDirectory = temporaryDirectoryReference.get().toFile();
		temporaryDirectory.deleteOnExit();

		File projectParentDirectory = new File("../../");
		File[] archetypeProjectDirectories = projectParentDirectory.listFiles(new FilenameFilterArchetypeProjectImpl());
		MavenXpp3Reader mavenXpp3Reader = new MavenXpp3Reader();
		Invoker invoker = new DefaultInvoker();

		for (File archetypeProjectDirectory : archetypeProjectDirectories) {

			File archetypePom = new File(archetypeProjectDirectory, "pom.xml");

			if (!archetypePom.exists()) {

				logger.info("Skipping tests for {} since no pom.xml was found for that project.",
					archetypeProjectDirectory.getName());

				continue;
			}

			// Programmatically run the following command:

			//J-
			// mvn --batch-mode archetype:generate \
			//	-DinteractiveMode=false
			//	-DarchetypeGroupId=com.liferay.faces.archetype \
			//	-DarchetypeArtifactId=com.liferay.faces.archetype.$ARCHETYPE_LIBRARY.portlet \
			//	-DarchetypeVersion=$LATEST_ARCHETYPE_SNAPSHOT_VERSION \
			//	-DgroupId=com.mycompany \
			//	-DartifactId=com.mycompany.my.$ARCHETYPE_LIBRARY.portlet \
			//J+
			FileReader archetypePomReader = new FileReader(archetypePom);
			Model model = mavenXpp3Reader.read(archetypePomReader);
			InvocationRequest generateProjectInvocationRequest = new DefaultInvocationRequest();
			generateProjectInvocationRequest.setBaseDirectory(temporaryDirectory);
			generateProjectInvocationRequest.setBatchMode(true);
			generateProjectInvocationRequest.setGoals(Collections.singletonList("archetype:generate"));

			Properties properties = new Properties();
			properties.setProperty("interactiveMode", "false");

			String version = model.getVersion();
			String archetypeProjectDirectoryName = archetypeProjectDirectory.getName();
			String archetypeType = archetypeProjectDirectoryName.replace("-portlet", "");
			version = getTestArchetypeVersion(version, archetypeType);
			properties.setProperty("archetypeVersion", version);
			properties.setProperty("archetypeGroupId", model.getGroupId());

			String archetypeArtifactId = model.getArtifactId();
			properties.setProperty("archetypeArtifactId", archetypeArtifactId);
			properties.setProperty("groupId", "com.mycompany");

			String generatedProjectName = "com.mycompany.my." + archetypeProjectDirectoryName.replace("-", ".");
			properties.setProperty("artifactId", generatedProjectName);
			generateProjectInvocationRequest.setProperties(properties);

			InvocationResult invocationResult = invoker.execute(generateProjectInvocationRequest);
			CommandLineException executionException = invocationResult.getExecutionException();
			int exitCode = invocationResult.getExitCode();

			if (executionException != null) {
				throw executionException;
			}
			else if (exitCode > 0) {
				throw new CommandLineException("Failed to generate archetype from " + archetypeArtifactId +
					". Build failed with exit code " + exitCode + ".");
			}

			// Programmatically run the following command: mvn clean package.
			InvocationRequest buildPortletInvocationRequest = new DefaultInvocationRequest();
			File generatedProjectDirectory = new File(temporaryDirectory, generatedProjectName);
			buildPortletInvocationRequest.setBaseDirectory(generatedProjectDirectory);
			buildPortletInvocationRequest.setBatchMode(true);
			buildPortletInvocationRequest.setGoals(Arrays.asList("clean", "package"));
			properties = new Properties();
			properties.setProperty("interactiveMode", "false");
			buildPortletInvocationRequest.setProperties(properties);
			invocationResult = invoker.execute(buildPortletInvocationRequest);
			executionException = invocationResult.getExecutionException();
			exitCode = invocationResult.getExitCode();

			if (executionException != null) {
				throw executionException;
			}
			else if (exitCode > 0) {
				throw new CommandLineException("Failed to generate archetype from " + archetypeArtifactId +
					". Build failed with exit code " + exitCode + ".");
			}

			mavenWarReference.set(getZipFileFromWar(generatedProjectName, generatedProjectDirectory, "target"));

			ZipFile mavenWar = mavenWarReference.get();

			Map<String, ZipEntry> mavenWarZipEntries = getZipEntriesFromWar(mavenWar);

			// Programmatically run the following command: gradle clean build.
			GradleConnector gradleConnector = GradleConnector.newConnector();
			gradleConnector.forProjectDirectory(generatedProjectDirectory);

			projectConnectionReference.set(gradleConnector.connect());

			ProjectConnection projectConnection = projectConnectionReference.get();

			BuildLauncher buildLauncher = projectConnection.newBuild();
			buildLauncher.setStandardError(System.err);
			buildLauncher.setStandardOutput(System.out);
			buildLauncher.forTasks("clean", "build");
			buildLauncher.run();
			projectConnection.close();
			projectConnectionReference.clear();

			gradleWarReference.set(getZipFileFromWar(generatedProjectName, generatedProjectDirectory, "build", "libs"));

			ZipFile gradleWar = gradleWarReference.get();

			Map<String, ZipEntry> gradleWarZipEntries = getZipEntriesFromWar(gradleWar);
			Set<String> mavenWarZipEntryNames = mavenWarZipEntries.keySet();
			int numberOfFilesInMavenWar = mavenWarZipEntryNames.size();
			Set<String> gradleWarZipEntryNames = gradleWarZipEntries.keySet();
			int numberOfFilesInGradleWar = gradleWarZipEntryNames.size();

			if (numberOfFilesInMavenWar != numberOfFilesInGradleWar) {

				verifyNoExtraFilesInWar("maven", mavenWarZipEntryNames, gradleWarZipEntryNames,
					archetypeProjectDirectoryName);
				verifyNoExtraFilesInWar("gradle", gradleWarZipEntryNames, mavenWarZipEntryNames,
					archetypeProjectDirectoryName);
			}

			Set<String> unequalWarZipEntryNames = new HashSet<String>();

			for (String mavenWarZipEntryName : mavenWarZipEntryNames) {

				ZipEntry gradleWarZipEntry = gradleWarZipEntries.get(mavenWarZipEntryName);

				if (gradleWarZipEntry == null) {

					// Already tested via verifyNoExtraFilesInWar() above;
					continue;
				}

				ZipEntry mavenWarZipEntry = mavenWarZipEntries.get(mavenWarZipEntryName);

				if ("META-INF/MANIFEST.MF".equals(mavenWarZipEntryName) &&
						!areZipEntriesEqual(mavenWarZipEntryName, mavenWarZipEntry, gradleWarZipEntry)) {

					logger.warn("Maven and gradle war META-INF/MANIFEST.MF files do not have the same contents.");

					String mavenManifestContents = getMETA_INF_MANIFEST_MFContents(mavenWar, mavenWarZipEntry);
					logger.info(META_INF_MANIFEST_MF_CONTENTS_LOG_MESSAGE, "Maven", mavenManifestContents);

					String gradleManifestContents = getMETA_INF_MANIFEST_MFContents(gradleWar, gradleWarZipEntry);
					logger.info(META_INF_MANIFEST_MF_CONTENTS_LOG_MESSAGE, "Gradle", gradleManifestContents);

					String manifestVersionRegex = "^\\s*Manifest-Version:\\s*1.0\n?[\\s\\S]*";
					boolean mavenManifestStartsWithManifestVersion = mavenManifestContents.matches(
							manifestVersionRegex);
					boolean gradleManifestStartsWithManifestVersion = gradleManifestContents.matches(
							manifestVersionRegex);
					String failingWarTypes = null;

					if (!mavenManifestStartsWithManifestVersion && !gradleManifestStartsWithManifestVersion) {
						failingWarTypes = "Maven and Gradle war";
					}
					else if (!mavenManifestStartsWithManifestVersion) {
						failingWarTypes = "Maven war";
					}
					else if (!gradleManifestStartsWithManifestVersion) {
						failingWarTypes = "Gradle war";
					}

					if (failingWarTypes != null) {
						throw new AssertionError(failingWarTypes +
							" META-INF/MANIFEST.MF file(s) do not contain \"Manifest-Version: 1.0\" as the first line.");
					}

					continue;
				}

				if (!areZipEntriesEqual(mavenWarZipEntryName, mavenWarZipEntry, gradleWarZipEntry)) {
					unequalWarZipEntryNames.add(mavenWarZipEntryName);
				}
			}

			mavenWar.close();
			mavenWarReference.clear();
			gradleWar.close();
			gradleWarReference.clear();
			Files.walkFileTree(generatedProjectDirectory.toPath(), new FileVisitorDeleteImpl());

			if (!unequalWarZipEntryNames.isEmpty()) {
				throw new AssertionError("The following file(s) were different between the " +
					archetypeProjectDirectoryName + " maven and gradle wars: " + toString(unequalWarZipEntryNames));
			}
		}
	}

	private static final class CleanUpHook {

		private final Reference<ProjectConnection> projectConnection;
		private final Reference<ZipFile> mavenWar;
		private final Reference<ZipFile> gradleWar;
		private final Reference<Path> temporaryDirectory;

		public CleanUpHook(Reference<ProjectConnection> projectConnection, Reference<ZipFile> mavenWar,
			Reference<ZipFile> gradleWar, Reference<Path> directory) {
			this.projectConnection = projectConnection;
			this.gradleWar = gradleWar;
			this.mavenWar = mavenWar;
			this.temporaryDirectory = directory;
		}

		public void cleanUp() {

			ProjectConnection projectConnection = this.projectConnection.get();

			if (projectConnection != null) {
				projectConnection.close();
			}

			close(mavenWar.get());
			close(gradleWar.get());

			try {
				Files.walkFileTree(temporaryDirectory.get(), new FileVisitorDeleteImpl());
			}
			catch (IOException e) {
				logger.error("Failed to delete temporary test file: " + temporaryDirectory.get().toString(), e);
			}
		}
	}

	private static final class FilenameFilterArchetypeProjectImpl implements FilenameFilter {

		// Private Final Data Members
		private final List<String> archetypeWhitelist;

		private FilenameFilterArchetypeProjectImpl() {

			String archetypeWhitelistString = System.getProperty("it.archetype.whitelist");
			List<String> archetypeWhitelist = new LinkedList<String>();

			if ((archetypeWhitelistString != null) && !"".equals(archetypeWhitelistString)) {

				for (String archetypeString : archetypeWhitelistString.split("[,]")) {

					String trimmedValue = archetypeString.trim();
					archetypeWhitelist.add(trimmedValue);
				}
			}

			this.archetypeWhitelist = Collections.unmodifiableList(archetypeWhitelist);
		}

		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith("-portlet") && isWhitelisted(name);
		}

		private boolean isWhitelisted(String name) {

			boolean whitelisted = archetypeWhitelist.isEmpty();

			for (String archetypeString : archetypeWhitelist) {

				if (name.contains(archetypeString)) {

					whitelisted = true;

					break;
				}
			}

			return whitelisted;
		}
	}

	private static final class FilenameFilterBuiltWarImpl implements FilenameFilter {

		private final String generatedProjectName;

		public FilenameFilterBuiltWarImpl(String generatedProjectName) {
			this.generatedProjectName = generatedProjectName;
		}

		public boolean accept(File dir, String name) {
			return name.startsWith(generatedProjectName) && name.endsWith(".war");
		}
	}

	private static final class FileVisitorDeleteImpl extends SimpleFileVisitor<Path> {

		private static FileVisitResult visitToDelete(Path path) {

			try {
				Files.delete(path);
			}
			catch (IOException e) {
				logger.error("Failed to delete temporary test file: " + path.toString(), e);
			}

			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path directory, IOException ioException) throws IOException {
			return visitToDelete(directory);
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			return visitToDelete(file);
		}
	}

	private static final class Reference<T> {

		private T t;

		public void clear() {
			set(null);
		}

		public T get() {
			return t;
		}

		public void set(T t) {
			this.t = t;
		}
	}
}
