package com.pwvconsultants.tacosspring.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pwvconsultants.tacosspring.model.Taco;
import com.pwvconsultants.tacosspring.service.TacoService;

//TODO: add endpoint for RequiredLetterWordProcessor
@RestController
@RequestMapping(value = "/api")
public class TacoApi {

    @Autowired
    TacoService tacoService;

    @GetMapping(value = "/tacos", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getTacos() {
        return tacoService.getTacos();
    }

    @GetMapping(value = "/taco/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getTaco(@PathVariable String name) {
        return tacoService.getTaco(name);
    }

    @PutMapping(value = "/taco/{name}")
    public String updateTaco(@RequestBody Taco changedTaco) {
        String result = tacoService.updateTaco(changedTaco);
        return result;
    }

    @DeleteMapping(value = "/taco/{name}")
    public String deleteTaco(@PathVariable String name) {
        String result = tacoService.removeTaco(name);
        return result;
    }
}
