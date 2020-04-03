package com.pwvconsultants.tacosspring.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Tacos {
    private Taco[] tacos;

    public Tacos() {}
}
