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


    private RequiredLetterWordProcessor() {
    }

    public static void processText(String text) {
        RequiredLetterWordProcessor letterWordProcessor = new RequiredLetterWordProcessor();
        List<String> words = new ArrayList<>();
        Matcher matcher = wordExtractingPattern.matcher(text);
        while (matcher.find()) {
            System.out.println(matcher.group());
            words.add(matcher.group());
        }
        words.stream().forEach(s -> letterWordProcessor.processWord(s));
    }

    private void processWord(String word) {
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
