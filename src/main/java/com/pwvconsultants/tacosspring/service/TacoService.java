package com.pwvconsultants.tacosspring.service;


import org.springframework.stereotype.Component;

import com.pwvconsultants.tacosspring.data.TacoFileDataSourceService;
import com.pwvconsultants.tacosspring.model.Taco;

@Component
public class TacoService {

    public static final String tacosJsonPath = "src/main/resources/db.json";
    private TacoFileDataSourceService tacoFileDataSourceService = new TacoFileDataSourceService(tacosJsonPath);

    public String getTacos() {
        return tacoFileDataSourceService.getTacosJsonString();
    }

    public String getTaco(String name) {
        return tacoFileDataSourceService.getTaco(name);
    }

    public String updateTaco(Taco taco) {
        return tacoFileDataSourceService.updateTaco(taco);
    }

    public String removeTaco(String name) {
        return tacoFileDataSourceService.removeTaco(name);
    }
}
