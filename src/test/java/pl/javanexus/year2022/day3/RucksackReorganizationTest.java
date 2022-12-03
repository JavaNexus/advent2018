package pl.javanexus.year2022.day3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.javanexus.InputReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RucksackReorganizationTest {

    private InputReader inputReader;
    private RucksackReorganization rucksackReorganization;

    @BeforeEach
    void setUp() {
        inputReader = new InputReader();
        rucksackReorganization = new RucksackReorganization();
    }

    @ParameterizedTest
    @CsvSource({
            "year2022/day3/input1.txt, 157",
            "year2022/day3/input2.txt, 7727"
    })
    public void shouldCountDuplicateItemsPriorities(String fileName, int expectedScore) {
        assertThat(
                rucksackReorganization.getSumOfDuplicateItemTypesPriorites(inputReader.getLinesStream(fileName)),
                is(expectedScore));
    }
}
