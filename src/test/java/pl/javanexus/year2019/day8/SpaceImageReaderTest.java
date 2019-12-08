package pl.javanexus.year2019.day8;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SpaceImageReaderTest {

    private SpaceImageReader spaceImageReader;
    private InputReader inputReader;

    @Before
    public void setUp() throws Exception {
        this.inputReader = new InputReader();
        this.spaceImageReader = new SpaceImageReader();
    }

    @Test
    public void testReadInput() {
        String input = "123456789012";
        List<int[][]> layers = spaceImageReader.readInput(3, 2, input);
        int result = spaceImageReader.getLayerWithMinNumberOfDigit(layers, 0);
        assertEquals(1, result);
    }

    @Test
    public void testDecode() {
        final int width = 2;
        final int height = 2;
        List<int[][]> layers = spaceImageReader.readInput(width, height, "0222112222120000");
        spaceImageReader.decode(layers, width, height);
    }

    @Test
    public void testReadInputFromFile() throws IOException {
        final int width = 25;
        final int height = 6;
        final int[] input = inputReader.readIntArray("year2019/day8/input1.csv", "");
        List<int[][]> layers = spaceImageReader.readInput(width, height, input);
        int result = spaceImageReader.getLayerWithMinNumberOfDigit(layers, 0);
        assertEquals(2356, result);

        spaceImageReader.decode(layers, width, height);
    }
}
