package pl.javanexus.year2020.day9;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EncodingErrorTest {

    private InputReader inputReader;
    private EncodingError encodingError;

    @Before
    public void setUp() throws Exception {
        inputReader = new InputReader();
        encodingError = new EncodingError();
    }

    @Test
    public void shouldFindFirstInvalidNumber() throws IOException {
        assertEquals(new BigInteger("127"), encodingError.findFirstInvalidValue(
                getValues("year2020/day9/input1.txt"), 5));
        assertEquals(new BigInteger("1038347917"), encodingError.findFirstInvalidValue(
                getValues("year2020/day9/input2.txt"), 25));
    }

    private List<BigInteger> getValues(String fileName) throws IOException {
        return inputReader.readValues(fileName, (index, value) -> new BigInteger(value));
    }
}
