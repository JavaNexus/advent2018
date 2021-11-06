package pl.javanexus.year2020.day3;

public class TobogganTrajectory {

    public int countTreesAlongMultipleTrajectories(SlopeMap slopeMap, int[][] trajectories) {
        int multipliedNumberOfTrees = 1;
        for (int[] trajectory : trajectories) {
            multipliedNumberOfTrees *= countTreesAlongTrajectory(slopeMap, trajectory[0], trajectory[1]);
        }

        return multipliedNumberOfTrees;
    }

    public int countTreesAlongTrajectory(SlopeMap slopeMap, int dX, int dY) {
        int trees = 0, x = dX, y = dY;

        while (y < slopeMap.getHeight()) {
            if (slopeMap.getField(x, y) == SlopeMap.TREE) {
                trees++;
            }
            x += dX;
            y += dY;
        }

        return trees;
    }
}
