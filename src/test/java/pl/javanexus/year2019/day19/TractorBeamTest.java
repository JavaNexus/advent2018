package pl.javanexus.year2019.day19;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;
import pl.javanexus.year2019.day19.TractorBeam;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TractorBeamTest {

    private InputReader inputReader;

    @Before
    public void setUp() {
        this.inputReader = new InputReader();
    }

    @Test
    public void testFindTractorBeamArea() throws IOException {
        long[] instructions =
                inputReader.readLongArray("year2019/day19/input1.csv", ",");
        TractorBeam tractorBeam = new TractorBeam(50, 50, instructions);
        tractorBeam.findTractorBeamArea();
        assertEquals(213, tractorBeam.countFieldsByType()[TractorBeam.MOVING]);
    }
}
