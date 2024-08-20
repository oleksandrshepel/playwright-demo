package org.oshepel.playwright.demo.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileUtils {

    public static List<String> readFile(String filepath) {
        try {
            return Files.readAllLines(Paths.get(filepath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Failed to read file {}", filepath, e);
            throw new IllegalStateException(e);
        }
    }
}