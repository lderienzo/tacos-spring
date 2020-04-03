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


public final class TacoFileDataSourceService {

    public static final String RETURN_DONE = "done";
    private String filePath;
    private String tacosJsonString;
    private Taco[] tacos;
    private ObjectMapper mapper;

    public TacoFileDataSourceService(String filePath) {
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
        String absoluteFilePath = getAbsoluteFilePath();
        return getTacosJsonStringFromAbsolutePath(absoluteFilePath);
    }

    private String getAbsoluteFilePath() {
        return new File(filePath).getAbsolutePath();
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
        int endArrayIndex = contentBuilder.indexOf("]");
        return contentBuilder.subSequence(beginArrayIndex, endArrayIndex + 1).toString();
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
            return "not found";
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
        findTacoAndUpdateIt(tacoData, tacos);
        writeTacosToFile(tacos);
        return RETURN_DONE;
    }

    private void findTacoAndUpdateIt(Taco tacoData, Taco[] tacos) {
        for (int i = 0; i < tacos.length; i++) {
            if (tacoIsFound(tacoData.getName(), tacos[i].getName())) {
                tacos[i] = tacoData;
            }
        }
    }

    private boolean tacoIsFound(String nameOfCurrentTaco, String nameOfTacoToFind) {
        return nameOfCurrentTaco.equals(nameOfTacoToFind);
    }

    private void writeTacosToFile(Taco[] tacosArray) {
        Tacos tacos = new Tacos(tacosArray);
        try {
            mapper.writeValue(new File(filePath), tacos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String removeTaco(String name) {
        tacos = readTacoDataFromFile(mapper);
        int indexOfTacoToRemove = findIndexOfTacoToRemove(name, tacos);
        tacos = removeTacoFromArray(indexOfTacoToRemove, tacos);
        writeTacosToFile(tacos);
        return RETURN_DONE;
    }

    private int findIndexOfTacoToRemove(String name, Taco[] tacos) {
        return IntStream.range(0, tacos.length)
                .filter(i -> tacoIsFound(name, tacos[i].getName()))
                .findFirst().orElse(-1);
    }

    public Taco[] removeTacoFromArray(int index, Taco[] tacos) {
        if (returnArrayIfTacosEmptyOrIndexOutOfBounds(index, tacos))
            return tacos;
        List<Taco> tacoList = convertArrayToList(tacos);
        tacoList.remove(index);
        return convertListBackToArray(tacoList);
    }

    private boolean returnArrayIfTacosEmptyOrIndexOutOfBounds(int index, Taco[] tacos) {
        return tacos == null || tacos.length == 0 || index < 0 || index >= tacos.length;
    }

    private List<Taco> convertArrayToList(Taco[] tacos) {
        return Stream.of(tacos).collect(Collectors.toList());
    }

    private Taco[] convertListBackToArray(List<Taco> tacoList) {
        return tacoList.stream().toArray(n -> new Taco[n]);
    }
}

