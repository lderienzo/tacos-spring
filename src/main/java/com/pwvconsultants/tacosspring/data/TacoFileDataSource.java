package com.pwvconsultants.tacosspring.data;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pwvconsultants.tacosspring.model.Taco;
import com.pwvconsultants.tacosspring.model.Tacos;


public final class TacoFileDataSource {

    public static final String DONE = "done";
    public static final String NOT_FOUND = "not found";
    public static final String TACOS_JSON_FILE = "db.json";
    private static final FileHandler FILE_HANDLER = new FileHandler();
    private final ObjectMapper mapper;
    private String tacosJsonString;
    private Taco[] tacos;


    public TacoFileDataSource() {
        mapper = new ObjectMapper();
        this.tacos = readTacoDataFromFile(mapper);
    }

    private Taco[] readTacoDataFromFile(ObjectMapper mapper) {
        tacosJsonString = readTacosJsonStringFromFile();
        try {
            return mapper.readValue(tacosJsonString, Taco[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String readTacosJsonStringFromFile() {
        String content = FILE_HANDLER.readFileFromDirectoryContainingThisJar(TACOS_JSON_FILE);
        return parseOutTacosJsonArrayPortion(content);
    }

    private String parseOutTacosJsonArrayPortion(String content) {
        int beginArrayIndex = content.indexOf("[");
        int endArrayIndex = content.indexOf("]") + 1;
        return content.subSequence(beginArrayIndex, endArrayIndex).toString();
    }

    public String getTacosJsonString() {
        tacosJsonString = readTacosJsonStringFromFile();
        return tacosJsonString;
    }

    public String getTaco(String name) {
        tacos = readTacoDataFromFile(mapper);
        List<Taco> foundTaco = Arrays.stream(tacos).filter(taco -> taco.getName().equals(name)).collect(Collectors.toList());
        if (foundTaco.size() == 1)
            return writeTacoObjectToJsonString(foundTaco.get(0));
        else
            return NOT_FOUND;
    }

    private String writeTacoObjectToJsonString(Taco foundTaco) {
        try {
            return mapper.writeValueAsString(foundTaco);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String updateTaco(Taco tacoData) {
        tacos = readTacoDataFromFile(mapper);
        int indexOfTacoToUpdate = findIndexOfTaco(tacoData.getName(), tacos);
        tacos = updateTacoArray(tacoData, indexOfTacoToUpdate, tacos);
        writeTacoDataToFile(tacos);
        return DONE;
    }

    private int findIndexOfTaco(String name, Taco[] tacos) {
        return IntStream.range(0, tacos.length)
                .filter(i -> tacoIsFound(name, tacos[i].getName()))
                .findFirst().orElse(-1);
    }

    private boolean tacoIsFound(String nameOfCurrentTaco, String nameOfTacoToFind) {
        return nameOfCurrentTaco.equals(nameOfTacoToFind);
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

    private void writeTacoDataToFile(Taco[] tacosArray) {
        String tacosJson = convertTacosObjectToJsonString(new Tacos(tacosArray));
        FILE_HANDLER.writeToFileInJarParentDirectory(TACOS_JSON_FILE, tacosJson);
    }

    private String convertTacosObjectToJsonString(Tacos tacos) {
        try {
            return mapper.writeValueAsString(tacos);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String removeTaco(String name) {
        tacos = readTacoDataFromFile(mapper);
        int indexOfTacoToRemove = findIndexOfTaco(name, tacos);
        tacos = removeTacoFromArray(indexOfTacoToRemove, tacos);
        writeTacoDataToFile(tacos);
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

