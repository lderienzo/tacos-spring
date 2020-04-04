package com.pwvconsultants.tacosspring.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public final class FileReader {

    public static StringBuilder readFileContents(String filePath) {
        String absoluteFilePath = new File(filePath).getAbsolutePath();;
        return readFileContentsUsingAbsolutePath(absoluteFilePath);
    }

    private static StringBuilder readFileContentsUsingAbsolutePath(String absoluteFilePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(absoluteFilePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return contentBuilder;
    }
}
