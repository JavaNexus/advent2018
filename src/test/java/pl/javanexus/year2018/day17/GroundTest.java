package pl.javanexus.year2018.day17;

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
    public void testSimulateWaterFlow() throws IOException {
        Ground ground = new Ground(inputParser.parseInput("day17_small.input"));
        //original
        // top_wide

        System.out.println("Before: ");
        ground.printGround(450);

        ground.simulateWaterFlow(SPRING);
        System.out.println(ground.measureWaterVolume());

        System.out.println("After: ");
        ground.printGround(450);
    }

    @Test
    public void testMeasureWaterFlow() throws IOException {
        Ground ground = new Ground(inputParser.parseInput("day17_test.input"), 500);

        System.out.println("Before: ");
//        ground.printGround(400);

        ground.simulateWaterFlow(SPRING);
        System.out.println(ground.measureWaterVolume());
        //Answer A: 31038 Answer B: 25250

        System.out.println("After: ");
        ground.printGround(400);
    }
}
