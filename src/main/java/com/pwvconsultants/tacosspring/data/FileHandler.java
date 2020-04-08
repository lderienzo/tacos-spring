package com.pwvconsultants.tacosspring.data;

import static com.pwvconsultants.tacosspring.data.JarParentDirectoryInitializer.getThisJarsParentDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public final class FileHandler {

    private static final String JAR_PARENT_DIRECTORY = getThisJarsParentDirectory();

    public String readFileFromDirectoryContainingThisJar(String inFileName) {
        String inputFilePath = constructFilePathFromJarParentDirectoryAndFileName(inFileName);
        try (InputStream inputStream = new FileInputStream(new File(inputFilePath));
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String constructFilePathFromJarParentDirectoryAndFileName(String fileName) {
        if (valueForParentDirectoryIsNotEmpty())
            return JAR_PARENT_DIRECTORY + File.separator + fileName;
        else
            return fileName;
    }

    private boolean valueForParentDirectoryIsNotEmpty() {
        return !"".equals(JAR_PARENT_DIRECTORY);
    }

    public void writeToFileInJarParentDirectory(String outFileName, String stringToWrite) {
        String outFilePath = constructFilePathFromJarParentDirectoryAndFileName(outFileName);
        try {
            FileWriter writer = new FileWriter(new File(outFilePath));
            writer.write(stringToWrite);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
