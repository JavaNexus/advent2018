package pl.javanexus.year2020.day6;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CustomCustomsTest {

    private InputReader inputReader;
    private CustomCustoms customCustoms;

    @Before
    public void setUp() throws Exception {
        inputReader = new InputReader();
        customCustoms = new CustomCustoms();
    }

    @Test
    public void shouldCountUnique() throws IOException {
        assertEquals(11,
                customCustoms.sumUniqueAnswers(inputReader.readStringValues("year2020/day6/input1.txt")));
        assertEquals(0,
                customCustoms.sumUniqueAnswers(inputReader.readStringValues("year2020/day6/input2.txt")));
    }

    @Test
    public void shouldCountCommon() throws IOException {
        assertEquals(6,
                customCustoms.sumCommonAnswers(inputReader.readStringValues("year2020/day6/input1.txt")));
        assertEquals(3628,
                customCustoms.sumCommonAnswers(inputReader.readStringValues("year2020/day6/input2.txt")));
    }
}
