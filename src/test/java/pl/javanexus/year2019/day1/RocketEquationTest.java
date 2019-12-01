package pl.javanexus.year2019.day1;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RocketEquationTest {

    private RocketEquation rocketEquation;

    @Before
    public void setUp() {
        this.rocketEquation = new RocketEquation();
    }

    @Test
    public void testCalculateFuel() {
        assertEquals(2, rocketEquation.calculateFuel(12));
        assertEquals(2, rocketEquation.calculateFuel(14));
        assertEquals(654, rocketEquation.calculateFuel(1969));
        assertEquals(33583, rocketEquation.calculateFuel(100756));
    }

    @Test
    public void testCalculateTotalFuel() {
        assertEquals(2, rocketEquation.calculateTotalFuel(12));
        assertEquals(2, rocketEquation.calculateTotalFuel(14));
        assertEquals(966, rocketEquation.calculateTotalFuel(1969));
        assertEquals(50346, rocketEquation.calculateTotalFuel(100756));
    }

    @Test
    public void testCalculateModuleBaseFuel() throws IOException {
        List<Integer> moduleMass = new InputReader()
                .readIntegerValues("year2019/day1/input1.csv");
        int totalFuel = rocketEquation.calculateModuleBaseFuel(moduleMass);

        System.out.println(totalFuel);
    }

    @Test
    public void testCalculateModuleTotalFuel() throws IOException {
        List<Integer> moduleMass = new InputReader()
                .readIntegerValues("year2019/day1/input1.csv");
        int totalFuel = rocketEquation.calculateModuleTotalFuel(moduleMass);

        System.out.println(totalFuel);
    }
}
