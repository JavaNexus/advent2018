package pl.javanexus.year2022.day4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.javanexus.InputReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CampCleanupTest {

    private InputReader inputReader;
    private CampCleanup campCleanup;

    @BeforeEach
    void setUp() {
        inputReader = new InputReader();
        campCleanup = new CampCleanup();
    }

    @ParameterizedTest
    @CsvSource({
            "year2022/day4/input1.txt, 2",
            "year2022/day4/input2.txt, 471"
    })
    public void shouldCountFullyOverlappingSections(String fileName, int expectedResult) {
        assertThat(
                campCleanup.countFullyOverlappingSections(inputReader.getLinesStream(fileName)),
                is(expectedResult));
    }

    @ParameterizedTest
    @CsvSource({
            "year2022/day4/input1.txt, 4",
            "year2022/day4/input2.txt, 888"
    })
    public void shouldCountOverlappingSections(String fileName, int expectedResult) {
        assertThat(
                campCleanup.countOverlappingSections(inputReader.getLinesStream(fileName)),
                is(expectedResult));
    }

    @ParameterizedTest
    @CsvSource({
            "2, 6, 2, 6, true",

            "2, 6, 2, 5, true",
            "2, 6, 3, 6, true",
            "2, 6, 3, 5, true",

            "5, 7, 5, 9, false",
            "5, 7, 1, 7, false",
            "5, 7, 1, 9, false",

            "3, 5, 6, 9, false",
            "3, 5, 1, 9, false",

            "3, 9, 6, 9, true",
            "6, 9, 3, 9, false",

            "3, 9, 3, 5, true",
            "3, 5, 3, 9, false",
    })
    void shouldFindFullyOverlappingSections(int leftFrom, int leftTo, int rightFrom, int rightTo,
                                            boolean expectedResult) {
        assertThat(
                new CampCleanup.Assignment(leftFrom, leftTo)
                        .isFullyOverlapping(new CampCleanup.Assignment(rightFrom, rightTo)),
                is(expectedResult));
    }

    @ParameterizedTest
    @CsvSource({
            "5, 7, 7, 9, true",
            "2, 6, 2, 6, true",

            "2, 6, 2, 5, true",
            "2, 6, 3, 6, true",
            "2, 6, 3, 5, true",

            "5, 7, 5, 9, true",
            "5, 7, 1, 7, true",
            "5, 7, 1, 9, true",

            "3, 5, 6, 9, false",
            "3, 5, 1, 2, false",
            "3, 5, 1, 9, true",

            "3, 9, 6, 9, true",
            "6, 9, 3, 9, true",

            "3, 9, 3, 5, true",
            "3, 5, 3, 9, true",
    })
    void shouldFindOverlappingSections(int leftFrom, int leftTo, int rightFrom, int rightTo,
                                            boolean expectedResult) {
        assertThat(
                new CampCleanup.Assignment(leftFrom, leftTo)
                        .isOverlapping(new CampCleanup.Assignment(rightFrom, rightTo)),
                is(expectedResult));
    }
}
