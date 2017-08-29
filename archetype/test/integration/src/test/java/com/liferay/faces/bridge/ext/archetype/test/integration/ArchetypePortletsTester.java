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
package com.liferay.faces.bridge.ext.archetype.test.integration;

import java.util.Locale;

import org.junit.Test;

import com.liferay.faces.test.selenium.browser.BrowserDriver;
import com.liferay.faces.test.selenium.browser.BrowserDriverManagingTesterBase;
import com.liferay.faces.test.selenium.browser.TestUtil;
import com.liferay.faces.test.selenium.browser.WaitingAsserter;


/**
 * @author  Kyle Stiemann
 */
public class ArchetypePortletsTester extends BrowserDriverManagingTesterBase {

	// Private Constants
	private String[] ARCHETYPE_LIBRARIES = {
			"Alloy", "ICEfaces", "jsf", "PrimeFaces", "RichFaces", "BootsFaces", "ButterFaces"
		};
	private String[] CONTAINER_CLASSES = {
			"alloy-panel", "ui-panel", "liferay-faces-bridge-body", "ui-panel", "rf-p", "panel-group",
			"butter-component"
		};

	@Test
	public void runArchetypePortletsTest() {

		BrowserDriver browserDriver = getBrowserDriver();
		WaitingAsserter waitingAsserter = getWaitingAsserter();

		for (int i = 0; i < ARCHETYPE_LIBRARIES.length; i++) {

			String archetypeLibraryLowerCase = ARCHETYPE_LIBRARIES[i].toLowerCase(Locale.ENGLISH);

			try {

				browserDriver.navigateWindowTo(TestUtil.DEFAULT_BASE_URL + "/group/archetypes/my-" +
					archetypeLibraryLowerCase);
				waitingAsserter.assertTextPresentInElement("Hello com.mycompany.my." + archetypeLibraryLowerCase +
					".portlet!", "//div[contains(@class,'" + CONTAINER_CLASSES[i] + "')]");

				if (!"jsf".equals(archetypeLibraryLowerCase)) {
					waitingAsserter.assertElementDisplayed("//li/em[contains(.,'" + ARCHETYPE_LIBRARIES[i] + "')]");
				}

				String facesImplName = System.getProperty("faces.impl.name");
				waitingAsserter.assertElementDisplayed("//li/em[contains(.,'" + facesImplName + "')]");
			}
			catch (AssertionError e) {
				throw new AssertionError(archetypeLibraryLowerCase + "-portlet failed:", e);
			}
			catch (Exception e) {
				throw new AssertionError(archetypeLibraryLowerCase + "-portlet tester threw an error:", e);
			}
		}
	}
}
