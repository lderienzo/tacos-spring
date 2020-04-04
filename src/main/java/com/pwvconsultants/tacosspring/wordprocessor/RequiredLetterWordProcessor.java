package com.pwvconsultants.tacosspring.wordprocessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;

public class RequiredLetterWordProcessor {
    private static final Set<Character> REQUIRED_LETTER_SET = ImmutableSet.of('A','E','I','L','N','O','R','S','T','U');
    private static final Pattern wordExtractingPattern = Pattern.compile("\\w+[']?\\w?(\\-\\w+'\\w)?");

    // TODO: The most common word and number of uses as an object.
    // e.g.   {word:”word”,numberOfUses:2}.

    // TODO: this is not working properly -- appears to be returning everything
    public String[] processText(String textBlock) {
        List<String> words = new ArrayList<>();
        Matcher matcher = wordExtractingPattern.matcher(textBlock);
        while (matcher.find()) {
            System.out.println(matcher.group());
            words.add(matcher.group());
        }
        Set<String> validWords = words.stream().filter(w -> wordIsValid(w)).collect(Collectors.toSet());
        return convertSetToSortedArray(validWords);
    }

    private boolean wordIsValid(String word) {
        String normalizedWord = normalizeWordToUpperCase(word);
        Collection<Character> charCollection = convertWordToCharacterCollection(normalizedWord);
        return characterCollectionIsValid(charCollection);
    }

    private String normalizeWordToUpperCase(String word) {
        return word.toUpperCase();
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

    private String[] convertSetToSortedArray(Set<String> validWordSet) {
        String[] validWordArray = validWordSet.stream().toArray(String[]::new);
        Arrays.sort(validWordArray, Comparator.comparing(String::length));
        return validWordArray;
//        Arrays.stream(validWordArray).forEach(System.out::println);
    }

    private void processWordOld(String word) {
        String upperCase = word.toUpperCase();
        Character[] charObjectArray = upperCase.chars().mapToObj(c -> (char)c).toArray(Character[]::new);
        Collection<Character> strCharLinkedList = new LinkedList(Arrays.asList(charObjectArray));
        strCharLinkedList.removeIf(c -> REQUIRED_LETTER_SET.contains(c));
        Set<String> validWordSet = new HashSet<>();
        if (strCharLinkedList.size() == 1) {
            System.out.println("STRING PASSES");
            validWordSet.add(word);
        }
        else {
            String string = strCharLinkedList.stream().map(String::valueOf).collect(Collectors.joining());
            if (string.chars().allMatch(c -> c == string.charAt(0))) {
                System.out.println("STRING PASSES");
                validWordSet.add(word);
            }
            else
                System.out.println("STRING FAILS");
        }
        strCharLinkedList.forEach(System.out::println);

        String[] validWordArray = validWordSet.stream().toArray(String[]::new);
        Arrays.sort(validWordArray, Comparator.comparing(String::length));
        Arrays.stream(validWordArray).forEach(System.out::println);
    }
}
