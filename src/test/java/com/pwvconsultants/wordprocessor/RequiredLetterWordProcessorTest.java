package com.pwvconsultants.wordprocessor;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.pwvconsultants.utils.FileReader;
import com.pwvconsultants.tacosspring.wordprocessor.RequiredLetterWordProcessor;

public class RequiredLetterWordProcessorTest {

    private static final String TXT_SAMPLE_FILE_PATH = "src/test/resources/textSample.txt";
    private static final String TXT_SAMPLE = FileReader.readFileContents(TXT_SAMPLE_FILE_PATH).toString();
    private static final String EXPECTED_MOST_COMMON_WORD = "their";
    private static final int EXPECTED_NUMBER_OF_USES = 6;
    private static final String[] EXPECTED_RETURN_WORD_ARRAY = {"do", "if", "we", "of", "but", "you", "new", "lay", "two",
            "and", "now", "end", "the", "foes", "fair", "take", "love", "here", "life", "miss", "pair", "rage", "scene",
            "these", "shall", "their", "fatal", "alike", "stage", "attend", "lovers", "strife", "strive", "verona",
            "piteous", "ancient", "unclean", "patient", "fearful", "continuance"};
    private RequiredLetterWordProcessor wordProcessor = new RequiredLetterWordProcessor();

    @Test
    public void whenValidTextBlockPassedThenDataProcessedCorrectlyAndExpectedValuesReturned() {
        // given/when
        RequiredLetterWordProcessor.ProcessingResult processingResult =
                wordProcessor.processText(TXT_SAMPLE);
        // then
        assertThat(processingResult.getRemainingWords()).containsExactly(EXPECTED_RETURN_WORD_ARRAY);
Arrays.stream(processingResult.getRemainingWords()).forEach(System.out::println);

        assertThat(processingResult.getMostCommonWord().getWord()).isEqualTo(EXPECTED_MOST_COMMON_WORD);
System.out.print("Most Common Word: ");
System.out.println(processingResult.getMostCommonWord().getWord());

        assertThat(processingResult.getMostCommonWord().getNumberOfUses()).isEqualTo(EXPECTED_NUMBER_OF_USES);
System.out.print("Number of Uses: ");
System.out.println(processingResult.getMostCommonWord().getNumberOfUses());
    }

    @Test
    public void whenInValidTextBlockPassedThenEmptyResultDataReturned() {
        // given/when
        RequiredLetterWordProcessor.ProcessingResult processingResult =
                wordProcessor.processText("");
        // then
        assertThat(processingResult.getRemainingWords()).containsExactly(new String[]{});
        assertThat(processingResult.getMostCommonWord().getWord()).isEqualTo("");
        assertThat(processingResult.getMostCommonWord().getNumberOfUses()).isEqualTo(0);
    }
}
