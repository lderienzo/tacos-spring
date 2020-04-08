package com.pwvconsultants.wordprocessor;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.jupiter.api.Test;

import com.pwvconsultants.CommonTestMembers;
import com.pwvconsultants.tacosspring.wordprocessor.RequiredLetterProcessor;

public class RequiredLetterProcessorTest extends CommonTestMembers {

    private static final String TXT_SAMPLE = FILE_HANDLER.readFileFromDirectoryContainingThisJar("textSample.txt");
    private static final RequiredLetterProcessor WORD_PROCESSOR = new RequiredLetterProcessor();
    private static final String EXPECTED_MOST_COMMON_WORD = "their";
    private static final int EXPECTED_NUMBER_OF_USES = 6;
    private static final String[] EXPECTED_RETURN_WORD_ARRAY = {"do", "if", "we", "of", "but", "you", "new", "lay", "two",
            "and", "now", "end", "the", "foes", "fair", "take", "love", "here", "life", "miss", "pair", "rage", "scene",
            "these", "shall", "their", "fatal", "alike", "stage", "attend", "lovers", "strife", "strive", "verona",
            "piteous", "ancient", "unclean", "patient", "fearful", "continuance"};

    @Test
    public void whenValidTextBlockPassedThenDataProcessedCorrectlyAndExpectedValuesReturned() {
        // given/when
        RequiredLetterProcessor.ProcessingResult processingResult =
                WORD_PROCESSOR.processText(TXT_SAMPLE);
        // then
        assertThat(processingResult.getRemainingWords()).containsExactly(EXPECTED_RETURN_WORD_ARRAY);
        assertThat(processingResult.getMostCommonWord().getWord()).isEqualTo(EXPECTED_MOST_COMMON_WORD);
        assertThat(processingResult.getMostCommonWord().getNumberOfUses()).isEqualTo(EXPECTED_NUMBER_OF_USES);
    }

    @Test
    public void whenInValidTextBlockPassedThenEmptyResultDataReturned() {
        // given/when
        RequiredLetterProcessor.ProcessingResult processingResult =
                WORD_PROCESSOR.processText("");
        // then
        assertThat(processingResult.getRemainingWords()).containsExactly();
        assertThat(processingResult.getMostCommonWord().getWord()).isEqualTo("");
        assertThat(processingResult.getMostCommonWord().getNumberOfUses()).isEqualTo(0);
    }
}
