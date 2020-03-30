package com.pwvconsultants.tacosspring.data;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pwvconsultants.tacosspring.model.Taco;


public final class TacoFileDataSourceService {

    private String filePath;
    private String tacosJson;
    private Taco[] tacoArray;
    private ObjectMapper mapper;

    public TacoFileDataSourceService(String filePath) {
        this.filePath = filePath;
        this.tacosJson = readFileContents();
        mapper = new ObjectMapper();
        this.tacoArray = readJsonIntoTacoArray(mapper);
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

    public Taco[] readJsonIntoTacoArray(ObjectMapper mapper) {
        try {
            return mapper.readValue(tacosJson, Taco[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTacosJson() {
        return tacosJson;
    }

    public String getTaco(String name) {
        List<Taco> foundTaco = Arrays.stream(tacoArray)
                .filter(taco -> taco.getName().equals(name)).collect(Collectors.toList());
        if (foundTaco.size() == 1)
            return writeTacoObjectToJsonString(foundTaco.get(0));
        else
            return "not found";
    }

    private String writeTacoObjectToJsonString(Taco foundTaco) {
        try {
            return mapper.writeValueAsString(foundTaco);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

