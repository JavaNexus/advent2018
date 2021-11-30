package pl.javanexus.year2020.day9;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

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

    @Test
    public void shouldFindSmallSubsetWithSum() throws IOException {
        List<BigInteger> subsetWithSum = encodingError.findSubsetWithSum(
                getValues("year2020/day9/input1.txt"), new BigInteger("127"));
        assertEquals(toBigInteger(15, 25, 47, 40), subsetWithSum);
        assertEquals(new BigInteger("62"), encodingError.sumMinAndMax(subsetWithSum));
    }

    @Test
    public void shouldFindLargeSubsetWithSum() throws IOException {
        List<BigInteger> subsetWithSum = encodingError.findSubsetWithSum(
                getValues("year2020/day9/input2.txt"), new BigInteger("1038347917"));
        assertEquals(new BigInteger("137394018"), encodingError.sumMinAndMax(subsetWithSum));
    }

    private List<BigInteger> toBigInteger(long... values) {
        return LongStream.of(values).mapToObj(BigInteger::valueOf).collect(Collectors.toList());
    }

    private List<BigInteger> getValues(String fileName) throws IOException {
        return inputReader.readValues(fileName, (index, value) -> new BigInteger(value));
    }
}
