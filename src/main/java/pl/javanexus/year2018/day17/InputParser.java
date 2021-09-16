package pl.javanexus.year2018.day17;

import pl.javanexus.InputReader;
import pl.javanexus.Line;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {

    public static final Pattern PATTERN = Pattern.compile("(x|y)=([0-9]+), (x|y)=([0-9]+)..([0-9]+)");

    enum InputLineType {
        CONST_X("x") {
            @Override
            public Line getLine(int constant, int from, int to) {
                return new Line(new Point(constant, from), new Point(constant, to));
            }
        },
        CONST_Y("y") {
            @Override
            public Line getLine(int constant, int from, int to) {
                return new Line(new Point(from, constant), new Point(to, constant));
            }
        };

        private final String dimension;

        InputLineType(String dimension) {
            this.dimension = dimension;
        }

        public abstract Line getLine(int constant, int from, int to);

        public static InputLineType getByConstantDimension(String dimension) {
            return CONST_X.dimension.equals(dimension) ? CONST_X : CONST_Y;
        }
    }

    public List<Line> parseInput(String inputFileName) throws IOException {
        return new InputReader().readValues(inputFileName,
                (index, line) -> {
                    Matcher matcher = PATTERN.matcher(line);
                    if (matcher.find()) {
                        return InputLineType.getByConstantDimension(matcher.group(1)).getLine(
                                Integer.parseInt(matcher.group(2)),
                                Integer.parseInt(matcher.group(4)),
                                Integer.parseInt(matcher.group(5)));
                    } else {
                        throw new IllegalArgumentException("Unexpected line pattern: " + line);
                    }
                });
    }
}
