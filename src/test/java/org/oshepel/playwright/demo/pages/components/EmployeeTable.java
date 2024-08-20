package org.oshepel.playwright.demo.pages.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.oshepel.playwright.demo.model.Office;
import org.testng.Assert;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class EmployeeTable extends WebPageTable {

    private final Locator rootLocator = playwrightPage.locator("#example");

    @Override
    public String[] getHeaderValues() {
        return Stream.of(Column.values()).map(Column::getHeader).toArray(String[]::new);
    }

    @Override
    protected Locator getRootLocator() {
        return rootLocator;
    }

    @Override
    @Step("The employee table should have headers: Name, Age, Position, Office")
    public EmployeeTable shouldHaveHeaders() {
        PlaywrightAssertions.assertThat(getHeadersLocator()).hasText(getHeaderValues());
        return this;
    }

    @Override
    @Step("The employee table should have values: {expectedTableRows}")
    public EmployeeTable shouldHaveRows(List<? extends WebPageTableRow> expectedTableRows) {
        var actualRows = getRowsLocator().all().stream()
                .map(row -> Row.builder()
                        .name(Column.NAME.getValueExtractor().apply(row))
                        .age(Integer.parseInt(Column.AGE.getValueExtractor().apply(row)))
                        .position(Column.POSITION.getValueExtractor().apply(row))
                        .office(Office.of(Column.OFFICE.getValueExtractor().apply(row)))
                        .build())
                .toList();
        Assert.assertEquals(actualRows, expectedTableRows);
        return this;
    }

    @AllArgsConstructor
    @Getter
    public enum Column implements WebPageTableColumn {
        NAME(0, "Name", rootLocator -> rootLocator.locator("td").nth(0).innerText()),
        AGE(1, "Age", rootLocator -> rootLocator.locator("td").nth(1).locator("input").inputValue()),
        POSITION(2, "Position", rootLocator -> rootLocator.locator("td").nth(2).locator("input").inputValue()),
        OFFICE(3, "Office", rootLocator -> rootLocator.locator("td").nth(3).locator("select").inputValue());

        private final Integer columnIndex;
        private final String header;
        private final Function<Locator, String> valueExtractor;

        public static Column of(String value) {
            for (Column column : values()) {
                if (column.header.equals(value)) {
                    return column;
                }
            }
            throw new IllegalArgumentException(String.format("Column with header %s not found", value));
        }
    }

    @Builder
    @Getter
    @EqualsAndHashCode
    public static class Row implements WebPageTableRow {

        private String name;
        private Integer age;
        private String position;
        private Office office;

    }

}
