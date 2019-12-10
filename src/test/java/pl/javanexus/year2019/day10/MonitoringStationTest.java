package pl.javanexus.year2019.day10;

import org.junit.Before;
import org.junit.Test;
import pl.javanexus.InputReader;

import java.io.IOException;

import static org.junit.Assert.*;

public class MonitoringStationTest {

    private InputReader inputReader;

    @Before
    public void setUp() throws Exception {
        this.inputReader = new InputReader();
    }

    @Test
    public void testSelectLocation() throws IOException {
        testSelectLocation("year2019/day10/input_tiny.csv", 3, 4, 8);
        testSelectLocation("year2019/day10/input_small_001.csv", 5, 8, 33);
        testSelectLocation("year2019/day10/input_small_002.csv", 1, 2, 35);
        testSelectLocation("year2019/day10/input_small_003.csv", 6, 3, 41);
        testSelectLocation("year2019/day10/input_big_001.csv", 11, 13, 210);
        testSelectLocation("year2019/day10/input1.csv", 8, 16, 214);
    }

    private void testSelectLocation(String filePath, int expectedX, int expectedY,
                                    int expectedNumberOfAsteroidsInLineOfSight) throws IOException {
        final MonitoringStation station = getMonitoringStation(filePath);
//        assertEquals(10, station.getAsteroids().size());

        station.countAsteroidsInLineOfSight();
        MonitoringStation.Asteroid asteroid = station.findAsteroidWithMostLinesOfSightToOtherAsteroids();
        assertEquals(expectedX, asteroid.getX());
        assertEquals(expectedY, asteroid.getY());
        assertEquals(expectedNumberOfAsteroidsInLineOfSight, asteroid.getNumberOfAsteroidsInDirectLineOfSight());
    }

    /**
     *
     * @throws IOException
     */
    @Test
    public void testHasLineOfSight() throws IOException {
        final MonitoringStation station = getMonitoringStation("year2019/day10/input_tiny.csv");
        assertEquals(10, station.getAsteroids().size());

        MonitoringStation.Asteroid from, to;

        from = station.getAsteroid(1, 0);
        to = station.getAsteroid(4, 3);
        assertFalse(station.hasLineOfSight(from, to));

        to = station.getAsteroid(3, 4);
        assertFalse(station.hasLineOfSight(from, to));

        to = station.getAsteroid(4, 2);
        assertTrue(station.hasLineOfSight(from, to));

        //...

        from = station.getAsteroid(4, 0);
        to = station.getAsteroid(4, 2);
        assertTrue(station.hasLineOfSight(from, to));

        from = station.getAsteroid(4, 0);
        to = station.getAsteroid(4, 3);
        assertFalse(station.hasLineOfSight(from, to));

        //...

        from = station.getAsteroid(4, 2);
        to = station.getAsteroid(0, 2);
        assertFalse(station.hasLineOfSight(from, to));
    }

    private MonitoringStation getMonitoringStation(String filePath) throws IOException {
        return new MonitoringStation(inputReader.readStringValues(filePath));
    }

    @Test
    public void testGreatestCommonDivisor() {
        assertEquals(51, MathUtil.findGreatestCommonDivisor(1989, 867));
    }
}
