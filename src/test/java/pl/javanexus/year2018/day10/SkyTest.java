package pl.javanexus.year2018.day10;

import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

public class SkyTest {

    public static final Pattern LINE_PATTERN =
            Pattern.compile("position=<[ ]*([-0-9]+), [ ]*([-0-9]+)> velocity=<[ ]*([-0-9]+), [ ]*([-0-9]+)>");
    public static final String STARS_JS_PATH =
            "D:\\Projekty\\Java\\advent2018\\src\\test\\resources\\sky\\";

    @Test
    public void testAlignStars() throws IOException {
        Sky sky = alignStarsFromFile("day10_stars.input");
        System.out.println(sky.getTime());
        saveAlignedPointsToFile(sky.printStars(), "alignedStars.js");
    }

    @Test
    public void testSimpleInput() throws IOException {
        Sky sky = alignStarsFromFile("day10_test.input");
        assertEquals(3, sky.getTime());
        saveAlignedPointsToFile(sky.printStars(), "testStars.js");
    }

    private Sky alignStarsFromFile(String inputFileName) throws IOException {
        final Sky sky = new Sky(readStarsFromFile(inputFileName));
        while (sky.isAreaAfterNextStepSmaller()) {
            sky.moveStarsByOneStep();
        }

        return sky;
    }

    private void saveAlignedPointsToFile(String starsJs, String outputFileName) throws IOException {
        FileWriter fileWriter = new FileWriter(STARS_JS_PATH + outputFileName);
        fileWriter.write(starsJs);
        fileWriter.close();
    }

    private List<Star> readStarsFromFile(String fileName) throws IOException {
        return new InputReader().readValues(fileName, (index, line) -> {
            Matcher matcher = LINE_PATTERN.matcher(line);
            if (matcher.find()) {
                return new Star(index,
                        Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)),
                        Integer.parseInt(matcher.group(3)),
                        Integer.parseInt(matcher.group(4)));
            } else {
                throw new IllegalArgumentException("Unexpected line format: " + index + ": " + line);
            }
        });
    }
}
