package org.oshepel.playwright.demo.tests;

import io.qameta.allure.*;
import org.oshepel.playwright.demo.pages.FormInputsPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Feature("Search")
public class FormInputsTest extends BaseUiTest {

    private FormInputsPage formInputsPage;

    @BeforeMethod
    @Override
    public void initAndOpenPage() {
        formInputsPage = new FormInputsPage();
        formInputsPage.open();
    }

    @DataProvider(name = "search")
    public Object[][] searchData() {
        return new Object [][] {
                {"CE"}, {"ce"}, {"cE"}
        };
    }

    @Issue("Jira ticket")
    @Test(description = "Search by name", dataProvider = "search")
    @Description("""
            - Open the form inputs page,
            - Select entries count,
            - Type into the search field a key word,
            - Validate the results.
            """)
    @Severity(SeverityLevel.CRITICAL)
    void testSearchByName(String searchKey) {
        formInputsPage
                .selectEntriesCount(25)
                .searchByName(searchKey)
                .exampleTableShouldHaveNames(searchKey);
    }
}
