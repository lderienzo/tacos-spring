package com.pwvconsultants.tacosspring.service;


import org.springframework.stereotype.Component;

import com.pwvconsultants.tacosspring.data.TacoFileDataSourceService;
import com.pwvconsultants.tacosspring.model.Taco;

@Component
public class TacoService {

    private TacoFileDataSourceService tacoFileDataSourceService = new TacoFileDataSourceService("src/main/resources/db.json");

    public String updateTaco(Taco taco) {
        return "done";
    }

    public String removeTaco(String name) {
        return "done";
    }

    public String getTaco(String name) {
        return tacoFileDataSourceService.getTaco(name);
    }

    public String getTacos() {
        return tacoFileDataSourceService.getTacosJson();
    }
}
