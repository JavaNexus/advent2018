package pl.javanexus.grid;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Grid<P extends Point> {

    private final P[][] points;
    private final int width;
    private final int height;

    public Grid(P[][] points) {
        this.points = points;
        this.width = Arrays.stream(points).mapToInt(row -> row.length).max().getAsInt();
        this.height = points.length;
    }

    private Set<P> getAdjacentPoints(P centralPoint) {
        Set<P> adjacent = new HashSet<>();
        for (int y = centralPoint.getY() - 1; y < centralPoint.getY() + 1; y++) {
            for (int x = centralPoint.getX() - 1; x < centralPoint.getX() + 1; x++) {
                getPoint(x, y).ifPresent(adjacent::add);
            }
        }

        return adjacent;
    }

    private Set<P> getGridAdjacentPoints(P centralPoint) {
        return null;
    }

    public void print() {
        iterateOverGrid(
                (x, y) -> getPoint(x, y)
                        .ifPresent(point -> System.out.printf("%s", point.getSymbol())),
                (y) -> System.out.printf("\n"));
        System.out.println();
    }

    private void iterateOverGrid(BiConsumer<Integer, Integer> cellConsumer, Consumer<Integer> rowConsumer) {
        for (int y = 0; y < points.length; y++) {
            for (int x = 0; x < points[y].length; x++) {
                cellConsumer.accept(x, y);
            }
            rowConsumer.accept(y);
        }
    }

    private Optional<P> getPoint(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return Optional.of(points[y][x]);
        } else {
            return Optional.empty();
        }
    }
}
