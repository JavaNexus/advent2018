package pl.javanexus.day6;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VectorTest {

    public static final double EXPECTED_PRECISSION = 0.0001;

    @Test
    public void testGetCosin() {
        Point pA = new Point('A', 2, 6);
        Point pB = new Point('B', 5, 4);
        Point pC = new Point('C', 7, 1);

        double expectedCosin = 12.0 / 13.0;
        assertEquals(expectedCosin, new Vector(pA, pB).getCosin(new Vector(pB, pC)), EXPECTED_PRECISSION);
    }
}
