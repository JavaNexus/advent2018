package pl.javanexus.year2020.day5;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class BinaryBoarding {

    public static final int ROW_MULTIPLIER = 8;

    private final Pattern passPattern = Pattern.compile("([BF]{7})([RL]{3})");

    public int getHighestSeatId(List<String> passes) {
        return getAllSeatIds(passes).max().orElseThrow(() -> new IllegalStateException("Highest seat id not found"));
    }

    //487 = 533
    //488 = 535
    public int getEmptySeatId(List<String> passes) {
        int[] seatIds = getAllSeatIds(passes).toArray();
        for (int previousSeatId = seatIds[0], i = 1; i < seatIds.length; i++) {
            if (seatIds[i] - previousSeatId > 1) {
                return seatIds[i] - 1;
            }
            previousSeatId = seatIds[i];
        }

        throw new RuntimeException("Empty seat not found");
    }

    private IntStream getAllSeatIds(List<String> passes) {
        return passes.stream().mapToInt(this::getSeatId).sorted();
    }

    public int getSeatId(String pass) {
        Matcher matcher = passPattern.matcher(pass);
        if (matcher.find()) {
            int row = toDecimal(matcher.group(1), 'F', 'B');
            int column = toDecimal(matcher.group(2), 'L', 'R');
            return row * ROW_MULTIPLIER + column;
        } else {
            throw new IllegalArgumentException("Unexpected pass format: " + pass);
        }
    }

    public int toDecimal(String value, char zeroCharacter, char oneCharacter) {
        return Integer.parseInt(value.replace(zeroCharacter, '0').replace(oneCharacter, '1'), 2);
    }
}
