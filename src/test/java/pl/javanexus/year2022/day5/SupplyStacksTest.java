package pl.javanexus.year2022.day5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.javanexus.InputReader;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SupplyStacksTest {

    private InputReader inputReader;

    @BeforeEach
    void setUp() {
        inputReader = new InputReader();
    }

    @ParameterizedTest
    @CsvSource({
            "year2022/day5/input1.txt, CMZ",
            "year2022/day5/input2.txt, SHMSDGZVC"
    })
    public void shouldRearrangeCratesOneAtATime(String fileName, String expectedTopCrates) {
        SupplyStacks supplyStacks = new SupplyStacks(SupplyStacks.CraneType.CM_9000);
        assertThat(supplyStacks.rearrangeCrates(inputReader.getLinesStream(fileName)), is(expectedTopCrates));
    }

    @ParameterizedTest
    @CsvSource({
            "year2022/day5/input1.txt, MCD",
            "year2022/day5/input2.txt, VRZGHDFBQ"
    })
    public void shouldRearrangeCratesMultipleAtATime(String fileName, String expectedTopCrates) {
        SupplyStacks supplyStacks = new SupplyStacks(SupplyStacks.CraneType.CM_9001);
        assertThat(supplyStacks.rearrangeCrates(inputReader.getLinesStream(fileName)), is(expectedTopCrates));
    }
}
