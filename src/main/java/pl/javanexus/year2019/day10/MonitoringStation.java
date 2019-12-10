package pl.javanexus.year2019.day10;

import lombok.Data;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

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

        int gcd = Math.abs(MathUtil.findGreatestCommonDivisor(dX, dY));

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

    private void printField() {
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                System.out.print(field[y][x]);
            }
            System.out.print("\n");
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

        @Override
        public String toString() {
            return "[" + x + ", " + y + "](" + numberOfAsteroidsInDirectLineOfSight + ")";
        }
    }

}
