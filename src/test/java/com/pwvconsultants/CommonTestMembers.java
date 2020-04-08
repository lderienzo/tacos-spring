package com.pwvconsultants;

import com.pwvconsultants.tacosspring.data.FileHandler;

public class CommonTestMembers {
    public static final String ORIGINAL_TACOS_JSON_FILE_CONTENTS = "{\"tacos\":[{\"name\":\"chorizo taco\",\"tortilla\":\"corn\",\"toppings\":\"chorizo\",\"vegetarian\":false,\"soft\":true},{\"name\":\"chicken taco\",\"tortilla\":\"flour\",\"toppings\":\"chicken\",\"vegetarian\":false,\"soft\":true},{\"name\":\"al pastor taco\",\"tortilla\":\"corn\",\"toppings\":\"pork\",\"vegetarian\":false,\"soft\":true},{\"name\":\"veggie taco\",\"tortilla\":\"spinach\",\"toppings\":\"veggies\",\"vegetarian\":true,\"soft\":true}]}";
    public static final FileHandler FILE_HANDLER = new FileHandler();
}
