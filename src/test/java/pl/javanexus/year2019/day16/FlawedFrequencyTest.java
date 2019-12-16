package pl.javanexus.year2019.day16;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

public class FlawedFrequencyTest {

    private InputReader inputReader;

    @Before
    public void setUp() throws Exception {
        this.inputReader = new InputReader();
    }

    @Test
    public void testCalculateNextPhase() {
        FlawedFrequency flawedFrequency = new FlawedFrequency(new int[]{1, 2, 3, 4, 5, 6, 7, 8});

        flawedFrequency.calculateNextPhase();
        assertArrayEquals(new int[] {4, 8, 2, 2, 6, 1, 5, 8}, flawedFrequency.getCurrentPhase());
        flawedFrequency.calculateNextPhase();
        assertArrayEquals(new int[] {3, 4, 0, 4, 0, 4, 3, 8}, flawedFrequency.getCurrentPhase());
        flawedFrequency.calculateNextPhase();
        assertArrayEquals(new int[] {0, 3, 4, 1, 5, 5, 1, 8}, flawedFrequency.getCurrentPhase());
        flawedFrequency.calculateNextPhase();
        assertArrayEquals(new int[] {0, 1, 0, 2, 9, 4, 9, 8}, flawedFrequency.getCurrentPhase());
    }

    @Test
    public void testCalculateNextPhases() {
        FlawedFrequency flawedFrequency =
                new FlawedFrequency(new int[]{8, 0, 8, 7, 1, 2, 2, 4, 5, 8, 5, 9, 1, 4, 5, 4, 6, 6, 1, 9, 0, 8, 3, 2, 1, 8, 6, 4, 5, 5, 9, 5});
        flawedFrequency.calculateNextPhases(100);
        assertArrayEquals(
                new int[] {2, 4, 1, 7, 6, 1, 7, 6},
                Arrays.copyOf(flawedFrequency.getCurrentPhase(), 8));
        // becomes 24176176
    }

    @Test
    public void testInput() throws IOException {
        int[] input = inputReader.readIntArray("year2019/day16/input1.csv", "");

        FlawedFrequency flawedFrequency = new FlawedFrequency(input);
        flawedFrequency.calculateNextPhases(100);

        System.out.println(Arrays.toString(flawedFrequency.getCurrentPhase()));
        //11833188
    }

    @Test
    public void testLargeInput() throws IOException {
        int[] input = inputReader.readIntArray("year2019/day16/input1.csv", "");

        FlawedFrequency flawedFrequency = new FlawedFrequency(input, 10000);
        flawedFrequency.calculateNextPhases(100);

        int offset = 5976809;
        int[] output = new int[8];
        System.arraycopy(flawedFrequency.getCurrentPhase(), offset, output, 0, output.length);
        System.out.println(Arrays.toString(output));
    }
}
