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
        Ground ground = new Ground(inputParser.parseInput("day17_top.input"));

        System.out.println("Before: ");
        ground.printGround(400);

        ground.simulateWaterFlow(SPRING);

        System.out.println("After: ");
        ground.printGround(400);
    }

    @Test
    public void testMeasureFlow() throws IOException {
        Ground ground = new Ground(inputParser.parseInput("day17_test.input"));

        System.out.println("Before: ");
        ground.printGround(400);

        ground.simulateWaterFlow(SPRING);
        System.out.println(ground.measureWaterVolume());
        //Answer A: 31038 Answer B: 25250

        System.out.println("After: ");
        ground.printGround(400);
    }

    @Test
    public void testFlood() throws IOException {
        final Point waterSource = SPRING;//new Point(10, 0);
        final String inputFileName =
            //"day17_overflow_on_the_right.input";
            //"day17_overflow_on_the_left.input";
            //"day17_overflow_on_both_sides.input";
                "day17_original.input";

        Ground ground = new Ground(inputParser.parseInput(inputFileName));

        System.out.println("Before: ");
        ground.printGround(450);

        ground.flood(waterSource);

        System.out.println("After: ");
        ground.printGround(450);
    }
}
