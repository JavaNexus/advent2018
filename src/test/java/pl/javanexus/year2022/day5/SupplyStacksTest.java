package pl.javanexus.year2022.day5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.javanexus.InputReader;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SupplyStacksTest {

    private InputReader inputReader;
    private SupplyStacks supplyStacks;

    @BeforeEach
    void setUp() {
        inputReader = new InputReader();
        supplyStacks = new SupplyStacks();
    }

    @ParameterizedTest
    @CsvSource({
            "year2022/day5/input1.txt, CMZ",
            "year2022/day5/input2.txt, SHMSDGZVC"
    })
    public void test(String fileName, String expectedTopCrates) {
        assertThat(supplyStacks.rearrangeCrates(inputReader.getLinesStream(fileName)), is(expectedTopCrates));
    }
}
