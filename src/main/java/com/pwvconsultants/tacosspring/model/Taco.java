package com.pwvconsultants.tacosspring.model;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Taco {
    @Size(min = 1, max = 255, message = "Taco name must be between 1 and 255 characters")
    private String name; // max length 255
    private String tortilla;
    private String toppings;
    private boolean vegetarian;
    private boolean soft;

    public Taco () {}
}
