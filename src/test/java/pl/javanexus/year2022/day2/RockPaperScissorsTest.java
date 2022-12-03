package pl.javanexus.year2022.day2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.javanexus.InputReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RockPaperScissorsTest {

    private RockPaperScissors rockPaperScissors;
    private InputReader inputReader;

    @BeforeEach
    void setUp() {
        rockPaperScissors = new RockPaperScissors();
        inputReader = new InputReader();
    }

    @ParameterizedTest
    @CsvSource({
            "year2022/day2/input1.txt, 15",
            "year2022/day2/input2.txt, 11767"
    })
    public void shouldCalculateScore(String fileName, int expectedScore) {
        assertThat(rockPaperScissors.calculateScore(inputReader.getLinesStream(fileName)), is(expectedScore));
    }

    @ParameterizedTest
    @CsvSource({
            "year2022/day2/input1.txt, 12",
            "year2022/day2/input2.txt, 13886"
    })
    public void shouldSelectPlayerSymbol(String fileName, int expectedResult) {
        assertThat(rockPaperScissors.calculateScoreWithSelectedSymbol(inputReader.getLinesStream(fileName)),
                is(expectedResult));
    }
}
