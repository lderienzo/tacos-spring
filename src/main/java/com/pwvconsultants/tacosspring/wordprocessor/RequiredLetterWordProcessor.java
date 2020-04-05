package com.pwvconsultants.tacosspring.wordprocessor;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
import com.pwvconsultants.tacosspring.TacosSpringApplication;

import lombok.AllArgsConstructor;
import lombok.Data;

@Component
public class RequiredLetterWordProcessor {
    private static final Set<Character> REQUIRED_LETTER_SET = ImmutableSet.of('A','E','I','L','N','O','R','S','T','U');
    private static final Pattern wordExtractingPattern = Pattern.compile("\\w+[']?\\w?(\\-\\w+'\\w)?");


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
        Collection<Character> charCollection = convertStringToCharacterCollection(normalizedWord);
        return characterCollectionIsValid(charCollection);
    }

    private Collection<Character> convertStringToCharacterCollection(String word) {
        Character[] charObjectArray = word.chars().mapToObj(c -> (char)c).toArray(Character[]::new);
        return new LinkedList(Arrays.asList(charObjectArray));
    }

    private boolean characterCollectionIsValid(Collection<Character> charCollection) {
        charCollection.removeIf(REQUIRED_LETTER_SET::contains);
        String word = convertCharCollectionBackToWordString(charCollection);
        return wordDoesNotConsistOfOnlyRequiredLetters(word) &&
                (onlyOneCharacterRemaining(word) || allRemainingCharactersAreTheSame(word));
    }

    private String convertCharCollectionBackToWordString(Collection<Character> charCollection) {
        return charCollection.stream().map(String::valueOf).collect(Collectors.joining());
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
        return new HashSet(validWordList);
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
        File jarFilePath = getPathOfCurrentlyExecutingJar();
        String outFilePath = constructOutFilePathFromJarFilePath(jarFilePath);
        writeJsonResultToOutFile(processingResult, outFilePath);
    }

    private File getPathOfCurrentlyExecutingJar() {
        File jarFile = null;
        try {
            jarFile = new File(TacosSpringApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            new RuntimeException(e);
        }
        return jarFile;
    }

    private String constructOutFilePathFromJarFilePath(File jarFilePath) {
        return jarFilePath.getParent() + File.separator + "textSampleResults.json";
    }

    private void writeJsonResultToOutFile(ProcessingResult processingResult, String outFilePath) {
        try {
            new ObjectMapper().writeValue(new File(outFilePath), processingResult);
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
    public class MostCommonWord {
        private String word;
        private long numberOfUses;

        public MostCommonWord() {}
    }
}
