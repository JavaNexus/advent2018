package pl.javanexus.year2019.day14;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NanofactoryTest {

    private InputReader inputReader;
    private Nanofactory nanofactory;

    @Before
    public void setUp() throws Exception {
        this.inputReader = new InputReader();
        this.nanofactory = new Nanofactory();
    }

    @Test
    public void testNanofactory() throws IOException {
        //input_sample_1.csv //13312
        //input1.csv //???
        List<String> input = inputReader.readStringValues("year2019/day14/input_tiny_1.csv");
        nanofactory.readInput(input);
        assertEquals(165, nanofactory.calculateTotalNumberOfOreUnits());
    }
}
