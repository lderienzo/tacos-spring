package com.pwvconsultants.data;

import static com.pwvconsultants.tacosspring.data.TacoFileDataSource.DONE;
import static com.pwvconsultants.tacosspring.data.TacoFileDataSource.NOT_FOUND;
import static com.pwvconsultants.tacosspring.data.TacoFileDataSource.TACOS_JSON_FILE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.pwvconsultants.CommonTestMembers;
import com.pwvconsultants.tacosspring.data.TacoFileDataSource;
import com.pwvconsultants.tacosspring.model.Taco;

public class TacoFileDataSourceTest extends CommonTestMembers {

    private static final String ORIGINAL_TACOS_JSON_ARRAY = "[{\"name\":\"chorizo taco\",\"tortilla\":\"corn\",\"toppings\":\"chorizo\",\"vegetarian\":false,\"soft\":true},{\"name\":\"chicken taco\",\"tortilla\":\"flour\",\"toppings\":\"chicken\",\"vegetarian\":false,\"soft\":true},{\"name\":\"al pastor taco\",\"tortilla\":\"corn\",\"toppings\":\"pork\",\"vegetarian\":false,\"soft\":true},{\"name\":\"veggie taco\",\"tortilla\":\"spinach\",\"toppings\":\"veggies\",\"vegetarian\":true,\"soft\":true}]";
    private static final TacoFileDataSource TACO_FILE_DATA_SOURCE = new TacoFileDataSource();


    @AfterEach
    public void resetDbJsonWithOriginal() {
        FILE_HANDLER.writeToFileInJarParentDirectory(TACOS_JSON_FILE, ORIGINAL_TACOS_JSON_FILE_CONTENTS);
    }

    @Test
    public void whenGetTacosJsonCalledWithValidFilePathThenTacosJsonReturned() {
        // given / when
        String actualFileContents = TACO_FILE_DATA_SOURCE.getTacosJsonString();
        // then
        assertThat(actualFileContents).isEqualTo(ORIGINAL_TACOS_JSON_ARRAY);
    }

    @Test
    public void whenGetTacoCalledWithValidTacoNameThenCorrectJsonStringReturned() {
        // given / when
        String actualTacoJson = TACO_FILE_DATA_SOURCE.getTaco("chicken taco");
        // then
        String expectedTacoJson = "{\"name\":\"chicken taco\",\"tortilla\":\"flour\",\"toppings\":\"chicken\",\"vegetarian\":false,\"soft\":true}";
        assertThat(actualTacoJson).isEqualTo(expectedTacoJson);
    }

    @Test
    public void whenGetTacoCalledWithNameOfNonExistingTacoThenNotFoundStringReturned() {
        // given / when
        String actualTacoJson = TACO_FILE_DATA_SOURCE.getTaco("Bogus taco");
        // then
        assertThat(actualTacoJson).isEqualTo(NOT_FOUND);
    }

    @Test
    public void whenUpdateTacoCalledWithValidTacoThenWrittenToJsonFile() {
        // given
        Taco changedFlourTortillaToHardCorn= new Taco("chicken taco", "corn", "chicken", false, false);
        // when
        String returnString = TACO_FILE_DATA_SOURCE.updateTaco(changedFlourTortillaToHardCorn);
        // then
        String updatedFileContents = TACO_FILE_DATA_SOURCE.getTacosJsonString();
        String expectedFileContents = "[{\"name\":\"chorizo taco\",\"tortilla\":\"corn\",\"toppings\":\"chorizo\",\"vegetarian\":false,\"soft\":true},{\"name\":\"chicken taco\",\"tortilla\":\"corn\",\"toppings\":\"chicken\",\"vegetarian\":false,\"soft\":false},{\"name\":\"al pastor taco\",\"tortilla\":\"corn\",\"toppings\":\"pork\",\"vegetarian\":false,\"soft\":true},{\"name\":\"veggie taco\",\"tortilla\":\"spinach\",\"toppings\":\"veggies\",\"vegetarian\":true,\"soft\":true}]";
        assertThat(updatedFileContents).isEqualTo(expectedFileContents);
        assertThat(returnString).isEqualTo(DONE);
    }

    @Test
    public void whenRemoveTacoCalledWithValidTacoThenRemovedAndWrittenToJsonFile() {
        // given
        String tacoToRemove = "chicken taco";
        // when
        String returnString = TACO_FILE_DATA_SOURCE.removeTaco(tacoToRemove);
        // then
        String updatedFileContents = TACO_FILE_DATA_SOURCE.getTacosJsonString();
        String expectedFileContents = "[{\"name\":\"chorizo taco\",\"tortilla\":\"corn\",\"toppings\":\"chorizo\",\"vegetarian\":false,\"soft\":true},{\"name\":\"al pastor taco\",\"tortilla\":\"corn\",\"toppings\":\"pork\",\"vegetarian\":false,\"soft\":true},{\"name\":\"veggie taco\",\"tortilla\":\"spinach\",\"toppings\":\"veggies\",\"vegetarian\":true,\"soft\":true}]";
        assertThat(updatedFileContents).isEqualTo(expectedFileContents);
        assertThat(returnString).isEqualTo(DONE);
    }

    @Test
    public void whenRemoveTacoCalledWithInvalidTacoThenNoneRemoved() {
        // given
        String tacoToRemove = "bogus taco";
        // when
        String returnString = TACO_FILE_DATA_SOURCE.removeTaco(tacoToRemove);
        // then
        String updatedFileContents = TACO_FILE_DATA_SOURCE.getTacosJsonString();
        assertThat(updatedFileContents).isEqualTo(ORIGINAL_TACOS_JSON_ARRAY);
        assertThat(returnString).isEqualTo(DONE);
    }
}
