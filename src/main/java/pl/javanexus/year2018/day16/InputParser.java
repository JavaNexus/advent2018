package pl.javanexus.year2018.day16;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {

    public static final int OPCODE_INDEX = 0;
    public static final int INPUT_A_INDEX = 1;
    public static final int INPUT_B_INDEX = 2;
    public static final int RESULT_INDEX = 3;

    private static final Pattern[] PATTERNS = {
            Pattern.compile("^Before: \\[([0-9]+, [0-9]+, [0-9]+, [0-9]+)]"),
            Pattern.compile("^[0-9]+ [0-9]+ [0-9]+ [0-9]+"),
            Pattern.compile("^After:  \\[([0-9]+, [0-9]+, [0-9]+, [0-9]+)]"),
            Pattern.compile("^$"),
    };
    public static final String REGISTER_DELIMITER = ", ";

    public List<DeviceInput> parseInput(List<String> lines) {
        List<DeviceInput> input = new LinkedList<>();

        int[] initialRegister = null;
        int[] expectedRegister = null;
        int[] opcode = null;

        int index = 0;
        for (String line : lines) {
            int lineType = index++ % PATTERNS.length;

            Matcher matcher = PATTERNS[lineType].matcher(line);
            if (matcher.find()) {
                switch (lineType) {
                    case 0:
                        initialRegister = parseIntArray(matcher.group(1), REGISTER_DELIMITER);
                        break;
                    case 1:
                        opcode = parseIntArray(matcher.group(), " ");
                        break;
                    case 2:
                        expectedRegister = parseIntArray(matcher.group(1), REGISTER_DELIMITER);
                        break;
                    case 3:
                        input.add(new DeviceInput(initialRegister, opcode, expectedRegister));
                        break;
                }
            } else {
                String exceptionMessage =
                        String.format("Unexpected line: %s [index: %d]; expected pattern: %s",
                                line, index, PATTERNS[lineType].pattern());
                throw new IllegalArgumentException(exceptionMessage);
            }
        }

        return input;
    }

    private int[] parseIntArray(String serialized, String delimiter) {
        return Arrays.stream(serialized.split(delimiter))
                .mapToInt(Integer::parseInt).toArray();
    }
}
