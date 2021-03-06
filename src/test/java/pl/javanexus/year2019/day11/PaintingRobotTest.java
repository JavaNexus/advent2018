package pl.javanexus.year2019.day11;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class PaintingRobotTest {

    private InputReader inputReader;

    @Before
    public void setUp() throws Exception {
        this.inputReader = new InputReader();
    }

    @Test
    public void testPaintingRobotFromInput() throws IOException {
        long[] instructions = inputReader.readLongArray("year2019/day11/input1.csv", ",");
        PaintingRobot paintingRobot = new PaintingRobot(100, 100, instructions);
        paintingRobot.paintFromCenter();

        assertEquals(2252, paintingRobot.countPanelsPaintedAtLeastOnce());
    }

    @Test
    public void testPaintingRegistrationNumber() throws IOException {
        long[] instructions = inputReader.readLongArray("year2019/day11/input1.csv", ",");
        PaintingRobot paintingRobot = new PaintingRobot(100, 100, instructions);

        PaintingRobot.Step start = paintingRobot.getStartingStep();
        paintingRobot.paintPanel(start.getX(), start.getY(), PaintingRobot.COLOR_WHITE);
        paintingRobot.paint(start);
        paintingRobot.printHull();//AGALRGJE
    }

    @Test
    public void testPaintingRobot() {
        PaintingRobot paintingRobot = new PaintingRobot(5, 5, new long[]{3, 100});
        int[] numberOfPaintedPanels = paintingRobot.countPaintedPanelsByColor();
        assertEquals(25, numberOfPaintedPanels[PaintingRobot.COLOR_BLACK]);
        assertEquals(0, numberOfPaintedPanels[PaintingRobot.COLOR_WHITE]);
    }
}
