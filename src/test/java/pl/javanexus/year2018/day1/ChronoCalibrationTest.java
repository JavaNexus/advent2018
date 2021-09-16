package pl.javanexus.year2018.day1;

import org.junit.Test;
import pl.javanexus.InputReader;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ChronoCalibrationTest {

    @Test
    public void testFirstDuplicate() throws Exception {
        validateFirstDuplicate(new int[]{+1, -1}, 0);
        validateFirstDuplicate(new int[]{+3, +3, +4, -2, -4}, 10);
        validateFirstDuplicate(new int[]{-6, +3, +8, +5, -6}, 5);
        validateFirstDuplicate(new int[]{+7, +7, -2, -7, -4}, 14);
    }

    @Test
    public void testFirstDuplicateFromFile() throws Exception {
        List<Integer> values = new InputReader().readIntegerValues("day1_calibration.input");
        ChronoCalibration calibration = new ChronoCalibration(values);
        System.out.println(calibration.getFirstDuplicate());
    }

    private void validateFirstDuplicate(int[] values, int expected) {
        ChronoCalibration calibration = new ChronoCalibration(values);
        assertEquals(expected, calibration.getFirstDuplicate());
    }
}
