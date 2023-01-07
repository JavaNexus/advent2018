package pl.javanexus.year2022.day8;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.javanexus.InputReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TreetopTreeHouseTest {

    private InputReader inputReader;

    @BeforeEach
    void setUp() {
        inputReader = new InputReader();
    }

    @ParameterizedTest
    @CsvSource({
            "year2022/day8/input1.txt, 21",
            "year2022/day8/input2.txt, 1533",
    })
    public void shouldCountVisibleTrees(String fileName, int expectedResult) {
        assertThat(new TreetopTreeHouse(inputReader.getIntMatrix(fileName)).countVisibleTrees(), is(expectedResult));
    }
}
