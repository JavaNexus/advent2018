package pl.javanexus.year2022.day2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RockPaperScissorsTest {

    private RockPaperScissors rockPaperScissors;

    @BeforeEach
    void setUp() {
        rockPaperScissors = new RockPaperScissors();
    }

    @ParameterizedTest
    @CsvSource({
            "year2022/day2/input1.txt, 15",
            "year2022/day2/input2.txt, 11767"
    })
    public void shouldCalculateScore(String fileName, int expectedScore) {
        assertThat(rockPaperScissors.calculateScore(getStream(fileName)), is(expectedScore));
    }

    @ParameterizedTest
    @CsvSource({
            "year2022/day2/input1.txt, 12",
            "year2022/day2/input2.txt, 13886"
    })
    public void shouldSelectPlayerSymbol(String fileName, int expectedResult) {
        assertThat(rockPaperScissors.calculateScoreWithSelectedSymbol(getStream(fileName)), is(expectedResult));
    }

    private Stream<String> getStream(String fileName) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("Couldn't fiond file: " + fileName);
        }
        return new BufferedReader(new InputStreamReader(inputStream)).lines();
    }
}
