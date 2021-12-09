package pl.javanexus.year2020.day10;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.javanexus.InputReader;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AdapterArrayTest {

    private InputReader inputReader;
    private AdapterArray adapterArray;

    @BeforeEach
    void setUp() {
        inputReader = new InputReader();
        adapterArray = new AdapterArray();
    }

    @ParameterizedTest
    @CsvSource({
            "year2020/day10/input1.txt, 7, 5",
            "year2020/day10/input2.txt, 22, 10",
            "year2020/day10/input3.txt, 65, 28"
    })
    void shouldFindAdapterDistribution(String fileName,
                                       int numberOf1JoltAdapters, int numberOf3JoltAdapters) throws IOException {
        int[] distribution = adapterArray.findAdapterDistribution(inputReader.readIntegerValues(fileName));
        assertThat(distribution[0], is(numberOf1JoltAdapters));
        assertThat(distribution[2], is(numberOf3JoltAdapters));
    }
}
