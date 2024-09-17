package org.oshepel.playwright.demo.pages.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;

import java.util.List;

public abstract class WebPageTable implements WebPageElement {

    protected final Page playwrightPage = getPlaywrightPage();

    public Locator getHeadersLocator() {
        return getRootLocator().locator("thead th");
    }

    public Locator getRowsLocator() {
        return getRootLocator().locator("tbody tr");
    }

    public void fillCell(Locator cellLocator, WebElementType elementType, String value) {
        switch (elementType) {
            case TEXTAREA -> {
                cellLocator.fill(value);
                PlaywrightAssertions.assertThat(cellLocator).hasText(value);
            }
            case INPUT -> {
                var locator = cellLocator.locator("input");
                locator.fill(value);
                PlaywrightAssertions.assertThat(locator).hasValue(value);
            }
            case SELECT -> {
                var locator = cellLocator.locator("select");
                locator.selectOption(value);
                PlaywrightAssertions.assertThat(locator).hasValue(value);
            }
        }

    }

    protected abstract String[] getHeaderValues();

    protected abstract Locator getRootLocator();

    public abstract WebPageTable shouldHaveHeaders();

    public abstract WebPageTable shouldHaveRows(List<? extends WebPageTableRow> expectedTableRows);
}
