package pl.javanexus.year2020.day5;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BinaryBoardingTest {

    private BinaryBoarding binaryBoarding;
    private InputReader inputReader;

    @Before
    public void setUp() throws Exception {
        binaryBoarding = new BinaryBoarding();
        inputReader = new InputReader();
    }

    @Test
    public void shouldFindEmptySeatId() throws IOException {
        List<String> passes = inputReader.readStringValues("year2020/day5/input1.txt");
        assertEquals(534, binaryBoarding.getEmptySeatId(passes));
    }

    @Test
    public void shouldGetHighestSeatId() throws IOException {
        List<String> passes = inputReader.readStringValues("year2020/day5/input1.txt");
        assertEquals(991, binaryBoarding.getHighestSeatId(passes));
    }

    @Test
    public void shouldGetSeatId() {
        assertEquals(567, binaryBoarding.getSeatId("BFFFBBFRRR"));
        assertEquals(119, binaryBoarding.getSeatId("FFFBBBFRRR"));
        assertEquals(820, binaryBoarding.getSeatId("BBFFBBFRLL"));
    }

    @Test
    public void shouldConvertToBinary() {
        assertEquals(44, binaryBoarding.toDecimal("FBFBBFF", 'F', 'B'));
        assertEquals(70, binaryBoarding.toDecimal("BFFFBBF", 'F', 'B'));
        assertEquals(14, binaryBoarding.toDecimal("FFFBBBF", 'F', 'B'));
        assertEquals(102, binaryBoarding.toDecimal("BBFFBBF", 'F', 'B'));
        assertEquals(5, binaryBoarding.toDecimal("RLR", 'L', 'R'));
    }
}
