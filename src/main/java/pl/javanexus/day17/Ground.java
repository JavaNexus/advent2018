package pl.javanexus.day17;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import pl.javanexus.Line;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Ground {

    private final Tile[][] grid;
    private final int width;
    private final int height;

    public Ground(List<Line> lines) {
        this.width = lines.stream().mapToInt(reservoir -> reservoir.getTo().getX()).max().getAsInt() + 1;
        this.height = lines.stream().mapToInt(reservoir -> reservoir.getTo().getY()).max().getAsInt() + 1;
        this.grid = initGrid(GroundType.SAND);
        fillInGrid(lines, GroundType.CLAY);
    }

    private void fillInGrid(List<Line> lines, GroundType groundType) {
        for (Line line : lines) {
            for (int x = line.getFrom().getX(); x <= line.getTo().getX(); x++) {
                for (int y = line.getFrom().getY(); y <= line.getTo().getY(); y++) {
                    getGroundTile(x, y).ifPresent(tile -> tile.setGroundType(groundType));
                }
            }
        }
    }

    private Tile[][] initGrid(GroundType defaultGroundType) {
        Tile[][] grid = new Tile[height][];
        for (int y = 0; y < grid.length; y++) {
            grid[y] = new Tile[width];
            for (int x = 0; x < grid[y].length; x++) {
                grid[y][x] = new Tile(defaultGroundType);
            }
        }

        return grid;
    }

    public void simulateWaterFlow(Point firstPoint) {
        Stack<Point> groundToCover = new Stack<>();
        groundToCover.push(firstPoint);

        while (!groundToCover.isEmpty()) {
            Point currentPoint = groundToCover.pop();

            getGroundTile(currentPoint).ifPresent(currentTile -> {
                simulateWaterFlow(currentPoint, currentTile, groundToCover);
                printGround();
            });
        }
    }

    private void simulateWaterFlow(Point currentPoint, Tile currentTile, Stack<Point> groundToCover) {
        currentTile.visit();

        switch (currentTile.getGroundType()) {
            case SAND:
            case WATER:
                currentTile.setGroundType(GroundType.WATER);
                getNextPoints(currentPoint)
                        .forEach(nextPoint -> groundToCover.push(nextPoint));
//                groundToCover.push(currentPoint.getPointBelow());
                break;
            case CLAY:
//                Point pointAbove = currentPoint.getPointAbove();
//                groundToCover.push(pointAbove.getPointToTheLeft());
//                groundToCover.push(pointAbove.getPointToTheRight());
                break;
//            case WATER:
//                groundToCover.push(currentPoint.getPointAbove());
//                break;
            default:
                throw new IllegalArgumentException("Unexpected ground type: " + currentPoint);
        }
    }

    /**
     * 1) If tileBelow == CLAY spread left and right until tileBelow != CLAY
     *
     * @param currentPoint
     * @return
     */
    private Set<Point> getNextPoints(Point currentPoint) {
        Iterator<Point[]> possiblePoints = Arrays.asList(
                new Point[] {currentPoint.getPointBelow()},
                new Point[] {currentPoint.getPointToTheLeft()},
                new Point[] {currentPoint.getPointToTheRight()},
                new Point[] {currentPoint.getPointAbove()}
        ).iterator();

        Set<Point> nextPoints = new HashSet<>();
        while (nextPoints.isEmpty() && possiblePoints.hasNext()) {
            Stream.of(possiblePoints.next())
                    .filter(this::canWaterFlowThrough)
                    .forEach(nextPoints::add);
        }

        return nextPoints;
    }

    private boolean canWaterFlowThrough(Point point) {
        return getGroundTile(point)
                .filter(Tile::canWaterFlowThrough)
                .isPresent();
    }

    public int measureWaterVolume() {
        int waterVolume = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (getGroundTile(x, y).get().getGroundType() == GroundType.WATER) {
                    waterVolume++;
                }
            }
        }

        return waterVolume;
    }

    public void printGround() {
        iterateOverGrid(
                (x, y) -> getGroundTile(x, y)
                        .ifPresent(groundTile -> {
                            if (groundTile.numberOfVisits > 0) {
                                System.out.printf("%d", groundTile.numberOfVisits);
                            } else {
                                System.out.printf("%s", groundTile.getGroundType().getSymbol());
                            }
                        }),
                (y) -> System.out.printf("\n"),
                475);
        System.out.println();
    }

    private void iterateOverGrid(BiConsumer<Integer, Integer> cellConsumer, Consumer<Integer> rowConsumer, int offsetX) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = offsetX; x < grid[y].length; x++) {
                cellConsumer.accept(x, y);
            }
            rowConsumer.accept(y);
        }
    }

    private void setGroundTile(Point point, Tile groundTile) {
        setGroundTile(point.getX(), point.getY(), groundTile);
    }

    private void setGroundTile(int x, int y, Tile groundTile) {
        grid[y][x] = groundTile;
    }

    private Optional<Tile> getGroundTile(Point point) {
        return getGroundTile(point.getX(), point.getY());
    }

    private Optional<Tile> getGroundTile(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return Optional.of(grid[y][x]);
        } else {
            return Optional.empty();
        }
    }

    private class Tile {

        public static final int MAX_NUMBER_OF_VISITS = 3;
        @Getter @Setter
        private GroundType groundType;
        private int numberOfVisits;

        public Tile(GroundType groundType) {
            this.groundType = groundType;
            this.numberOfVisits = 0;
        }

        public void visit() {
            this.numberOfVisits++;
        }

        public boolean canWaterFlowThrough() {
            return numberOfVisits < MAX_NUMBER_OF_VISITS &&
                    (groundType == GroundType.SAND || groundType == GroundType.WATER);
        }
    }
}
