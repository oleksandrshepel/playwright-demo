package org.oshepel.playwright.demo.pages;

import com.microsoft.playwright.Page;
import org.oshepel.playwright.demo.tests.BaseUiTest;

public interface PageObject {

	default Page getPlaywrightPage() {
		return BaseUiTest.getPlaywrightConfig().getPage();
	}
}