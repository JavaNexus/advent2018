package pl.javanexus.year2019.day12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoonsOfJupiter {

    //<x=-1, y=0, z=2>
    private static final Pattern inputPattern = Pattern.compile("<x=([-0-9]+), y=([-0-9]+), z=([-0-9]+)>");

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

    public void updatePosition(int time) {
        for (int t = 0; t < time; t++) {
            for (Moon moon : moons) {
                moon.updateVelocity(moons);
            }
            for (Moon moon : moons) {
                moon.updatePosition();
            }
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

    public static class Moon {

        private final long[] position = {0, 0, 0};
        private final long[] velocity = {0, 0, 0};

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
                    ", v=" + Arrays.toString(velocity) +
                    '}';
        }
    }
}
