package pl.javanexus.year2022.day1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CalorieCountingTest {

    private CalorieCounting calorieCounting;

    @BeforeEach
    public void setUp() throws Exception {
        calorieCounting = new CalorieCounting();
    }

    @ParameterizedTest
    @CsvSource({
            "year2022/day1/input1.txt, 24000",
            "year2022/day1/input2.txt, 72240"
    })
    void shouldGetMostCalories(String fileName, long expectedCalorieCount) throws IOException {
        long calories = calorieCounting.getMostCaloriesFromSingleElf(getResourceAsStream(fileName));
        assertThat(calories, is(expectedCalorieCount));
    }

    @ParameterizedTest
    @CsvSource({
            "year2022/day1/input1.txt, 45000",
            "year2022/day1/input2.txt, 210957"
    })
    void shouldGetMostCaloriesFromThreeElves(String fileName, long expectedCalorieCount) throws IOException {
        long calories = calorieCounting.getMostCaloriesFromThreeElves(getResourceAsStream(fileName));
        assertThat(calories, is(expectedCalorieCount));
    }

    private InputStream getResourceAsStream(String fileName) {
        return getClass().getClassLoader().getResourceAsStream(fileName);
    }
}
