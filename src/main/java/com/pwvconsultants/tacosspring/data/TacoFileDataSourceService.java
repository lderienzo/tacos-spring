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
    private Taco[] tacos;
    private ObjectMapper mapper;

    public TacoFileDataSourceService(String filePath) {
        this.filePath = filePath;
        mapper = new ObjectMapper();
        this.tacos = readJsonIntoTacoArray(mapper);
    }

    private Taco[] readJsonIntoTacoArray(ObjectMapper mapper) {
        tacosJson = readFileContents();
        try {
            return mapper.readValue(tacosJson, Taco[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return contentBuilder.toString();
    }

    public String getTacosJson() {
        tacosJson = readFileContents();
        return tacosJson;
    }

    public String getTaco(String name) {
        List<Taco> foundTaco = Arrays.stream(tacos).filter(taco -> taco.getName().equals(name)).collect(Collectors.toList());
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

    public void updateTaco(Taco updatedTaco) {
        iterateOverTacosAndUpdate(updatedTaco);
        writeTacosToFile();
    }

    private void iterateOverTacosAndUpdate(Taco updatedTaco) {
        for (int i = 0; i < tacos.length; i++) {
            if (currentTacoIsToBeUpdated(updatedTaco, tacos[i])) {
                tacos[i] = updatedTaco;
            }
        }
    }

    private boolean currentTacoIsToBeUpdated(Taco currentTaco, Taco updatedTaco) {
        return currentTaco.getName().equals(updatedTaco.getName());
    }

    private void writeTacosToFile() {
        try {
            mapper.writeValue(new File(filePath), tacos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

