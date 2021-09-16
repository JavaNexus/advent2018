package pl.javanexus.year2018.day17;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.Line;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SpringTest {

    public static final String LINES_JSON_FILE = "D:\\Projekty\\Java\\advent2018\\src\\test\\resources\\spring\\lines.js";

    public static final int SPRING_X = 500;
    public static final int SPRING_Y = 0;
    private InputParser inputParser;

    @Before
    public void setUp() throws Exception {
        this.inputParser = new InputParser();
    }

    @Test
    public void testMeasureWaterFlow() throws IOException {
        ReservoirFactory reservoirFactory = new ReservoirFactory();
        Map<Point, Reservoir> reservoirs =
                reservoirFactory.createReservoirs(inputParser.parseInput("day17_test.input"));//"day17_small.input"
        Spring spring = new Spring(new Point(SPRING_X, SPRING_Y), reservoirs);
        assertEquals(57, spring.measureWaterFlow());
    }

    @Test
    public void testGetVolume() {
        Reservoir reservoir = new Reservoir(
                new Point(498, 10),
                new Point(504, 10),
                new Point(498, 13),
                new Point(504, 13));
        assertEquals(15, reservoir.getVolume());
    }

    @Test
    public void testIsUnderPoint() {
        Reservoir reservoir = new Reservoir(
                new Point(480, 32),
                new Point(503, 32),
                new Point(480, 43),
                new Point(503, 43));
        assertTrue(reservoir.isUnderPoint(new Point(490, 37)));
    }

    @Test
    public void testGetNextWaterSources() {
        Reservoir reservoir = new Reservoir(
                new Point(492, 9),
                new Point(512, 8),
                new Point(492, 21),
                new Point(512, 21));
        List<Point> waterSources = reservoir.getNextWaterSources();
        assertEquals(2, waterSources.size());

        Point left = waterSources.get(0);
        assertEquals(491, left.getX());
        assertEquals(10, left.getY());

        Point right = waterSources.get(1);
        assertEquals(513, right.getX());
        assertEquals(9, right.getY());
    }

    @Test
    public void testCreateReservoir() throws IOException {
        ReservoirFactory reservoirFactory = new ReservoirFactory();
        Map<Point, Reservoir> reservoirs =
                reservoirFactory.createReservoirs(inputParser.parseInput("day17_test.input"));
        assertFalse(reservoirs.isEmpty());
    }

    @Test
    public void testInputFile() throws IOException {
        String linesJson = inputParser.parseInput("day17_test.input").stream()
                .map(Line::toJSON)
                .collect(Collectors.joining(", ", "[", "]"));

        File file = new File(LINES_JSON_FILE);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(linesJson);
        fileWriter.flush();
        fileWriter.close();
    }
}
