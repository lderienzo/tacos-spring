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

public class RequiredLetterWordProcessor {
    private static final Set<Character> REQUIRED_LETTER_SET = ImmutableSet.of('A','E','I','L','N','O','R','S','T','U');
    private static final Pattern wordExtractingPattern = Pattern.compile("\\w+[']?\\w?(\\-\\w+'\\w)?");

    // TODO: The most common word and number of uses as an object.
    // e.g.   {word:”word”,numberOfUses:2}.

    public String[] processText(String textBlock) {
        List<String> words = new ArrayList<>();
        Matcher matcher = wordExtractingPattern.matcher(textBlock);
System.out.println("-------- Original Words --------");
        while (matcher.find()) {
System.out.println(matcher.group());
            words.add(matcher.group());
        }

        List<String> validWords = words.stream()
                .filter(w -> wordIsValid(w)).collect(Collectors.toList());
System.out.println("-------- Valid Words --------");
validWords.stream().forEach(System.out::println);

        List<String> normalizedValidWords = validWords.stream().map(w -> w.toLowerCase()).collect(Collectors.toList());
        Map<String, Long> wordFrequencyMap = countWordFrequency(normalizedValidWords);
System.out.println("-------- Word Frequencies --------");
wordFrequencyMap.entrySet().stream().forEach(System.out::println);

        Set<String> validWordSet = deDupeListByConvertingToSet(normalizedValidWords);
System.out.println("-------- De-duped Words --------");
validWordSet.stream().forEach(System.out::println);

        String[] sortedArray = convertSetToSortedArray(validWordSet);
System.out.println("-------- Sorted De-duped Words --------");
Arrays.stream(sortedArray).forEach(System.out::println);
        return sortedArray;
    }

    // TODO -- MOVE
    private Map<String, Long> countWordFrequency(List<String> validWords) {
        Map<String, Long> collect = validWords.stream().collect(groupingBy(Function.identity(), counting()));
        return collect.entrySet()
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
        return onlyOneCharacterRemaining(charCollection) || allRemainingCharactersAreTheSame(word);
    }

    private String convertCharCollectionBackToWordString(Collection<Character> charCollection) {
        return charCollection.stream().map(String::valueOf).collect(Collectors.joining());
    }

    private boolean onlyOneCharacterRemaining(Collection<Character> charCollection) {
        return charCollection.size() == 1;
    }

    private boolean allRemainingCharactersAreTheSame(String word) {
        return word.chars().allMatch(c -> c == word.charAt(0));
    }

    private Set<String> deDupeListByConvertingToSet(List<String> validWordList) {
        return new HashSet(validWordList);
    }

    private String[] convertSetToSortedArray(Set<String> validWordSet) {
        String[] validWordArray = validWordSet.stream().toArray(String[]::new);
        Arrays.sort(validWordArray, Comparator.comparing(String::length));
        return validWordArray;
    }
}
