package pl.javanexus.year2019.day12;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MoonsOfJupiterTest {

    public static final String[] INPUT_1 = {
            "<x=-1, y=0, z=2>",
            "<x=2, y=-10, z=-7>",
            "<x=4, y=-8, z=8>",
            "<x=3, y=5, z=-1>"
    };

    public static final String[] INPUT_2 = {
            "<x=5, y=-1, z=5>",
            "<x=0, y=-14, z=2>",
            "<x=16, y=4, z=0>",
            "<x=18, y=1, z=16>"
    };

    @Test
    public void testMoonsOfJupiter() {
        MoonsOfJupiter moonsOfJupiter = new MoonsOfJupiter();
        moonsOfJupiter.parseInitialPositions(INPUT_1);

        moonsOfJupiter.simulateMovementOverTime(1);
        verifyPosition(new long[][] {{2, -1, 1}, {3, -7, -4}, {1, -7, 5}, {2, 2, 0}}, moonsOfJupiter);

        moonsOfJupiter.simulateMovementOverTime(9);//After 10 steps
        verifyPosition(new long[][] {{2, 1, -3}, {1, -8, 0}, {3, -6, 1}, {2, 0, 4}}, moonsOfJupiter);

        assertEquals(179, moonsOfJupiter.getTotalEnergy());
    }

    @Test
    public void testGetTotalEnergy() {
        MoonsOfJupiter moonsOfJupiter = new MoonsOfJupiter();
        moonsOfJupiter.parseInitialPositions(INPUT_2);

        moonsOfJupiter.simulateMovementOverTime(1000);
        assertEquals(7928, moonsOfJupiter.getTotalEnergy());
    }

    @Test
    public void testReset() {
        MoonsOfJupiter moonsOfJupiter = new MoonsOfJupiter();
        moonsOfJupiter.parseInitialPositions(INPUT_1);

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

    private void verifyPosition(long[][] expectedPositions, MoonsOfJupiter moonsOfJupiter) {
        for (int moonId = 0; moonId < moonsOfJupiter.getNumberOfMoons(); moonId++) {
            assertArrayEquals(expectedPositions[moonId], moonsOfJupiter.getMoon(moonId).getPosition());
        }
    }

    @Test
    public void testFindPeriod() {
        long[][] input = {
                {2,-1,1},   //0
                {5,-3,-1},  //1
                {5,-6,-1},  //2
                {2,-8,0},   //3
                {-1,-9,2},  //4
                {-1,-7,3},  //5
                {2,-2,1},   //6 X
                {5,2,-2},   //7
                {5,3,-4},
                {2,1,-3},
                {-1,-4,1},
                {-1,-7,4},
                {2,-8,6},
                {5,-8,5},
                {5,-7,1},
                {2,-4,-4},
                {-1,1,-6},
                {-1,3,-5},
                {2,2,-1},
                {5,-2,5},
                {5,-7,8},
                {2,-9,8},
                {-1,-8,5},
                {-1,-6,-1},
                {2,-3,-5},
                {5,-1,-6},
                {5,0,-4},
                {2,0,1},
                {-1,-1,5},
                {-1,-3,6},
                {2,-6,4},
                {5,-8,1},
                {5,-9,-3},
                {2,-7,-4},
                {-1,-2,-2},
                {-1,2,1},
                {2,3,3},
                {5,1,2},
                {5,-4,0},
                {2,-7,-1},
                {-1,-8,-1},
                {-1,-8,1},
                {2,-7,2},
                {5,-4,2},
                {5,1,1},
                {2,3,-1},
                {-1,2,-1},
                {-1,-2,0},
                {2,-7,2},
                {5,-9,3},
                {5,-8,1},
                {2,-6,-2},
                {-1,-3,-4},
                {-1,-1,-3},
                {2,0,1},
                {5,0,4},
                {5,-1,6},
                {2,-3,5},
                {-1,-6,1},
                {-1,-8,-4},
                {2,-9,-6},
                {5,-7,-5},
                {5,-2,-1},
                {2,2,5},
                {-1,3,8},
                {-1,1,8},
                {2,-4,5},
                {5,-7,-1},
                {5,-8,-5},
                {2,-8,-6},
                {-1,-7,-4},
                {-1,-4,1},
                {2,1,5},
                {5,3,6},
                {5,2,4},
                {2,-2,1},
                {-1,-7,-3},
                {-1,-9,-4},
                {2,-8,-2},
                {5,-6,1},
                {5,-3,3},
                {2,-1,2},
                {-1,0,0},
                {-1,0,-1},
                {2,-1,-1},
                {5,-3,1},
                {5,-6,2},
                {2,-8,2},
                {-1,-9,1},
                {-1,-7,-1},
                {2,-2,-1},
                {5,2,0},
                {5,3,2},
                {2,1,3},
                {-1,-4,1},
                {-1,-7,-2},
                {2,-8,-4},
                {5,-8,-3},
                {5,-7,1},
                {2,-4,4},
        };

        int[] periodStart = {0, 0, 0};
        int[] periodLength = {0, 0, 0};
        int[] sequenceLength = {0, 0, 0};
        for (int t = 0; t < input.length; t++) {
            for (int coordinate = 0; coordinate < 3; coordinate++) {
                if (periodLength[coordinate] > 0) {
                    if (input[t][coordinate] == input[t - periodLength[coordinate]][coordinate]) {
//                        System.out.println("Matched next element of period at " + t + " (" + input[t][coordinate] + ")"
//                                + " [" + sequenceLength[coordinate] + "/" + periodLength[coordinate] + "]");
                        sequenceLength[coordinate]++;
                        if (sequenceLength[coordinate] == periodLength[coordinate]) {
                            System.out.println("Bingo! Found period for coordinate: " + coordinate + " of length: " + periodLength[coordinate]);
                        }
                    } else {
//                        System.out.println("Element " + input[t][coordinate] + " broke period sequence at " + t);
                        periodLength[coordinate] = 0;
                        sequenceLength[coordinate] = 0;
                    }
                } else if (input[t][coordinate] == input[periodStart[coordinate]][coordinate]) {
                    periodLength[coordinate] = t - periodStart[coordinate];
//                    System.out.println("Possible period found from: " + periodStart[coordinate] + " to: " + t
//                            + " - length: " + periodLength[coordinate]);
                }
            }
        }
    }
}
