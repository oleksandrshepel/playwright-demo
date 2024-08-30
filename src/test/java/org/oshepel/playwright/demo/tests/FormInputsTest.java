package org.oshepel.playwright.demo.tests;

import io.qameta.allure.*;
import org.oshepel.playwright.demo.pages.FormInputsPage;
import org.oshepel.playwright.demo.testdata.TestDataStep;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

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
        return new Object[][]{
                {"CE"}, {"ce"}, {"cE"}
        };
    }

    @Issue("Jira ticket")
    @Test(description = "Search by name", dataProvider = "search")
    @Description("""
            - Open the form inputs page,
            - Select entries count,
            - Type into the search field a key word,
            - Validate the employee table headers,
            - Validate the employee table contains only rows that have the key word in the name column.
            """)
    @Severity(SeverityLevel.CRITICAL)
    void testSearchByName(String searchKey) {
        //Arrange
        var entriesPerPage = 10L;
        var expectedSearchResult = TestDataStep.givenEmployeeInfo()
                .stream()
                .filter(entry -> Pattern.compile(Pattern.quote(searchKey), Pattern.CASE_INSENSITIVE)
                        .matcher(entry.getName())
                        .find())
                .limit(entriesPerPage)
                .toList();
        //Act
        formInputsPage
                .selectEntriesCount(entriesPerPage)
                .searchByName(searchKey)
                .getEmployeeTable()
        //Assert
                .shouldHaveHeaders()
                .shouldHaveRows(expectedSearchResult);
    }

    //todo create a test
    //update Zorita Serrano
    //find Zorita by search
    //assert new data is displayed
}
