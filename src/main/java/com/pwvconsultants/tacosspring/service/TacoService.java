package com.pwvconsultants.tacosspring.service;


import org.springframework.stereotype.Component;

import com.pwvconsultants.tacosspring.data.TacoFileDataSource;
import com.pwvconsultants.tacosspring.model.Taco;

@Component
public class TacoService {

    public static final String tacosJsonPath = "src/main/resources/db.json";
    private TacoFileDataSource tacoFileDataSource = new TacoFileDataSource(tacosJsonPath);

    public String getTacos() {
        return tacoFileDataSource.getTacosJsonString();
    }

    public String getTaco(String name) {
        return tacoFileDataSource.getTaco(name);
    }

    public String updateTaco(Taco taco) {
        return tacoFileDataSource.updateTaco(taco);
    }

    public String removeTaco(String name) {
        return tacoFileDataSource.removeTaco(name);
    }
}
