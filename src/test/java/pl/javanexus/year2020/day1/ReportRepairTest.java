package pl.javanexus.year2020.day1;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReportRepairTest {

    private InputReader inputReader;

    @Before
    public void setUp() throws Exception {
        inputReader = new InputReader();
    }

    @Test
    public void shouldFindSum() {
        final List<Integer> numbers = List.of(1721, 979, 366, 299, 675, 1456);

        ReportRepair reportRepair = new ReportRepair();
        assertEquals(514579, reportRepair.findTwoValues(numbers, 2020));
    }

    @Test
    public void shouldFindSumInFile() throws IOException {
        ReportRepair reportRepair = new ReportRepair();
        int result = reportRepair.findTwoValues(getNumbers("year2020/day1/input1.csv"), 2020);
        System.out.println(result);
    }

    private List<Integer> getNumbers(String fileName) throws IOException {
        return inputReader.readIntegerValues(fileName);
    }
}
