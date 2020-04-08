package com.pwvconsultants.tacosspring.service;


import org.springframework.stereotype.Component;

import com.pwvconsultants.tacosspring.data.TacoFileDataSource;
import com.pwvconsultants.tacosspring.model.Taco;

@Component
public class TacoService {

    private static final TacoFileDataSource TACO_FILE_DATA_SOURCE = new TacoFileDataSource();

    public String getTacos() {
        return TACO_FILE_DATA_SOURCE.getTacosJsonString();
    }

    public String getTaco(String name) {
        return TACO_FILE_DATA_SOURCE.getTaco(name);
    }

    public String updateTaco(Taco taco) {
        return TACO_FILE_DATA_SOURCE.updateTaco(taco);
    }

    public String removeTaco(String name) {
        return TACO_FILE_DATA_SOURCE.removeTaco(name);
    }
}
