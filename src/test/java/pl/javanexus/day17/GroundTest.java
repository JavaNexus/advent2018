package pl.javanexus.day17;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class GroundTest {

    private static final Point SPRING = new Point(500, 0);

    private InputParser inputParser;

    @Before
    public void setUp() throws Exception {
        this.inputParser = new InputParser();
    }

    @Test
    public void testSmall() throws IOException {
        Ground ground = new Ground(inputParser.parseInput("day17_original.input"));

        System.out.println("Before: ");
        ground.printGround();

        ground.simulateWaterFlow(SPRING, -1);

        System.out.println("After: ");
        ground.printGround();
    }

    @Test
    public void testMeasureFlow() throws IOException {
        Ground ground = new Ground(inputParser.parseInput("day17_test.input"));

        System.out.println("Before: ");
        ground.printGround();

        ground.simulateWaterFlow(SPRING, 3);
        System.out.println(ground.measureWaterVolume());
        //Answer A: 31038 Answer B: 25250

        System.out.println("After: ");
        ground.printGround();
    }
}
