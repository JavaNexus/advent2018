package pl.javanexus.year2019.day2;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

public class IntcodeProgramTest {

    private static final int OUTPUT = 19690720;

    private IntcodeProgram intcodeProgram;
    private InputReader inputReader;

    @Before
    public void setUp() throws Exception {
        this.inputReader = new InputReader();
        this.intcodeProgram = new IntcodeProgram();
    }

    @Test
    public void testProgram() {
        evaluate(
                new int[]{3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50},
                new int[]{1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50});
        evaluate(
                new int[]{2, 0, 0, 0, 99},
                new int[]{1, 0, 0, 0, 99});
        evaluate(
                new int[]{2, 3, 0, 6, 99},
                new int[]{2, 3, 0, 3, 99});
        evaluate(
                new int[]{30, 1, 1, 4, 2, 5, 6, 0, 99},
                new int[]{1, 1, 1, 4, 99, 5, 6, 0, 99});
    }

    @Test
    public void testProgramAlarm() throws IOException {
        int[] output = getOutput(getInput(), 12, 2);
        intcodeProgram.execute(output);
        System.out.println(Arrays.toString(output));
        assertArrayEquals(new int[]{
                3716250, 12, 2, 2, 1, 1, 2, 3, 1, 3, 4, 3, 1, 5, 0, 3, 2, 1, 10, 48, 2, 9, 19, 144, 2, 23, 10, 576, 1, 6, 27, 578, 1, 31, 6, 580, 2, 35, 10, 2320, 1, 39, 5, 2321, 2, 6, 43, 4642, 2, 47, 10, 18568, 1, 51, 6, 18570, 1, 55, 6, 18572, 1, 9, 59, 18575, 1, 63, 9, 18578, 1, 67, 6, 18580, 2, 71, 13, 92900, 1, 75, 5, 92901, 1, 79, 9, 92904, 2, 6, 83, 185808, 1, 87, 5, 185809, 2, 6, 91, 371618, 1, 95, 9, 371621, 2, 6, 99, 743242, 1, 5, 103, 743243, 1, 6, 107, 743245, 1, 111, 10, 743249, 2, 115, 13, 3716245, 1, 119, 6, 3716247, 1, 123, 2, 3716249, 1, 127, 5, 0, 99, 2, 14, 0, 0
        }, output);
    }

    @Test
    public void testFindInput() throws IOException {
        int result = intcodeProgram.findInput(getInput(), OUTPUT);
        System.out.println(result);
    }

    private void evaluate(int[] expected, int[] input) {
        intcodeProgram.execute(input);
        assertArrayEquals(expected, input);

        System.out.println(Arrays.toString(input));
    }

    private int[] getOutput(int[] input, int noun, int verb) {
        int[] output = new int[input.length];
        intcodeProgram.resetOutput(input, output, noun, verb);

        return output;
    }

    private int[] getInput() throws IOException {
        return inputReader.readIntArray("year2019/day2/input1.csv", ",");
    }
}
