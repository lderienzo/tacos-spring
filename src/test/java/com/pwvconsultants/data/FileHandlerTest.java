package com.pwvconsultants.data;

import static com.pwvconsultants.tacosspring.service.TacoService.TACOS_JSON_FILE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.pwvconsultants.CommonTestMembers;


public final class FileHandlerTest extends CommonTestMembers {

    private static final String ALTERED_TACOS_JSON_FILE_CONTENTS = "{\"tacos\":[{\"name\":\"chicken taco\",\"tortilla\":\"flour\",\"toppings\":\"chicken\",\"vegetarian\":false,\"soft\":true},{\"name\":\"al pastor taco\",\"tortilla\":\"corn\",\"toppings\":\"pork\",\"vegetarian\":false,\"soft\":true},{\"name\":\"veggie taco\",\"tortilla\":\"spinach\",\"toppings\":\"veggies\",\"vegetarian\":true,\"soft\":true}]}";

    @Test
    public void whenValidTacosDbJsonFileNamePresentThenFileContentsCorrectlyRead() {
        // given/when
        String fileContents = FILE_HANDLER.readFileFromDirectoryContainingThisJar(TACOS_JSON_FILE);
        // then
        assertThat(fileContents).isEqualTo(ORIGINAL_TACOS_JSON_FILE_CONTENTS);
    }

    @Test
    public void whenValidTacosDbJsonFileNamePresentThenFileContentsCorrectlyWritten() {
        // given/when
        FILE_HANDLER.writeToFileInJarParentDirectory(TACOS_JSON_FILE, ALTERED_TACOS_JSON_FILE_CONTENTS);
        // then
        String expectedFileContents = FILE_HANDLER.readFileFromDirectoryContainingThisJar(TACOS_JSON_FILE);
        assertThat(expectedFileContents).isEqualTo(ALTERED_TACOS_JSON_FILE_CONTENTS);
        resetDbJsonWithOriginal();
    }

    public void resetDbJsonWithOriginal() {
        FILE_HANDLER.writeToFileInJarParentDirectory(TACOS_JSON_FILE, ORIGINAL_TACOS_JSON_FILE_CONTENTS);
    }
}
