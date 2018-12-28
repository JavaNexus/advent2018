package pl.javanexus.day16;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {

    private static final Pattern[] PATTERNS = {
            Pattern.compile("^Before: \\[([0-9]+, [0-9]+, [0-9]+, [0-9]+)]"),
            Pattern.compile("^([0-9]+) ([0-9]+) ([0-9]+) ([0-9]+)"),
            Pattern.compile("^After:  \\[([0-9]+, [0-9]+, [0-9]+, [0-9]+)]"),
            Pattern.compile("^$"),
    };
    public static final String REGISTER_DELIMITER = ", ";

    public List<DeviceInput> parseInput(List<String> lines) {
        List<DeviceInput> input = new LinkedList<>();

        int index = 0;
        DeviceInput.DeviceInputBuilder inputBuilder = null;
        for (String line : lines) {
            int lineType = index++ % PATTERNS.length;

            Matcher matcher = PATTERNS[lineType].matcher(line);
            if (matcher.find()) {
                switch (lineType) {
                    case 0:
                        inputBuilder = DeviceInput.builder().initialRegisterValues(parseIntArray(matcher.group(1), REGISTER_DELIMITER));
                        break;
                    case 1:
                        inputBuilder = inputBuilder
                                .inputA(Integer.parseInt(matcher.group(2)))
                                .inputB(Integer.parseInt(matcher.group(3)))
                                .resultRegisterIndex(Integer.parseInt(matcher.group(4)));
                        break;
                    case 2:
                        inputBuilder = inputBuilder.expectedRegisterValues(parseIntArray(matcher.group(1), REGISTER_DELIMITER));
                        break;
                    case 3:
                        input.add(inputBuilder.build());
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
