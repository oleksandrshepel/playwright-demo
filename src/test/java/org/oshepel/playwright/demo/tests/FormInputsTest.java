package org.oshepel.playwright.demo.tests;

import io.qameta.allure.*;
import org.oshepel.playwright.demo.mapper.EmployeeInfoMapper;
import org.oshepel.playwright.demo.pages.FormInputsPage;
import org.oshepel.playwright.demo.pages.components.EmployeeTable;
import org.oshepel.playwright.demo.testdata.TestDataStep;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

import static org.oshepel.playwright.demo.pages.components.EmployeeTable.Column.*;

@Feature("Search")
public class FormInputsTest extends BaseUiTest {

    private FormInputsPage formInputsPage;
    private EmployeeTable employeeTable;

    @BeforeMethod
    @Override
    public void initAndOpenPage() {
        formInputsPage = new FormInputsPage();
        employeeTable = new EmployeeTable();
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


    @DataProvider(name = "fillCellData")
    public Object[][] fillCellData() {
        return new Object[][]{
                {AGE, "0"},
                {POSITION, "TEST"},
                {OFFICE, "London"},
        };
    }

    @Issue("Jira ticket")
    @Test(description = "Fill and validate cell with different element types", dataProvider = "fillCellData")
    @Description("""
            - Open the form inputs page,
            - Locate the target cell,
            - Fill the cell based on element type (TEXTAREA, INPUT, SELECT),
            - Validate the cell has been filled correctly.
            """)
    @Severity(SeverityLevel.CRITICAL)
    void testFillCell(EmployeeTable.Column column, String value) {
        //Arrange
        var entriesPerPage = 100L;

        //Act
        formInputsPage.selectEntriesCount(entriesPerPage);

        var lastRowLocator = employeeTable.getRowsLocator().last();
        var employeeName = NAME.getValueExtractor().apply(lastRowLocator);

        employeeTable.updateCell(column, employeeName, value);
        formInputsPage.searchByName(employeeName);
        EmployeeTable.Row row = EmployeeInfoMapper.map(lastRowLocator);
        //Assert
        if (row.getName().equals(employeeName)) {
            switch (column) {
                case AGE -> Assert.assertEquals(row.getAge(), Integer.parseInt(value), "Age has not been updated");
                case POSITION -> Assert.assertEquals(row.getPosition(), value, "Position has not been updated");
                case OFFICE ->
                        Assert.assertEquals(String.valueOf(row.getOffice()), value.toUpperCase(), "Office has not been updated");
                default -> throw new IllegalArgumentException(String.format("The column [%s] was not found", column));
            }
        } else {
            throw new IllegalArgumentException(String.format("The record was not found by the employee name [%s]", employeeName));
        }

    }
}
