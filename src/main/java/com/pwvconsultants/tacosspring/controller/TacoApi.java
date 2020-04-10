package com.pwvconsultants.tacosspring.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pwvconsultants.tacosspring.service.TacoService;
import com.pwvconsultants.tacosspring.model.Taco;
import com.pwvconsultants.tacosspring.wordprocessor.RequiredLetterProcessor;

@RestController
@RequestMapping(value = "/api")
public class TacoApi {

    @Autowired
    TacoService tacoService;

    @Autowired
    RequiredLetterProcessor wordProcessor;

    @GetMapping(value = "/tacos", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getTacos() {
        return tacoService.getTacos();
    }

    @GetMapping(value = "/taco/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getTaco(@PathVariable String name) {
        return tacoService.getTaco(name);
    }

    @PutMapping(value = "/taco/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateTaco(@RequestBody Taco changedTaco) {
        return tacoService.updateTaco(changedTaco);
    }

    @DeleteMapping(value = "/taco/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteTaco(@PathVariable String name) {
        return tacoService.removeTaco(name);
    }

    @PostMapping(value = "/tacos/processtext", produces = MediaType.APPLICATION_JSON_VALUE)
    public RequiredLetterProcessor.ProcessingResult processTextBlock(@RequestBody String textBlock) {
        return wordProcessor.processText(textBlock);
    }
}
