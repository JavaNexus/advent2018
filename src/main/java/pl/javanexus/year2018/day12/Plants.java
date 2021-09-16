package pl.javanexus.year2018.day12;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Plants {

    private static final char POT_WITH_PLANT = '#';
    private static final Pattern SPREAD_PATTERN_REGEX = Pattern.compile("([.#]{5}) => (.|#)");

    private static final int CAPACITY = 8192;//1024;
    private static final byte EMPTY = 0;
    private static final byte FULL = 1;
    public static final int PATTERN_LENGTH = 5;

    private final ByteBuffer pots;
    private final Map<Integer, Byte> patterns;

    public Plants(String initialState, String[] spreadPatterns) {
        this.pots = parseInitialState(initialState);
        this.patterns = parsePatterns(spreadPatterns);
    }

    private Map<Integer, Byte> parsePatterns(String[] spreadPatterns) {
        Map<Integer, Byte> patternsMap = new HashMap<>();

        for (String spreadPattern : spreadPatterns) {
            Matcher matcher = SPREAD_PATTERN_REGEX.matcher(spreadPattern);
            if (matcher.find()) {
                patternsMap.put(
                        toInt(convertPotsPatternToByteArray(matcher.group(1))),
                        getPotState(matcher.group(2).charAt(0)));
            } else {
                throw new IllegalArgumentException("Unexpected spread pattern: " + spreadPattern);
            }
        }

        return patternsMap;
    }

    private Integer toInt(byte[] bytes) {
        int lastDigitIndex = bytes.length - 1;

        int value = 0;
        for (int i = 0; i < bytes.length; i++) {
            value |= Integer.rotateLeft(bytes[lastDigitIndex - i], i);
        }
        return value;
    }

    private byte[] convertPotsPatternToByteArray(String pattern) {
        byte[] buffer = new byte[pattern.length()];
        for (int i = 0; i < pattern.length(); i++) {
            buffer[i] = getPotState(pattern.charAt(i));
        }

        return buffer;
    }

    private byte getPotState(char pot) {
        return pot == POT_WITH_PLANT ? FULL : EMPTY;
    }

    private ByteBuffer parseInitialState(String initialState) {
        ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);
        buffer.put(new byte[PATTERN_LENGTH]);
        buffer.put(convertPotsPatternToByteArray(initialState));
        buffer.put(new byte[PATTERN_LENGTH]);

        return buffer;
    }

    public int grow(int numberOfGenerations) {
        ByteBuffer currentGeneration = pots;
        for (int i = 0; i < numberOfGenerations; i++) {
            currentGeneration = growNextGeneration(currentGeneration);
        }

        return getPotNumbersSum(currentGeneration, numberOfGenerations);
    }

    private int getPotNumbersSum(ByteBuffer currentGeneration, int numberOfGenerations) {
        int firstPotIndex = numberOfGenerations * 3 + PATTERN_LENGTH;
        int lastPotIndex = currentGeneration.position();
        currentGeneration.position(0);

        int sum = 0;
        for (int i = 0; i < lastPotIndex; i++) {
            sum += currentGeneration.get() == FULL ? i - firstPotIndex : 0;
        }

        return sum;
    }

    private ByteBuffer growNextGeneration(ByteBuffer currentGeneration) {
        ByteBuffer nextGeneration = ByteBuffer.allocate(CAPACITY);
        nextGeneration.put(new byte[PATTERN_LENGTH]);

        final byte[] potsToConsider = new byte[PATTERN_LENGTH];

        int lastPotIndex = currentGeneration.position();
        for (int i = 0; i < lastPotIndex; i++) {
            currentGeneration.position(i);
            currentGeneration.get(potsToConsider);

            Integer patternAsInt = toInt(potsToConsider);

            Byte nextState = Optional.ofNullable(patterns.get(patternAsInt)).orElse(EMPTY);
            nextGeneration.put(nextState);
        }
        nextGeneration.put(new byte[PATTERN_LENGTH]);

        return nextGeneration;
    }
}
