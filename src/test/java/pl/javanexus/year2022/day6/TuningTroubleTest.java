package pl.javanexus.year2022.day6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.javanexus.InputReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TuningTroubleTest {

    private TuningTrouble tuningTrouble;

    @BeforeEach
    void setUp() {
        tuningTrouble = new TuningTrouble();
    }

    @ParameterizedTest
    @CsvSource({
            "mjqjpqmgbljsphdztnvjfqwrcgsmlb, 7",
            "bvwbjplbgvbhsrlpgdmjqwftvncz, 5",
            "nppdvjthqldpwncqszvftbrmjlhg, 6",
            "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg, 10",
            "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw, 11"
    })
    public void shouldFindStartOfPacketMarkerPosition(String buffer, int expectedPosition) {
        assertThat(tuningTrouble.findStartOfPacketPosition(buffer), is(expectedPosition));
    }

    @ParameterizedTest
    @CsvSource({
            "mjqjpqmgbljsphdztnvjfqwrcgsmlb, 19",
            "bvwbjplbgvbhsrlpgdmjqwftvncz, 23",
            "nppdvjthqldpwncqszvftbrmjlhg, 23",
            "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg, 29",
            "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw, 26"
    })
    public void shouldFindStartOfMessageMarkerPosition(String buffer, int expectedPosition) {
        assertThat(tuningTrouble.findStartOfMessagePosition(buffer), is(expectedPosition));
    }

    @Test
    void shouldFindStartOfPacketMarkerPositionInInput() {
        String buffer = new InputReader().readStringValue("year2022/day6/input1.txt");
        assertThat(tuningTrouble.findStartOfPacketPosition(buffer), is(1816));
    }

    @Test
    void shouldFindStartOfMessageMarkerPositionInInput() {
        String buffer = new InputReader().readStringValue("year2022/day6/input1.txt");
        assertThat(tuningTrouble.findStartOfMessagePosition(buffer), is(2625));
    }
}
