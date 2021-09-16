package pl.javanexus.year2018.day2;

import java.util.*;
import java.util.stream.Collectors;

public class Inventory {

    private final List<String> values;
    private final int length;

    public Inventory(String[] values) {
        this(Arrays.stream(values).collect(Collectors.toList()));
    }

    public Inventory(List<String> values) {
        this.values = values;
        this.length = values.get(0).length();
    }

    public String getMatchingValue() {
        String match = null;
        for (int letterIndex = 0; letterIndex < length && match == null; letterIndex++) {
            Set<String> valuesWithoutOneLetter = new HashSet<>();
            for (int valueIndex  = 0; valueIndex < values.size() && match == null; valueIndex++) {
                String valueWithoutOneLetter = removeLetter(letterIndex, values.get(valueIndex));
                if (!valuesWithoutOneLetter.add(valueWithoutOneLetter)) {
                    match = valueWithoutOneLetter;
                }
            }
        }

        return match;
    }

    public Set<String> removeLetter(int index) {
        return values.stream()
                .map(value -> removeLetter(index, value))
                .collect(Collectors.toSet());
    }

    private String removeLetter(int index, String value) {
        return value.substring(0, index) + value.substring(index + 1);
    }

    public int getChecksum() {
        int valuesWith2RepeatedLetters = 0;
        int valuesWith3RepeatedLetters = 0;

        for (String value : values) {
            Map<Character, Integer> letters = countLetters(value);
            if (isAnyLetterRepeated(letters, 2)) {
                valuesWith2RepeatedLetters++;
            }
            if (isAnyLetterRepeated(letters, 3)) {
                valuesWith3RepeatedLetters++;
            }
        }

        return valuesWith2RepeatedLetters * valuesWith3RepeatedLetters;
    }

    private Map<Character, Integer> countLetters(String value) {
        Map<Character, Integer> letters = new HashMap<>();
        for (char letter : value.toCharArray()) {
            letters.compute(letter, (key, oldValue) -> oldValue == null ? 1 : oldValue + 1);
        }

        return letters;
    }

    private boolean isAnyLetterRepeated(Map<Character, Integer> letters, int numberOfRepetitions) {
        return letters.entrySet().stream()
                .filter(letterCounter -> letterCounter.getValue() == numberOfRepetitions)
                .findAny().isPresent();
    }


}
