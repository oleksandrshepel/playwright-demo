package org.oshepel.playwright.demo.pages.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;

public abstract class WebPageTable implements WebPageElement {

    protected final Page playwrightPage = getPlaywrightPage();

    public Locator getHeadersLocator() {
        return getRootLocator().locator("thead th");
    }

    public Locator getRowsLocator() {
        return getRootLocator().locator("tbody tr");
    }

    protected abstract String[] getHeaderValues();

    protected abstract Locator getRootLocator();

    public abstract WebPageTable shouldHaveHeaders();

    public abstract WebPageTable shouldHaveRows(List<? extends WebPageTableRow> expectedTableRows);

    protected String splitColumns(String row) {
        return row.replaceAll("\n", "").replaceAll("\t", "|");
    }
}
