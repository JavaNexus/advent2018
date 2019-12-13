package pl.javanexus.year2019.day10;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MonitoringStation {

    public static final String SYMBOL_ASTEROID = "#";
    public static final String SYMBOL_EMPTY = ".";

    @Getter
    private final int width;
    @Getter
    private final int height;

    @Getter
    private final List<Asteroid> asteroids;
    private final Asteroid[][] field;
    private final List<Quadrant> quadrants = new ArrayList<>(4);

    public MonitoringStation(List<String> input) {
        this.field = new Asteroid[input.size()][];
        this.asteroids = new LinkedList<>();

        parseInput(input);

        this.width = field[0].length;
        this.height = field.length;
    }

    private void parseInput(List<String> lines) {
        int y = 0;
        for (String line : lines) {
            String[] symbols = line.split("");
            field[y] = new Asteroid[symbols.length];
            for (int x = 0; x < field[y].length; x++) {
                if (symbols[x].startsWith(SYMBOL_ASTEROID)) {
                    Asteroid asteroid = new Asteroid(x, y);
                    field[y][x] = asteroid;
                    asteroids.add(asteroid);
                }
            }
            y++;
        }
    }

    public void countAsteroidsInLineOfSight() {
        for (Asteroid from : asteroids) {
            from.setNumberOfAsteroidsInDirectLineOfSight(0);
            for (Asteroid to : asteroids) {
                if (!from.equals(to) && hasLineOfSight(from, to)) {
                    from.addAsteroidInLineOfSight();
                }
            }
        }
    }

    public Asteroid findAsteroidWithMostLinesOfSightToOtherAsteroids() {
        Asteroid selectedAsteroid = null;
        for (Asteroid asteroid : asteroids) {
            if (selectedAsteroid == null || asteroid.getNumberOfAsteroidsInDirectLineOfSight() > selectedAsteroid.getNumberOfAsteroidsInDirectLineOfSight()) {
                selectedAsteroid = asteroid;
            }
        }

        return selectedAsteroid;
    }

    public boolean hasLineOfSight(Asteroid from, Asteroid to) {
        int dX = to.getX() - from.getX();
        int dY = to.getY() - from.getY();

        long gcd = Math.abs(MathUtil.findGreatestCommonDivisor(dX, dY));

        dX /= gcd;
        dY /= gcd;

//        System.out.println(from + " -> " + to + " = " + dX + " / " + dY + " -> " + gcd);

        boolean hasLineOfSight = true;
        for (int x = from.getX() + dX, y = from.getY() + dY; x != to.getX() || y != to.getY(); x += dX, y += dY) {
//            System.out.println("[" + x + ", " + y + "] -> " + (field[y][x]));
            hasLineOfSight &= (field[y][x] == null);
        }

//        System.out.println("line of sight from: " + from + " to: " + to + " -> " + hasLineOfSight);

        return hasLineOfSight;
    }

    public Asteroid getAsteroid(int x, int y) {
        return field[y][x];
    }

    public void populateQuadrants(Asteroid laserMoon) {
        this.quadrants.add(new Quadrant(laserMoon, getTargetingOrder(laserMoon, asteroid -> asteroid.getX() >= laserMoon.getX() && asteroid.getY() < laserMoon.getY())));
        this.quadrants.add(new Quadrant(laserMoon, getTargetingOrder(laserMoon, asteroid -> asteroid.getX() > laserMoon.getX() && asteroid.getY() == laserMoon.getY())));
        this.quadrants.add(new Quadrant(laserMoon, getTargetingOrder(laserMoon, asteroid -> asteroid.getX() >= laserMoon.getX() && asteroid.getY() > laserMoon.getY())));
        this.quadrants.add(new Quadrant(laserMoon, getTargetingOrder(laserMoon, asteroid -> asteroid.getX() < laserMoon.getX() && asteroid.getY() > laserMoon.getY())));
        this.quadrants.add(new Quadrant(laserMoon, getTargetingOrder(laserMoon, asteroid -> asteroid.getX() < laserMoon.getX() && asteroid.getY() == laserMoon.getY())));
        this.quadrants.add(new Quadrant(laserMoon, getTargetingOrder(laserMoon, asteroid -> asteroid.getX() < laserMoon.getX() && asteroid.getY() < laserMoon.getY())));
    }

    public List<Asteroid> getTargetingOrder(Asteroid laserMoon, Predicate<Asteroid> isInQuadrant) {
        return getAsteroids().stream()
                .filter(isInQuadrant)
                .sorted((o1, o2) -> {
                    int tanDiff = (int) Math.signum(o2.getTan(laserMoon) - o1.getTan(laserMoon));
                    return tanDiff != 0 ? tanDiff : laserMoon.getManhattanDistance(o1) - laserMoon.getManhattanDistance(o2);
                })
                .collect(Collectors.toList());
    }

    public List<Asteroid> fireLaser(Asteroid laserMoon) {
        final List<Asteroid> destroyedTargets = new LinkedList<>();

        int quadrantId = 0;
        while (hasRemainingTargets()) {
            Quadrant quadrant = quadrants.get(quadrantId);
            if (quadrant.hasRemainingTargets()) {
                destroyedTargets.addAll(quadrant.fireLaser(laserMoon));
            }
            quadrantId = (quadrantId + 1) % quadrants.size();
        }

        return destroyedTargets;
    }

    private boolean hasRemainingTargets() {
        return quadrants.stream().filter(Quadrant::hasRemainingTargets).count() > 0;
    }

    private void printField() {
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                System.out.print(field[y][x]);
            }
            System.out.print("\n");
        }
    }

    public static class Quadrant {

        public static final int NEGATIVE_INFINITY = -1111111111;
        private final Asteroid laserMoon;
        private final Queue<Asteroid> remainingTargets = new LinkedList<>();

        public Quadrant(Asteroid laserMoon, List<Asteroid> asteroids) {
            this.laserMoon = laserMoon;
            this.remainingTargets.addAll(asteroids);
        }

        public Queue<Asteroid> getTargets() {
            return remainingTargets;
        }

        public boolean hasRemainingTargets() {
            return !remainingTargets.isEmpty();
        }

        public List<Asteroid> fireLaser(Asteroid laserMoon) {
            List<Asteroid> destroyedTargets = new LinkedList<>();
            Queue<Asteroid> obscuredTargets = new LinkedList<>();

            double previousTan = NEGATIVE_INFINITY;
            while (!remainingTargets.isEmpty()) {
                Asteroid target = remainingTargets.poll();
                double tan = target.getTan(laserMoon);
                if (tan != previousTan) {
//                    System.out.println(" >: Destroyed asteroid: " + target);
                    destroyedTargets.add(target);
                    previousTan = tan;
                } else {
                    obscuredTargets.offer(target);
                }
            }
            remainingTargets.addAll(obscuredTargets);

            return destroyedTargets;
        }
    }

    @Data
    public static class Asteroid {

        private final int x;
        private final int y;
        private int numberOfAsteroidsInDirectLineOfSight;

        public void addAsteroidInLineOfSight() {
            this.numberOfAsteroidsInDirectLineOfSight++;
        }

        public double getTan(Asteroid asteroid) {
            return (double)(x - asteroid.getX()) / (double)(y - asteroid.getY());
        }

        public int getManhattanDistance(Asteroid asteroid) {
            return Math.abs(x - asteroid.getX()) + Math.abs(y - asteroid.getY());
        }

        @Override
        public String toString() {
            return "[" + x + ", " + y + "](" + numberOfAsteroidsInDirectLineOfSight + ")";
        }
    }

}
