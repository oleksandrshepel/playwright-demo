package org.oshepel.playwright.demo.testdata;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.oshepel.playwright.demo.mapper.EmployeeInfoMapper;
import org.oshepel.playwright.demo.pages.components.EmployeeTable;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestDataStep {

    private static final String EXPECTED_RES_PATH = "src/test/resources/data/";
    private static final String EMPLOYEE_INFO_FILE = "employee_info.csv";

    public static List<EmployeeTable.Row> givenEmployeeInfo() {
        return EmployeeInfoMapper.mapFromCsv(EXPECTED_RES_PATH.concat(EMPLOYEE_INFO_FILE));
    }

}
