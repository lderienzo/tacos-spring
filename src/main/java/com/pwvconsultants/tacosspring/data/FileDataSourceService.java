package com.pwvconsultants.tacosspring.data;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


public class FileDataSourceService implements DataSourceService {

    private String filePath;

    public FileDataSourceService(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String readData() {
        return readFileContents();
    }

    private String readFileContents() {
        String absoluteFilePath = getAbsoluteFilePath();
        return getContentStringFromAbsolutePath(absoluteFilePath);
    }

    private String getAbsoluteFilePath() {
        return new File(filePath).getAbsolutePath();
    }

    private String getContentStringFromAbsolutePath(String absoluteFilePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(absoluteFilePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }
}

