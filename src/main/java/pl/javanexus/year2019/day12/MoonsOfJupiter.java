package pl.javanexus.year2019.day12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoonsOfJupiter {

    //<x=-1, y=0, z=2>
    private static final Pattern inputPattern = Pattern.compile("<x=([-0-9]+), y=([-0-9]+), z=([-0-9]+)>");

    public static final int HISTORY_LENGTH = 10000; //2048;

    enum Coordinate {
        X(0),
        Y(1),
        Z(2);

        private final int index;

        Coordinate(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    private final List<Moon> moons = new ArrayList<>(4);

    public void parseInitialPositions(String[] positions) {
        for (String position : positions) {
            Matcher matcher = inputPattern.matcher(position);
            if (matcher.find()) {
                Moon moon = new Moon(
                        getCoordinate(matcher, Coordinate.X),
                        getCoordinate(matcher, Coordinate.Y),
                        getCoordinate(matcher, Coordinate.Z));
                moons.add(moon);
            }
        }
    }

    public void simulateMovementOverTime(int time) {
        for (int t = 0; t < time; t++) {
            updateState();
        }
    }

    private void updateState() {
        for (Moon moon : moons) {
            moon.updateVelocity(moons);
        }
        for (Moon moon : moons) {
            moon.updatePosition();
        }
    }

    private long getCoordinate(Matcher matcher, Coordinate coordinate) {
        return Long.parseLong(matcher.group(coordinate.getIndex() + 1));
    }

    public Moon getMoon(int i) {
        return moons.get(i);
    }

    public int getNumberOfMoons() {
        return moons.size();
    }

    public long getTotalEnergy() {
        return moons.stream().mapToLong(Moon::getTotalEnergy).sum();
    }

    public long getTotalKineticEnergy() {
        return moons.stream().mapToLong(Moon::getKineticEnergy).sum();
    }

    public long getTotalPotentialEnergy() {
        return moons.stream().mapToLong(Moon::getPotentialEnergy).sum();
    }

    public long[][] getPositions() {
        long[][] positions = new long[moons.size()][];
        for (int i = 0; i < moons.size(); i++) {
            long[] moonPosition = moons.get(i).getPosition();
            positions[i] = new long[moonPosition.length];
            System.arraycopy(moonPosition, 0, positions[i], 0, positions[i].length);
        }

        return positions;
    }

    public long[][] getVelocity() {
        long[][] velocity = new long[moons.size()][];
        for (int i = 0; i < moons.size(); i++) {
            long[] moonVelocity = moons.get(i).getVelocity();
            velocity[i] = new long[moonVelocity.length];
            System.arraycopy(moonVelocity, 0, velocity[i], 0, velocity[i].length);
        }

        return velocity;
    }

    public long getNumberOfStepsToRestoreInitialState(long totalEnergy, long[][] originalPosition, long[][] originalVelocity) {
        int t = 0;
        do {
            updateHistory(t);
            updateState();
            checkForPeriod(t);
            t++;

//            System.out.println(t + "," + getTotalKineticEnergy() + "," + getTotalPotentialEnergy() + "," + getTotalEnergy());
//            System.out.println(Arrays.toString(moons.get(0).getPosition()));
        } while (getTotalEnergy() != totalEnergy || !isExpectedState(originalPosition, originalVelocity));

        return t;
    }

    private void updateHistory(int t) {
        for (Moon moon : moons) {
            moon.updateHistory(t);
        }
    }

    public void checkForPeriod(int t) {
        for (Moon moon : moons) {
            moon.checkForPeriod(t);
        }
    }

    public void printMoons() {
        for (Moon moon : moons) {
            System.out.println(moon);
        }
    }

    private boolean isExpectedState(long[][] originalPosition, long[][] originalVelocity) {
        boolean isEqual = true;
        for (int i = 0; i < moons.size(); i++) {
            Moon moon = moons.get(i);
            isEqual &= isEqual(moon.getPosition(), originalPosition[i]) && isEqual(moon.getVelocity(), originalVelocity[i]);
        }

        return isEqual;
    }

    private boolean isEqual(long[] left, long[] right) {
        // TODO: 12.12.2019 check array length first
        boolean isEqual = true;
        for (int i = 0; i < left.length; i++) {
            isEqual &= left[i] == right[i];
        }

        return isEqual;
    }

    public static class Moon {

        private final long[] position = {0, 0, 0};
        private final long[] velocity = {0, 0, 0};

        private final int[] positionPeriod = {0, 0, 0};
        private final int[] periodStart = {0, 0, 0};
        private final int[] periodLength = {0, 0, 0};
        private final int[] sequenceLength = {0, 0, 0};

        private final long[][] history = new long[HISTORY_LENGTH][];

        public Moon(long x, long y, long z) {
            position[Coordinate.X.getIndex()] = x;
            position[Coordinate.Y.getIndex()] = y;
            position[Coordinate.Z.getIndex()] = z;
        }

        public void updateVelocity(List<Moon> moons) {
            for (Moon moon : moons) {
                for (int i = 0; i < position.length; i++) {
                    if (position[i] < moon.getPosition(i)) {
                        velocity[i]++;
                    } else if (position[i] > moon.getPosition(i)) {
                        velocity[i]--;
                    }
                }
            }
        }

        public void updatePosition() {
            for (int i = 0; i < position.length; i++) {
                position[i] += velocity[i];
            }
        }

        private long getPosition(int coordinate) {
            return position[coordinate];
        }

        public long[] getPosition() {
            return position;
        }

        public long[] getVelocity() {
            return velocity;
        }

        public long getPotentialEnergy() {
            return Arrays.stream(position).map(Math::abs).sum();
        }

        public long getKineticEnergy() {
            return Arrays.stream(velocity).map(Math::abs).sum();
        }

        public long getTotalEnergy() {
            return getKineticEnergy() * getPotentialEnergy();
        }

        @Override
        public String toString() {
            return "Moon{" +
                    "p=" + Arrays.toString(position) +
                    ", p_period=" + Arrays.toString(positionPeriod) +
                    ", v=" + Arrays.toString(velocity) +
                    ", E_K=" + getKineticEnergy() +
                    ", E_P=" + getPotentialEnergy() +
                    '}';
        }

        public void updateHistory(int t) {
            if (t < history.length) {
                history[t] = new long[position.length];
                System.arraycopy(position, 0, history[t], 0, position.length);
            }
        }

        public void checkForPeriod(int t) {
            for (int coordinate = 0; coordinate < 3; coordinate++) {
                if (positionPeriod[coordinate] > 0) {
                    continue;
                }

                if (periodLength[coordinate] > 0) {
                    if (history[t][coordinate] == history[t - periodLength[coordinate]][coordinate]) {
//                        System.out.println("Matched next element of period at " + t + " (" + history[t][coordinate] + ")"
//                                + " [" + sequenceLength[coordinate] + "/" + periodLength[coordinate] + "]");
                        sequenceLength[coordinate]++;
                        if (sequenceLength[coordinate] == periodLength[coordinate]) {
                            positionPeriod[coordinate] = periodLength[coordinate];
//                            System.out.println("Bingo! Found period for coordinate: " + coordinate + " of length: " + periodLength[coordinate]);
                        }
                    } else {
//                        System.out.println("Element " + history[t][coordinate] + " broke period sequence at " + t);
                        periodLength[coordinate] = 0;
                        sequenceLength[coordinate] = 0;
                    }
                }
                if (periodLength[coordinate] == 0 && history[t][coordinate] == history[periodStart[coordinate]][coordinate]) {
                    periodLength[coordinate] = t - periodStart[coordinate];
//                    System.out.println("Possible period found from: " + periodStart[coordinate] + " to: " + t
//                            + " - length: " + periodLength[coordinate]);
                }
            }
        }
    }
}
