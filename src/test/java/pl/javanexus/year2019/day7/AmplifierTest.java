package pl.javanexus.year2019.day7;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;
import pl.javanexus.year2019.Amplifier;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AmplifierTest {

    private InputReader inputReader;
    private Amplifier amplifier;

    @Before
    public void setUp() throws Exception {
        this.inputReader = new InputReader();
        this.amplifier = new Amplifier();
    }

    @Test
    public void testAmplifier() {
        assertEquals(43210, amplifier.execute(
                new int[]{3, 15, 3, 16, 1002, 16, 10, 16, 1, 16, 15, 15, 4, 15, 99, 0, 0},
                new int[]{4, 3, 2, 1, 0},
                0));
        assertEquals(54321, amplifier.execute(
                new int[]{3, 23, 3, 24, 1002, 24, 10, 24, 1002, 23, -1, 23, 101, 5, 23, 23, 1, 24, 23, 23, 4, 23, 99, 0, 0},
                new int[]{0, 1, 2, 3, 4},
                0));
        assertEquals(65210, amplifier.execute(
                new int[]{3, 31, 3, 32, 1002, 32, 10, 32, 1001, 31, -2, 31, 1007, 31, 0, 33, 1002, 33, 7, 33, 1, 33, 31, 31, 1, 32, 31, 31, 4, 31, 99, 0, 0, 0},
                new int[]{1, 0, 4, 3, 2},
                0));
    }

    @Test
    public void testFindHighestSignal() throws IOException {
        int[] instructions = inputReader.readIntArray("year2019/day7/input1.csv");
        int highestSignal = amplifier.findHighestSignal(instructions, new int[]{0, 1, 2, 3, 4}, 0);
        assertEquals(43812, highestSignal);
    }
}
