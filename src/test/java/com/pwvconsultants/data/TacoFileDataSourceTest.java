package com.pwvconsultants.data;

import static com.pwvconsultants.tacosspring.data.TacoFileDataSource.DONE;
import static com.pwvconsultants.tacosspring.data.TacoFileDataSource.NOT_FOUND;
import static com.pwvconsultants.tacosspring.service.TacoService.tacosJsonPath;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.pwvconsultants.tacosspring.data.TacoFileDataSource;
import com.pwvconsultants.tacosspring.model.Taco;

public class TacoFileDataSourceTest {

    private static final String TACOS_TEST_JSON_FILE_PATH = "src/test/resources/db.json";
    private static final String EXPECTED_UNCHANGED_TACO_DATA = "[{\"name\":\"chorizo taco\",\"tortilla\":\"corn\",\"toppings\":\"chorizo\",\"vegetarian\":false,\"soft\":true},{\"name\":\"chicken taco\",\"tortilla\":\"flour\",\"toppings\":\"chicken\",\"vegetarian\":false,\"soft\":true},{\"name\":\"al pastor taco\",\"tortilla\":\"corn\",\"toppings\":\"pork\",\"vegetarian\":false,\"soft\":true},{\"name\":\"veggie taco\",\"tortilla\":\"spinach\",\"toppings\":\"veggies\",\"vegetarian\":true,\"soft\":true}]";

    private TacoFileDataSource tacoFileDataSource = new TacoFileDataSource(TACOS_TEST_JSON_FILE_PATH);

    @AfterEach
    public void resetTestJsonWithOriginal() {
        Path testJsonPath = Paths.get(TACOS_TEST_JSON_FILE_PATH);
        Path originalJsonPath = Paths.get(tacosJsonPath);
        try {
            Files.copy(originalJsonPath, testJsonPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void whenGetTacosJsonCalledWithFilePathContainingTacosJsonThenTacosJsonStringReturned() {
        // given / when
        String actualFileContents = tacoFileDataSource.getTacosJsonString();
        // then
        assertThat(actualFileContents).isEqualTo(EXPECTED_UNCHANGED_TACO_DATA);
    }

    @Test
    public void whenGetTacoCalledWithNameOfPreExistingTacoThenThatTacosJsonStringReturned() {
        // given / when
        String actualTacoJson = tacoFileDataSource.getTaco("chicken taco");
        // then
        String expectedTacoJson = "{\"name\":\"chicken taco\",\"tortilla\":\"flour\",\"toppings\":\"chicken\",\"vegetarian\":false,\"soft\":true}";
        assertThat(actualTacoJson).isEqualTo(expectedTacoJson);
    }

    @Test
    public void whenGetTacoCalledWithNameOfNonExistingTacoThenNotFoundStringReturned() {
        // given / when
        String actualTacoJson = tacoFileDataSource.getTaco("Bogus taco");
        // then
        String expectedTacoJson = NOT_FOUND;
        assertThat(actualTacoJson).isEqualTo(expectedTacoJson);
    }

    @Test
    public void whenUpdateTacoCalledWithPreExistingTacoThenUpdatedTacoWrittenToJsonFile() {
        // given
        Taco changedFlourTortillaToHardCorn= new Taco("chicken taco", "corn", "chicken", false, false);
        // when
        tacoFileDataSource.updateTaco(changedFlourTortillaToHardCorn);
        // then
        String updatedFileContents = tacoFileDataSource.getTacosJsonString();
        String expectedFileContents = "[{\"name\":\"chorizo taco\",\"tortilla\":\"corn\",\"toppings\":\"chorizo\",\"vegetarian\":false,\"soft\":true},{\"name\":\"chicken taco\",\"tortilla\":\"corn\",\"toppings\":\"chicken\",\"vegetarian\":false,\"soft\":false},{\"name\":\"al pastor taco\",\"tortilla\":\"corn\",\"toppings\":\"pork\",\"vegetarian\":false,\"soft\":true},{\"name\":\"veggie taco\",\"tortilla\":\"spinach\",\"toppings\":\"veggies\",\"vegetarian\":true,\"soft\":true}]";
        assertThat(updatedFileContents).isEqualTo(expectedFileContents);
    }

    @Test
    public void whenRemoveTacoCalledWithPreExistingTacoThenTacoRemovedAndWrittenToJsonFile() {
        // given
        String tacoToRemove = "chicken taco";
        // when
        String returnString = tacoFileDataSource.removeTaco(tacoToRemove);
        // then
        String updatedFileContents = tacoFileDataSource.getTacosJsonString();
        String expectedFileContents = "[{\"name\":\"chorizo taco\",\"tortilla\":\"corn\",\"toppings\":\"chorizo\",\"vegetarian\":false,\"soft\":true},{\"name\":\"al pastor taco\",\"tortilla\":\"corn\",\"toppings\":\"pork\",\"vegetarian\":false,\"soft\":true},{\"name\":\"veggie taco\",\"tortilla\":\"spinach\",\"toppings\":\"veggies\",\"vegetarian\":true,\"soft\":true}]";
        assertThat(updatedFileContents).isEqualTo(expectedFileContents);
        assertThat(returnString).isEqualTo(DONE);
    }

    @Test
    public void whenRemoveTacoCalledWithNonExistentTacoThenNoTacoRemoved() {
        // given
        String tacoToRemove = "bogus taco";
        // when
        String returnString = tacoFileDataSource.removeTaco(tacoToRemove);
        // then
        String updatedFileContents = tacoFileDataSource.getTacosJsonString();
        assertThat(updatedFileContents).isEqualTo(EXPECTED_UNCHANGED_TACO_DATA);
        assertThat(returnString).isEqualTo(DONE);
    }
}
