package pl.javanexus.day17;

import org.junit.Test;
import pl.javanexus.InputReader;
import pl.javanexus.Line;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;

public class SpringTest {

    public static final String LINES_JSON_FILE = "D:\\Projekty\\Java\\advent2018\\src\\test\\resources\\spring\\lines.js";
    //    x=628, y=204..206
    //    y=1663, x=597..601
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

    private List<Line> parseInput(String inputFileName) throws IOException {
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

    @Test
    public void testCreateReservoir() throws IOException {
        ReservoirFactory reservoirFactory = new ReservoirFactory();
        Map<Point, Reservoir> reservoirs =
                reservoirFactory.createReservoirs(parseInput("day17_test.input"));
        assertFalse(reservoirs.isEmpty());
    }

    @Test
    public void testInputFile() throws IOException {
        String linesJson = parseInput("day17_test.input").stream()
                .map(Line::toJSON)
                .collect(Collectors.joining(", ", "[", "]"));

        File file = new File(LINES_JSON_FILE);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(linesJson);
        fileWriter.flush();
        fileWriter.close();
    }
}
