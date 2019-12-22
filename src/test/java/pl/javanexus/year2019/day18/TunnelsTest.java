package pl.javanexus.year2019.day18;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TunnelsTest {

    private InputReader inputReader;

    @Before
    public void setUp() throws Exception {
        this.inputReader = new InputReader();
    }

    @Test
    public void testCollectKeys() throws IOException {
        final Tunnels tunnels =
                new Tunnels(inputReader.readStringValues("year2019/day18/input1.csv"));
        int distance = tunnels.collectKeys();
        System.out.println("Distance: " + distance);
    }

    @Test
    public void testSample() throws IOException {
        Tunnels tunnels =
                new Tunnels(inputReader.readStringValues("year2019/day18/input_sample_001.csv"));
        assertEquals(8, tunnels.collectKeys());

        tunnels =
                new Tunnels(inputReader.readStringValues("year2019/day18/input_sample_002.csv"));
        assertEquals(86, tunnels.collectKeys());
    }
}
