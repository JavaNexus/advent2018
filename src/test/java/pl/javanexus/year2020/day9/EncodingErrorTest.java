package pl.javanexus.year2020.day9;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EncodingErrorTest {

    private InputReader inputReader;
    private EncodingError encodingError;

    @BeforeEach
    public void setUp() throws Exception {
        inputReader = new InputReader();
        encodingError = new EncodingError();
    }

    @ParameterizedTest
    @CsvSource({
            "year2020/day9/input1.txt, 5, 127",
            "year2020/day9/input2.txt, 25, 1038347917"
    })
    public void shouldFindFirstInvalidNumber(String fileName, int preambleLength,
                                             BigInteger expectedFirstInvalidValue) throws IOException {
        assertThat(encodingError.findFirstInvalidValue(getValues(fileName), preambleLength),
                is(expectedFirstInvalidValue));
    }

    @ParameterizedTest
    @MethodSource
    public void shouldFindSmallSubsetWithSum(String fileName, BigInteger sum, List<BigInteger> expectedSubSetWithSum,
                                             BigInteger expectedMinMaxSum) throws IOException {
        List<BigInteger> subsetWithSum = encodingError.findSubsetWithSum(getValues(fileName), sum);
        assertThat(subsetWithSum, is(expectedSubSetWithSum));
        assertThat(encodingError.sumMinAndMax(subsetWithSum), is(expectedMinMaxSum));
    }

    static Stream<Arguments> shouldFindSmallSubsetWithSum() {
        return Stream.of(
                Arguments.of("year2020/day9/input1.txt",
                        new BigInteger("127"),
                        toBigInteger(15, 25, 47, 40),
                        new BigInteger("62")),
                Arguments.of("year2020/day9/input2.txt",
                        new BigInteger("1038347917"),
                        toBigInteger(44615102, 43725476, 44648839, 45029251, 47640942, 56365291,
                                79304138, 62256657, 46882839, 82571714, 49053440, 62979028,
                                68834924, 57938100, 93668542, 81648351, 71185283),
                        new BigInteger("137394018"))
        );
    }

    private static List<BigInteger> toBigInteger(long... values) {
        return LongStream.of(values).mapToObj(BigInteger::valueOf).collect(Collectors.toList());
    }

    private List<BigInteger> getValues(String fileName) throws IOException {
        return inputReader.readValues(fileName, (index, value) -> new BigInteger(value));
    }
}
