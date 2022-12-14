package pl.javanexus.year2022;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.javanexus.InputReader;
import pl.javanexus.year2022.day7.NoSpaceLeftOnDevice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class NoSpaceLeftOnDeviceTest {

    private InputReader inputReader;
    private NoSpaceLeftOnDevice device;

    @BeforeEach
    void setUp() {
        inputReader = new InputReader();
        device = new NoSpaceLeftOnDevice();
    }

    @ParameterizedTest
    @CsvSource({
            "year2022/day7/input1.txt, 100000, 95437",
            "year2022/day7/input2.txt, 100000, 1182909"
    })
    void shouldSumSizesOfDirectories(String fileName, int threshold, int expectedResult) {
        assertThat(device.sumDirectoriesSizes(inputReader.getLinesStream(fileName), threshold), is(expectedResult));
    }

    @ParameterizedTest
    @CsvSource({
            "year2022/day7/input1.txt, 70000000, 30000000, 24933642",
            "year2022/day7/input2.txt, 70000000, 30000000, 2832508"
    })
    void shouldFindSizeOfDirectoryToDelete(String fileName, int diskSize, int updateSize, int expectedResult) {
        assertThat(device.findSizeOfDirectoryToDelete(inputReader.getLinesStream(fileName), diskSize, updateSize),
                is(expectedResult));
    }
}
