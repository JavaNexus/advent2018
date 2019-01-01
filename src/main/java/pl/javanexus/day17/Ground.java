package pl.javanexus.day17;

import lombok.Getter;
import lombok.Setter;
import pl.javanexus.Line;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Ground implements SerializableToJson {

    private final Map<Integer, Point> spillagePoints;
    private final Tile[][] grid;

    private final int width;
    private final int height;

    public Ground(List<Line> lines) {
        this.width = lines.stream().mapToInt(reservoir -> reservoir.getTo().getX()).max().getAsInt() + 2;
        this.height = lines.stream().mapToInt(reservoir -> reservoir.getTo().getY()).max().getAsInt() + 2;
        this.spillagePoints = new HashMap<>();
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

    public void simulateWaterFlow(Point firstPoint, int expectedNumberOfSpillagePoints) {
        Stack<Point> groundToCover = new Stack<>();
        Stack<Point> groundVisited = new Stack<>();

        Point currentPoint = firstPoint;
        while (currentPoint != null) {
            Optional<Tile> groundTile = getGroundTile(currentPoint);
            if (groundTile.isPresent()) {
                simulateWaterFlow(currentPoint, groundTile.get(), groundToCover);
                printGround();
            }

//            if (spillagePoints.size() == expectedNumberOfSpillagePoints) {
//                currentPoint = null;
//            } else
            if (!groundToCover.isEmpty()) {
                groundVisited.push(currentPoint);
                currentPoint = groundToCover.pop();
            } else if (!groundVisited.isEmpty()) {
                currentPoint = groundVisited.pop();
            } else {
                currentPoint = null;
            }
        }
    }

    private void simulateWaterFlow(Point currentPoint, Tile currentTile, Stack<Point> groundToCover) {
        currentTile.setGroundType(GroundType.WATER);
        currentTile.visit();

        if (currentPoint.getY() == height - 1) {
            spillagePoints.put(currentPoint.getX(), currentPoint);
        } else if (currentTile.numberOfVisits < 2) {//!spillagePoints.containsKey(currentPoint.getX())
            getNextPoints(currentPoint)
                    .forEach(nextPoint -> groundToCover.push(nextPoint));
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
                new Point[] {currentPoint.getPointToTheRight()}
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
        for (int x = 0; x < width - 1; x++) {
            for (int y = 0; y < height - 1; y++) {
                if (getGroundTile(x, y).get().getGroundType() == GroundType.WATER) {
                    waterVolume++;
                }
            }
        }

        return waterVolume - 1;
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
                480);
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

    @Override
    public String toJSON() {
        StringBuilder builder = new StringBuilder();
        builder.append("var points = [");

        iterateOverGrid(
                (x, y) -> {
                    getGroundTile(x, y)
                            .map(Tile::getGroundType)
                            .filter(type -> type == GroundType.WATER || type == GroundType.CLAY)
                            .ifPresent(type -> builder.append(String.format("{x:%d, y:%d, s:'%s'},", x, y, type.getSymbol())));
                },
                (y) -> {},
                0);
        builder.append("];");

        return builder.toString();
    }

    private class Tile {

        @Getter @Setter
        private GroundType groundType;
        private int numberOfVisits;

        public Tile(GroundType groundType) {
            this.groundType = groundType;
        }

        public boolean canWaterFlowThrough() {
            return groundType == GroundType.SAND;
        }

        public void visit() {
            numberOfVisits++;
        }
    }
}
