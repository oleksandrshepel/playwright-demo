package org.oshepel.playwright.demo.mapper;

import org.oshepel.playwright.demo.model.Office;
import org.oshepel.playwright.demo.pages.components.EmployeeTable;
import org.oshepel.playwright.demo.util.FileUtils;
import org.testng.Assert;

import java.util.List;

public class EmployeeInfoMapper {

    public static List<EmployeeTable.Row> mapFromCsv(String filePath) {
        return FileUtils.readFile(filePath)
                .stream()
                .map(row -> {
                            var cells = row.split("\\|");
                            Assert.assertEquals(cells.length, 4);
                            return EmployeeTable.Row.builder()
                                    .name(cells[EmployeeTable.Column.NAME.getColumnIndex()])
                                    .age(Integer.parseInt(cells[EmployeeTable.Column.AGE.getColumnIndex()]))
                                    .position(cells[EmployeeTable.Column.POSITION.getColumnIndex()])
                                    .office(Office.of(cells[EmployeeTable.Column.OFFICE.getColumnIndex()]))
                                    .build();
                        }
                )
                .toList();
    }
}
