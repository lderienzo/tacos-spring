package com.pwvconsultants.wordprocessor;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableSet;

public class RequiredLetterWordProcessorTest {

    private static final Set<Character> REQUIRED_LETTER_SET = ImmutableSet.of('A','E','I','L','N','O','R','S','T','U');

    @Test
    public void testWordProcessor() {
        String strToCheck = "households";
        // iterate over each character in string and check if its in set.
        // if so remove it.
        String upperCase = strToCheck.toUpperCase();
        Character[] charObjectArray = upperCase.chars().mapToObj(c -> (char)c).toArray(Character[]::new);
        Collection<Character> strCharLinkedList = new LinkedList(Arrays.asList(charObjectArray));
        strCharLinkedList.removeIf(c -> REQUIRED_LETTER_SET.contains(c));
        if (strCharLinkedList.size() == 1)
            System.out.println("STRING PASSES");
        else {
            String string = strCharLinkedList.stream().map(String::valueOf).collect(Collectors.joining());
            if (string.chars().allMatch(c -> c == string.charAt(0)))
                System.out.println("STRING PASSES");
            else
                System.out.println("STRING FAILS");
        }
        strCharLinkedList.forEach(System.out::println);
    }
}
