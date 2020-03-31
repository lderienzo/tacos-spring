package com.pwvconsultants.data;

import static com.pwvconsultants.tacosspring.service.TacoService.tacosJsonPath;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.junit.jupiter.api.Test;

import com.pwvconsultants.tacosspring.data.TacoFileDataSourceService;
import com.pwvconsultants.tacosspring.model.Taco;

public class FileReaderTest {

    private static final String tacosTestJsonPath = "src/test/resources/db.json";
    private TacoFileDataSourceService fileDataService = new TacoFileDataSourceService(tacosTestJsonPath);

    @Test
    public void whenGetTacosJsonCalledWithFilePathContainingTacosJsonThenTacosJsonStringReturned() {
        // given / when
        String actualFileContents = fileDataService.getTacosJson();
        // then
        String expectedFileContents = "[{\"name\":\"chorizo taco\",\"tortilla\":\"corn\",\"toppings\":\"chorizo\",\"vegetarian\":false,\"soft\":true},{\"name\":\"chicken taco\",\"tortilla\":\"flour\",\"toppings\":\"chicken\",\"vegetarian\":false,\"soft\":true},{\"name\":\"al pastor taco\",\"tortilla\":\"corn\",\"toppings\":\"pork\",\"vegetarian\":false,\"soft\":true},{\"name\":\"veggie taco\",\"tortilla\":\"spinach\",\"toppings\":\"veggies\",\"vegetarian\":true,\"soft\":true}]";
        assertThat(actualFileContents).isEqualTo(expectedFileContents);
    }

    @Test
    public void whenGetTacoCalledWithNameOfPreExistingTacoThenThatTacosJsonStringReturned() {
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

    @Test
    public void whenUpdateTacoCalledWithPreExistingTacoThenUpdatedTacoWrittenOutToJsonFile() {
        // given
        Taco tacoToUpdate = new Taco("chicken taco", "corn", "chicken", false, false);
        // when
        fileDataService.updateTaco(tacoToUpdate);
        // then
        String updatedFileContents = fileDataService.getTacosJson();
        String expectedFileContents = "[{\"name\":\"chorizo taco\",\"tortilla\":\"corn\",\"toppings\":\"chorizo\",\"vegetarian\":false,\"soft\":true},{\"name\":\"chicken taco\",\"tortilla\":\"corn\",\"toppings\":\"chicken\",\"vegetarian\":false,\"soft\":false},{\"name\":\"al pastor taco\",\"tortilla\":\"corn\",\"toppings\":\"pork\",\"vegetarian\":false,\"soft\":true},{\"name\":\"veggie taco\",\"tortilla\":\"spinach\",\"toppings\":\"veggies\",\"vegetarian\":true,\"soft\":true}]";
        assertThat(updatedFileContents).isEqualTo(expectedFileContents);
        resetTestJsonWithOriginal();
    }

    public void resetTestJsonWithOriginal() {
        Path testJsonPath = Paths.get(tacosTestJsonPath);
        Path originalJsonPath = Paths.get(tacosJsonPath);
        try {
            Files.copy(originalJsonPath, testJsonPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
