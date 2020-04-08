package com.pwvconsultants.tacosspring.wordprocessor;

import static com.google.common.collect.Lists.newLinkedList;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.pwvconsultants.tacosspring.data.FileHandler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Component
public class RequiredLetterProcessor {
    private static final Set<Character> REQUIRED_LETTER_SET = ImmutableSet.of('A','E','I','L','N','O','R','S','T','U');
    private static final Pattern wordExtractingPattern = Pattern.compile("\\w+[']?\\w?(-\\w+'\\w)?");
    private static final FileHandler FILE_HANDLER = new FileHandler();

    public ProcessingResult processText(String textBlock) {
        List<String> words = getListOfValidWords(textBlock);
        ProcessingResult processingResult = new ProcessingResult(getRemainingWordArray(words), getMostCommonWord(words));
        writeProcessingResultToFile(processingResult);
        return processingResult;
    }

    private List<String> getListOfValidWords(String textBlock) {
        List<String> words = extractWordListFromTextBlock(textBlock);
        words = searchWordListForValidWords(words);
        return normalizeValidWordsToLowerCase(words);
    }

    private List<String> extractWordListFromTextBlock(String textBlock) {
        List<String> words = new ArrayList<>();
        Matcher matcher = wordExtractingPattern.matcher(textBlock);
        while (matcher.find())
            words.add(matcher.group());
        return words;
    }

    private List<String> searchWordListForValidWords(List<String> words) {
        return words.stream()
                .filter(this::wordIsValid)
                .collect(Collectors.toList());
    }

    private boolean wordIsValid(String word) {
        String normalizedWord = word.toUpperCase();
        List<Character> charCollection = convertStringToCharacterList(normalizedWord);
        return characterListIsValid(charCollection);
    }

    private List<Character> convertStringToCharacterList(String word) {
        Character[] charObjectArray = word.chars().mapToObj(c -> (char)c).toArray(Character[]::new);
        return newLinkedList(Arrays.asList(charObjectArray));
    }

    private boolean characterListIsValid(List<Character> characterList) {
        characterList.removeIf(REQUIRED_LETTER_SET::contains);
        String word = convertCharListBackToWordString(characterList);
        return wordDoesNotConsistOfOnlyRequiredLetters(word) &&
                (onlyOneCharacterRemaining(word) || allRemainingCharactersAreTheSame(word));
    }

    private String convertCharListBackToWordString(List<Character> characterList) {
        return characterList.stream().map(String::valueOf).collect(Collectors.joining());
    }

    private boolean wordDoesNotConsistOfOnlyRequiredLetters(String word) {
        return !word.equals("");
    }

    private boolean onlyOneCharacterRemaining(String word) {
        return word.length() == 1;
    }

    private boolean allRemainingCharactersAreTheSame(String word) {
        return word.chars().allMatch(c -> c == word.charAt(0));
    }

    private List<String> normalizeValidWordsToLowerCase(List<String> validWords) {
        return validWords.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    private String[] getRemainingWordArray(List<String> words) {
        Set<String> validWordSet = deDupeListByConvertingToSet(words);
        return convertSetToSortedArray(validWordSet);
    }

    private Set<String> deDupeListByConvertingToSet(List<String> validWordList) {
        return Sets.newHashSet(validWordList);
    }

    private String[] convertSetToSortedArray(Set<String> validWordSet) {
        String[] validWordArray = validWordSet.toArray(new String[0]);
        Arrays.sort(validWordArray, Comparator.comparing(String::length));
        return validWordArray;
    }

    private MostCommonWord getMostCommonWord(List<String> words) {
        Map<String, Long> wordOccurrencesMap = countWordOccurrences(words);
        wordOccurrencesMap = sortMapByWordOccurrenceEntryValue(wordOccurrencesMap);
        if (wordOccurrencesMapHasEntries(wordOccurrencesMap)) {
            Map.Entry<String, Long> entry = getVeryFirstWordEntryWhichHasMostOccurrences(wordOccurrencesMap);
            return new MostCommonWord(entry.getKey(), entry.getValue());
        }
        else return new MostCommonWord("", 0);
    }

    private Map<String, Long> countWordOccurrences(List<String> words) {
        return words.stream().collect(groupingBy(Function.identity(), counting()));
    }

    private Map<String, Long> sortMapByWordOccurrenceEntryValue(Map<String, Long> wordOccurrencesMap) {
        return wordOccurrencesMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> {
                            throw new IllegalStateException();
                        },
                        LinkedHashMap::new
                ));
    }

    private boolean wordOccurrencesMapHasEntries(Map<String, Long> wordFrequencyMap) {
        return wordFrequencyMap.size() > 0;
    }

    private Map.Entry<String, Long> getVeryFirstWordEntryWhichHasMostOccurrences(Map<String, Long> wordFrequencyMap) {
        return wordFrequencyMap.entrySet().iterator().next();
    }

    private void writeProcessingResultToFile(ProcessingResult processingResult) {
        String resultObjectJsonString = writeJsonResultObjectToString(processingResult);
        FILE_HANDLER.writeToFileInJarParentDirectory("textSampleResults.json", resultObjectJsonString);
    }

    private String writeJsonResultObjectToString(ProcessingResult processingResult) {
        try {
            return new ObjectMapper().writeValueAsString(processingResult);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Data
    @AllArgsConstructor
    public class ProcessingResult {
        private String[] remainingWords;
        private MostCommonWord mostCommonWord;

        public ProcessingResult() {}
    }

    @Data
    @AllArgsConstructor
    public static class MostCommonWord {
        private String word;
        private long numberOfUses;

        public MostCommonWord() {}
    }
}
