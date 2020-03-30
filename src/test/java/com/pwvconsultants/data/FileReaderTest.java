package com.pwvconsultants.data;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.pwvconsultants.tacosspring.data.FileDataSourceService;

public class FileReaderTest {

    private FileDataSourceService fileDataService;

    @Test
    public void testDbFileReader() {
        // given
        fileDataService = new FileDataSourceService("src/test/resources/db.json");
        // when
        String fileContents = fileDataService.readData();
        // then
        assertThat(fileContents)
            .isEqualTo("[{\"name\":\"chorizo taco\",\"tortilla\":\"corn\",\"toppings\":\"chorizo\",\"vegetarian\":false,\"soft\":true},{\"name\":\"chicken taco\",\"tortilla\":\"flour\",\"toppings\":\"chicken\",\"vegetarian\":false,\"soft\":true},{\"name\":\"al pastor taco\",\"tortilla\":\"corn\",\"toppings\":\"pork\",\"vegetarian\":false,\"soft\":true},{\"name\":\"veggie taco\",\"tortilla\":\"spinach\",\"toppings\":\"veggies\",\"vegetarian\":true,\"soft\":true}]");
    }
}
