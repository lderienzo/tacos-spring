package com.pwvconsultants.data;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.pwvconsultants.tacosspring.data.TacoFileDataSourceService;

public class FileReaderTest {

    private TacoFileDataSourceService fileDataService = new TacoFileDataSourceService("src/test/resources/db.json");

    @Test
    public void whenGetTacosJsonCalledWithFilePathContainingTacosJsonThenTacosJsonStringReturned() {
        // given / when
        String actualFileContents = fileDataService.getTacosJson();
        // then
        String expectedFileContents = "[{\"name\":\"chorizo taco\",\"tortilla\":\"corn\",\"toppings\":\"chorizo\",\"vegetarian\":false,\"soft\":true},{\"name\":\"chicken taco\",\"tortilla\":\"flour\",\"toppings\":\"chicken\",\"vegetarian\":false,\"soft\":true},{\"name\":\"al pastor taco\",\"tortilla\":\"corn\",\"toppings\":\"pork\",\"vegetarian\":false,\"soft\":true},{\"name\":\"veggie taco\",\"tortilla\":\"spinach\",\"toppings\":\"veggies\",\"vegetarian\":true,\"soft\":true}]";
        assertThat(actualFileContents).isEqualTo(expectedFileContents);
    }

    @Test
    public void whenGetTacoCalledWithNameOfExistingTacoThenTacosJsonStringReturned() {
        // given / when
        String actualTacoJson = fileDataService.getTaco("chicken taco");
        // then
        String expectedTacoJson = "{\"name\":\"chicken taco\",\"tortilla\":\"flour\",\"toppings\":\"chicken\",\"vegetarian\":false,\"soft\":true}";
        assertThat(actualTacoJson).isEqualTo(expectedTacoJson);
    }

    @Test
    public void whenGetTacoCalledWithNameOfNonExistingTacoThenNotFoundStringReturned() {
        // given / when
        String actualTacoJson = fileDataService.getTaco("Bogus taco");
        // then
        String expectedTacoJson = "not found";
        assertThat(actualTacoJson).isEqualTo(expectedTacoJson);
    }
}
