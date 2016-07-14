package com.liferay.faces.test;

import java.util.logging.Level;

import org.junit.After;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


public class MyArtifactIdTester {

	private String guestUrl = "http://localhost:8080/web/guest";
	private String url;
	private WebClient webClient;

	@Test
	public void myArtifactIdTest() throws Exception {

		url = guestUrl + "/home";

		HtmlPage initialPage = webClient.getPage(url);
		webClient.waitForBackgroundJavaScript(100);

		String expectedText = "Hello myArtifactId!";
		String panelXpath = "//div[contains(@id,':panelId')]";

		HtmlDivision div = initialPage.getFirstByXPath(panelXpath);
		assertTrue("There should be a div on the page with an id that contains ':panelId', but there is no such div.",
			div != null);

		System.out.println("div.getTextContent() = " + div.getTextContent());
		assertTrue("div should contain text '" + expectedText + "', but contains '" + div.getTextContent() + "'",
			div.getTextContent().contains(expectedText));

	}

	@After
	public void tearDown() {
		webClient.close();
	}

	@Before
	public void setUp() {
		webClient = new WebClient(BrowserVersion.FIREFOX_38);
		webClient.getOptions().setJavaScriptEnabled(true);
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
	}

}

