package pl.javanexus.year2019.day19;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;
import pl.javanexus.year2019.day19.TractorBeam;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TractorBeamTest {

    private TractorBeam tractorBeam;

    @Before
    public void setUp() throws IOException {
        InputReader inputReader = new InputReader();
        long[] instructions =
                inputReader.readLongArray("year2019/day19/input1.csv", ",");
        this.tractorBeam = new TractorBeam(instructions);
    }

    @Test
    public void testFindTractorBeamArea() throws IOException {
        int[] types = tractorBeam.findTractorBeamArea(0, 0, 50, 50);
        assertEquals(213, types[TractorBeam.MOVING]);
    }

    @Test
    public void testFindTractorBeamStart() {
        tractorBeam.findTractorBeamArea(600, 800, 200, 200);
//        assertTrue(tractorBeam.checkIfFitsInTractorBeam(793, 1000, 100));
        //7501000 //too low
        //7900996 //too high //790, 996
        //7931000 //too high
    }
}
