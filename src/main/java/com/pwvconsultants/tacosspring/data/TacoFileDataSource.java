package com.pwvconsultants.tacosspring.data;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    private String filePath;
    private String tacosJsonString;
    private Taco[] tacos;
    private ObjectMapper mapper;

    public TacoFileDataSource(String filePath) {
        this.filePath = filePath;
        mapper = new ObjectMapper();
        this.tacos = readTacoDataFromFile(mapper);
    }

    private Taco[] readTacoDataFromFile(ObjectMapper mapper) {
        tacosJsonString = readFileContents();
        try {
            return mapper.readValue(tacosJsonString, Taco[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String readFileContents() {
        String absoluteFilePath = new File(filePath).getAbsolutePath();;
        return getTacosJsonStringFromAbsolutePath(absoluteFilePath);
    }

    private String getTacosJsonStringFromAbsolutePath(String absoluteFilePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(absoluteFilePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return parseOutTacosJsonArray(contentBuilder);
    }

    private String parseOutTacosJsonArray(StringBuilder contentBuilder) {
        int beginArrayIndex = contentBuilder.indexOf("[");
        int endArrayIndex = contentBuilder.indexOf("]") + 1;
        return contentBuilder.subSequence(beginArrayIndex, endArrayIndex).toString();
    }

    public String getTacosJsonString() {
        tacosJsonString = readFileContents();
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
        Tacos tacos = new Tacos(tacosArray);
        try {
            mapper.writeValue(new File(filePath), tacos);
        } catch (IOException e) {
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
        return tacos == null || tacos.length == 0 || index < 0 || index >= tacos.length;
    }

    private List<Taco> convertArrayToList(Taco[] tacos) {
        return Stream.of(tacos).collect(Collectors.toList());
    }

    private Taco[] convertListBackToArray(List<Taco> tacoList) {
        return tacoList.stream().toArray(n -> new Taco[n]);
    }
}

