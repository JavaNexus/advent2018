package pl.javanexus.year2019.day12;

import org.junit.Test;
import pl.javanexus.year2019.day10.MathUtil;

import java.math.BigInteger;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MoonsOfJupiterTest {

    public static final String[] INPUT_SMALL = {
            "<x=-1, y=0, z=2>",
            "<x=2, y=-10, z=-7>",
            "<x=4, y=-8, z=8>",
            "<x=3, y=5, z=-1>"
    };

    public static final String[] INPUT_BIG = {
            "<x=-8, y=-10, z=0>",
            "<x=5, y=5, z=10>",
            "<x=2, y=-7, z=3>",
            "<x=9, y=-8, z=-3>"
    };

    public static final String[] INPUT_TEST = {
            "<x=5, y=-1, z=5>",
            "<x=0, y=-14, z=2>",
            "<x=16, y=4, z=0>",
            "<x=18, y=1, z=16>"
    };

    @Test
    public void testMoonsOfJupiter() {
        MoonsOfJupiter moonsOfJupiter = new MoonsOfJupiter();
        moonsOfJupiter.parseInitialPositions(INPUT_SMALL);

        moonsOfJupiter.simulateMovementOverTime(1);
        verifyPosition(new long[][] {{2, -1, 1}, {3, -7, -4}, {1, -7, 5}, {2, 2, 0}}, moonsOfJupiter);

        moonsOfJupiter.simulateMovementOverTime(9);//After 10 steps
        verifyPosition(new long[][] {{2, 1, -3}, {1, -8, 0}, {3, -6, 1}, {2, 0, 4}}, moonsOfJupiter);

        assertEquals(179, moonsOfJupiter.getTotalEnergy());
    }

    @Test
    public void testGetTotalEnergy() {
        MoonsOfJupiter moonsOfJupiter = new MoonsOfJupiter();
        moonsOfJupiter.parseInitialPositions(INPUT_TEST);

        moonsOfJupiter.simulateMovementOverTime(1000);
        assertEquals(7928, moonsOfJupiter.getTotalEnergy());
    }

    @Test
    public void testReset() {
        MoonsOfJupiter moonsOfJupiter = new MoonsOfJupiter();
        moonsOfJupiter.parseInitialPositions(INPUT_SMALL);

        System.out.println("Before:");
        moonsOfJupiter.printMoons();

        long[][] originalPosition = moonsOfJupiter.getPositions();
        long[][] originalVelocity = moonsOfJupiter.getVelocity();

        long totalKineticEnergy = moonsOfJupiter.getTotalKineticEnergy();
        long totalPotentialEnergy = moonsOfJupiter.getTotalPotentialEnergy();
        long totalEnergy = moonsOfJupiter.getTotalEnergy();
        System.out.println("Total: " + totalEnergy + ", K: " + totalKineticEnergy + ", P: " + totalPotentialEnergy);

//        moonsOfJupiter.simulateMovementOverTime(2772);
//        moonsOfJupiter.printMoons();

        long numberOfStepsToRestoreInitialState =
                moonsOfJupiter.getNumberOfStepsToRestoreInitialState(totalEnergy, originalPosition, originalVelocity);

        System.out.println("After:");
        moonsOfJupiter.printMoons();
        System.out.println("K: " + moonsOfJupiter.getTotalKineticEnergy());
        System.out.println("P: " + moonsOfJupiter.getTotalPotentialEnergy());

        assertEquals(2772, numberOfStepsToRestoreInitialState);
    }

    @Test
    public void testFindPeriods() {
        MoonsOfJupiter moonsOfJupiter = new MoonsOfJupiter();
        moonsOfJupiter.parseInitialPositions(INPUT_TEST);

        moonsOfJupiter.findPeriods();
        BigInteger commonPeriod = MathUtil.findCommonPeriod(moonsOfJupiter.getPeriods());
        System.out.println(commonPeriod);
        assertEquals(new BigInteger("518311327635164"), commonPeriod);

        moonsOfJupiter.printMoons();
    }



    private void verifyPosition(long[][] expectedPositions, MoonsOfJupiter moonsOfJupiter) {
        for (int moonId = 0; moonId < moonsOfJupiter.getNumberOfMoons(); moonId++) {
            assertArrayEquals(expectedPositions[moonId], moonsOfJupiter.getMoon(moonId).getPosition());
        }
    }
}
