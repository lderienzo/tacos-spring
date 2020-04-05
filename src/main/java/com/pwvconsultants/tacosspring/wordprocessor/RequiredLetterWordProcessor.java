package com.pwvconsultants.tacosspring.wordprocessor;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

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

import com.google.common.collect.ImmutableSet;

import lombok.AllArgsConstructor;
import lombok.Data;

public class RequiredLetterWordProcessor {
    private static final Set<Character> REQUIRED_LETTER_SET = ImmutableSet.of('A','E','I','L','N','O','R','S','T','U');
    private static final Pattern wordExtractingPattern = Pattern.compile("\\w+[']?\\w?(\\-\\w+'\\w)?");

    public ProcessingResult processText(String textBlock) {
        List<String> words = extractWordListFromTextBlock(textBlock);
        List<String> validWords = searchWordListForValidWords(words);
        List<String> normalizedValidWords = normalizeValidWordsToLowerCase(validWords);
        Map<String, Long> wordFrequencyMap = countWordFrequency(normalizedValidWords);
        Set<String> validWordSet = deDupeListByConvertingToSet(normalizedValidWords);
        String[] sortedArray = convertSetToSortedArray(validWordSet);

        ProcessingResult processingResult;
        if (wordFrequencyMap.size() > 0) {
            Map.Entry<String, Long> entry = wordFrequencyMap.entrySet().iterator().next();
            processingResult = new ProcessingResult(new MostCommonWord(entry.getKey(), entry.getValue()), sortedArray);
        }
        else
            processingResult = new ProcessingResult(new MostCommonWord("", 0), sortedArray);
        return processingResult;
    }

    // TODO -- COME BACK
    private String[] deriveSortedWordArrayResultFromTextBlock(String textBlock) {
        List<String> words = extractWordListFromTextBlock(textBlock);
        words = searchWordListForValidWords(words);
        words = normalizeValidWordsToLowerCase(words);
        Map<String, Long> wordFrequencyMap = countWordFrequency(words);
        Set<String> validWordSet = deDupeListByConvertingToSet(words);
        return convertSetToSortedArray(validWordSet);
    }

    private List<String> extractWordListFromTextBlock(String textBlock) {
        List<String> words = new ArrayList<>();
        Matcher matcher = wordExtractingPattern.matcher(textBlock);
        while (matcher.find()) {
            words.add(matcher.group());
        }
        return words;
    }

    private List<String> searchWordListForValidWords(List<String> words) {
        List<String> validWords = words.stream()
                .filter(w -> wordIsValid(w)).collect(Collectors.toList());
        return validWords;
    }

    private boolean wordIsValid(String word) {
        String normalizedWord = word.toUpperCase();
        Collection<Character> charCollection = convertWordToCharacterCollection(normalizedWord);
        return characterCollectionIsValid(charCollection);
    }

    private Collection<Character> convertWordToCharacterCollection(String word) {
        Character[] charObjectArray = word.chars().mapToObj(c -> (char)c).toArray(Character[]::new);
        return new LinkedList(Arrays.asList(charObjectArray));
    }

    private boolean characterCollectionIsValid(Collection<Character> charCollection) {
        charCollection.removeIf(c -> REQUIRED_LETTER_SET.contains(c));
        String word = convertCharCollectionBackToWordString(charCollection);
        return wordDoesNotConsistOfOnlyRequiredLetters(word) &&
                (onlyOneCharacterRemaining(word) || allRemainingCharactersAreTheSame(word));
    }

    private boolean wordDoesNotConsistOfOnlyRequiredLetters(String word) {
        return !word.equals("");
    }

    private String convertCharCollectionBackToWordString(Collection<Character> charCollection) {
        return charCollection.stream().map(String::valueOf).collect(Collectors.joining());
    }

    private boolean onlyOneCharacterRemaining(String word) {
        return word.length() == 1;
    }

    private boolean allRemainingCharactersAreTheSame(String word) {
        return word.chars().allMatch(c -> c == word.charAt(0));
    }

    private List<String> normalizeValidWordsToLowerCase(List<String> validWords) {
        return validWords.stream().map(w -> w.toLowerCase()).collect(Collectors.toList());
    }

    private Map<String, Long> countWordFrequency(List<String> validWords) {
        Map<String, Long> collect = validWords.stream().collect(groupingBy(Function.identity(), counting()));
        Map<String, Long> wordFrequencyMap = collect.entrySet()
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
        return wordFrequencyMap;
    }

    private Set<String> deDupeListByConvertingToSet(List<String> validWordList) {
        Set<String> validWordSet = new HashSet(validWordList);
        return validWordSet;
    }

    private String[] convertSetToSortedArray(Set<String> validWordSet) {
        String[] validWordArray = validWordSet.stream().toArray(String[]::new);
        Arrays.sort(validWordArray, Comparator.comparing(String::length));
        return validWordArray;
    }

    @Data
    @AllArgsConstructor
    public class ProcessingResult {
        private MostCommonWord mostCommonWord;
        private String[] remainingWords;

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
