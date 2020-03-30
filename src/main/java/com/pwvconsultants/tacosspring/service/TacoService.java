package com.pwvconsultants.tacosspring.service;


import org.springframework.stereotype.Component;

import com.pwvconsultants.tacosspring.data.FileDataSourceService;
import com.pwvconsultants.tacosspring.model.Taco;

@Component
public class TacoService {

    private FileDataSourceService fileDataSourceService = new FileDataSourceService("src/main/resources/db.json");

    public String updateTaco(Taco taco) {
        return "done";
    }

    public String removeTaco(String name) {
        return "done";
    }

    public Taco getTaco(String name) {
        return null;
    }

    public String getTacos() {
        return fileDataSourceService.readData();
    }
}
