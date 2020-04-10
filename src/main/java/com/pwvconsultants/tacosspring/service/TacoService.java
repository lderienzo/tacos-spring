package com.pwvconsultants.tacosspring.service;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pwvconsultants.tacosspring.data.FileHandler;
import com.pwvconsultants.tacosspring.model.Taco;
import com.pwvconsultants.tacosspring.model.Tacos;

@Component
public final class TacoService {

    public static final String DONE = "done";
    public static final String NOT_FOUND = "not found";
    public static final String TACOS_JSON_FILE = "db.json";
    private static final FileHandler FILE_HANDLER = new FileHandler();
    private final ObjectMapper mapper;
    private String tacos;
    private Taco[] tacoArray;


    public TacoService() {
        mapper = new ObjectMapper();
    }

    public String getTacos() {
        tacos = readTacosJsonFromFile();
        return tacos;
    }

    private String readTacosJsonFromFile() {
        String content = FILE_HANDLER.readFileFromDirectoryContainingThisJar(TACOS_JSON_FILE);
        return parseOutTacosArrayPortion(content);
    }

    private String parseOutTacosArrayPortion(String content) {
        int beginArrayIndex = content.indexOf("[");
        int endArrayIndex = content.indexOf("]") + 1;
        return content.subSequence(beginArrayIndex, endArrayIndex).toString();
    }

    public String getTaco(String name) {
        tacoArray = readTacoArrayDataFromFile(mapper);
        List<Taco> foundTaco = Arrays.stream(tacoArray).filter(taco -> taco.getName().equals(name)).collect(Collectors.toList());
        if (foundTaco.size() == 1)
            return writeTacoObjectToJsonString(foundTaco.get(0));
        else
            return NOT_FOUND;
    }

    private Taco[] readTacoArrayDataFromFile(ObjectMapper mapper) {
        tacos = readTacosJsonFromFile();
        try {
            return mapper.readValue(tacos, Taco[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String writeTacoObjectToJsonString(Taco foundTaco) {
        try {
            return mapper.writeValueAsString(foundTaco);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String updateTaco(Taco tacoData) {
        tacoArray = readTacoArrayDataFromFile(mapper);
        int indexOfTacoToUpdate = findIndexOfTaco(tacoArray, tacoData.getName());
        tacoArray = updateTacoArray(tacoData, indexOfTacoToUpdate, tacoArray);
        writeTacoArrayDataToFile(tacoArray);
        return DONE;
    }

    private int findIndexOfTaco(Taco[] tacos, String name) {
        return IntStream.range(0, tacos.length)
                .filter(i -> tacoIsFound(tacos[i].getName(), name))
                .findFirst().orElse(-1);
    }

    private boolean tacoIsFound(String currentTaco, String tacoToFind) {
        return currentTaco.equals(tacoToFind);
    }

    private Taco[] updateTacoArray(Taco tacoData, int index, Taco[] tacos) {
        if (indexIsInvalid(index))
            return tacos;
        tacos[index] = tacoData;
        return tacos;
    }

    private boolean indexIsInvalid(int index) {
        return index == -1;
    }

    private void writeTacoArrayDataToFile(Taco[] tacosArray) {
        String completeTacosJson = convertTacosObjectToJsonString(new Tacos(tacosArray));
        FILE_HANDLER.writeToFileInJarParentDirectory(TACOS_JSON_FILE, completeTacosJson);
    }

    private String convertTacosObjectToJsonString(Tacos tacos) {
        try {
            return mapper.writeValueAsString(tacos);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String removeTaco(String name) {
        tacoArray = readTacoArrayDataFromFile(mapper);
        int indexOfTacoToRemove = findIndexOfTaco(tacoArray, name);
        tacoArray = removeTacoFromArray(indexOfTacoToRemove, tacoArray);
        writeTacoArrayDataToFile(tacoArray);
        return DONE;
    }

    public Taco[] removeTacoFromArray(int index, Taco[] tacos) {
        if (tacosEmptyOrIndexOutOfBounds(index, tacos))
            return tacos;
        List<Taco> tacoList = convertArrayToList(tacos);
        tacoList.remove(index);
        return convertListBackToArray(tacoList);
    }

    private boolean tacosEmptyOrIndexOutOfBounds(int index, Taco[] tacos) {
        return tacos == null || index < 0 || index >= tacos.length;
    }

    private List<Taco> convertArrayToList(Taco[] tacos) {
        return Stream.of(tacos).collect(Collectors.toList());
    }

    private Taco[] convertListBackToArray(List<Taco> tacoList) {
        return tacoList.toArray(new Taco[0]);
    }
}

