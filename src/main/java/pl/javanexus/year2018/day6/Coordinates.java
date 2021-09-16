package pl.javanexus.year2018.day6;

import lombok.Data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Coordinates {

    public static final int MAX_SIZE = 400;
    public static final int EMPTY_POINT = -1;

    private final Map<Integer, Point> pointsIndex = new HashMap<>();
    private final int[][] grid = new int[MAX_SIZE][];

    public Coordinates() {
        for (int x = 0; x < grid.length; x++) {
            grid[x] = new int[MAX_SIZE];
            for (int y = 0; y < grid[x].length; y++) {
                grid[x][y] = EMPTY_POINT;
            }
        }
    }

    public void setPoints(Point[] points) {
        for (Point point : points) {
            grid[point.getX()][point.getY()] = point.getId();
            pointsIndex.put(point.getId(), point);
        }
    }

    public int calculateSizeOfRegionWithinMaxDistanceToEachPoint(int maxDistanceToEachPoint) {
        int regionSize = 0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (calculateSumOfDistances(x, y) < maxDistanceToEachPoint) {
                    regionSize++;
                }
            }
        }

        return regionSize;
    }

    private int calculateSumOfDistances(int x, int y) {
        return pointsIndex.values().stream().mapToInt(p -> Math.abs(p.getX() - x) + Math.abs(p.getY() - y)).sum();
    }

    public void calculateClosestPoint() {
        iterateOverGrid(
                (x, y) -> {
                    if (grid[x][y] == EMPTY_POINT) {
                        int closestPoint = getClosestPoint(x, y);
                        grid[x][y] = closestPoint;
                    }
                },
                (y) -> {});
    }

    private int getClosestPoint(int x, int y) {
        List<PointWithDistance> pointsSortedByClosestDistance = pointsIndex.values().stream()
                .map(point -> new PointWithDistance(point, point.getDistance(x, y)))
                .sorted(Comparator.comparingInt(PointWithDistance::getDistance))
                .collect(Collectors.toList());
        if (isMoreThanOnePointTheSameDistance(pointsSortedByClosestDistance)) {
            return EMPTY_POINT;
        } else {
            return pointsSortedByClosestDistance.get(0).getPoint().getId();
        }
    }

    @Data
    private class PointWithDistance {
        private final Point point;
        private final int distance;
    }

    private boolean isMoreThanOnePointTheSameDistance(List<PointWithDistance> pointsSortedByClosestDistance) {
        return pointsSortedByClosestDistance.size() > 1 &&
                pointsSortedByClosestDistance.get(0).getDistance() == pointsSortedByClosestDistance.get(1).getDistance();
    }

    public Map<Integer, Point> getAreas() {
        iterateOverGrid(
                (x, y) -> {
                    int id = grid[x][y];
                    if (id != EMPTY_POINT) {
                        pointsIndex.get(id).incrementArea();
                    }
                },
                (y) -> {});

        return pointsIndex;
    }

    public void printGrid() {
        iterateOverGrid(
                (x, y) -> System.out.printf("[%02d]", grid[x][y]),
                (y) -> System.out.print("\n"));
    }

    public void printGridToFile() throws IOException {
        File file = new File("D:\\Projekty\\Java\\advent2018\\src\\test\\resources\\gridPoints.js");
        FileWriter fileWriter = new FileWriter(file);
        iterateOverGrid(
                (x, y) -> {
                    try {
                        fileWriter.append(String.format("{id:%d, x:%d, y:%d},\n", grid[x][y], x, y));
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                },
                (y) -> {});
        fileWriter.flush();
        fileWriter.close();
    }

    private void iterateOverGrid(BiConsumer<Integer, Integer> onPoint, Consumer<Integer> afterRow) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                onPoint.accept(x, y);
            }
            afterRow.accept(y);
        }
    }
}
