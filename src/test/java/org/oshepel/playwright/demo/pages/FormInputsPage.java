package org.oshepel.playwright.demo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;
import org.oshepel.playwright.demo.pages.components.EmployeeTable;

import java.util.List;

public class FormInputsPage extends AbstractBasePage {

    private static final String RELATIVE_PATH = "/examples/api/form.html";
    private static final String PAGE_HEADER = "Form inputs";
    private static final String SEARCH_LABEL = "Search:";

    private final Locator pageHeader = playwrightPage.locator(".page_title");
    private final Locator entriesCountSelect = playwrightPage.locator("select.dt-input");
    private final Locator searchInput = playwrightPage.locator("input.dt-input");
    private final Locator searchLabel = playwrightPage.locator("//input[@class='dt-input']/preceding-sibling::label");
    private final Locator table = playwrightPage.locator("#example");

    private final EmployeeTable employeeTable = new EmployeeTable();

    public EmployeeTable getEmployeeTable() {
        return employeeTable;
    }

    @Override
    @Step("Opening the form inputs page")
    public void open() {
        openPageWithRelativePath(RELATIVE_PATH);
        shouldBeLoaded();
    }

    @Override
    @Step("The page header should be displayed")
    public FormInputsPage shouldBeLoaded() {
        PlaywrightAssertions.assertThat(pageHeader).hasText(PAGE_HEADER);
        return this;
    }

    @Step("Selecting entries count")
    public FormInputsPage selectEntriesCount(Long count) {
        entriesCountSelect.selectOption(String.valueOf(count));
        return this;
    }

    @Step("Typing {value} into the search field")
    public FormInputsPage searchByName(String value) {
        searchInput.fill(value);
        PlaywrightAssertions.assertThat(searchInput).hasValue(value);
        PlaywrightAssertions.assertThat(searchLabel).hasText(SEARCH_LABEL);
        return this;
    }

    @Step("Example table should contain names with a search key {searchKey}")
    public FormInputsPage exampleTableShouldHaveNames(String searchKey) {
        List<String> namesFromTable = table.locator("td:nth-of-type(1)").allTextContents();
        namesFromTable.forEach(name -> Assertions.assertThat(name).containsIgnoringCase(searchKey));
        return this;
    }
}