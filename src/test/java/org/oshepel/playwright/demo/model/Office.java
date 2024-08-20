package org.oshepel.playwright.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Office {
    EDINBURGH("Edinburgh"),
    LONDON("London"),
    TOKYO("Tokyo"),
    NEW_YORK("New York"),
    SAN_FRANCISCO("San Francisco");

    private String name;

    public static Office of(String text) {
        for (Office office : values()) {
            if (office.name.equals(text.trim())) {
                return office;
            }
        }
        throw new IllegalArgumentException(String.format("Office %s not found", text));
    }

}
