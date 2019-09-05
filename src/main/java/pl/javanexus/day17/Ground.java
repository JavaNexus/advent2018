package pl.javanexus.day17;

import lombok.Getter;
import lombok.Setter;
import pl.javanexus.Line;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Ground implements SerializableToJson {

    private final Tile[][] grid;
    private final int width;
    private final int height;

    private final int bottom;

    private final Stack<Point> pointsVisited;
    private final Stack<Point> pointsToVisit;

    public Ground(List<Line> lines) {
        this(lines, -1);
    }

    public Ground(List<Line> lines, int bottom) {
        this.width = lines.stream().mapToInt(reservoir -> reservoir.getTo().getX()).max().getAsInt() + 2;
        this.height = lines.stream().mapToInt(reservoir -> reservoir.getTo().getY()).max().getAsInt() + 2;
        this.grid = initGrid(GroundType.SAND);
        fillInGrid(lines, GroundType.CLAY);

        this.bottom = bottom > 0 ? bottom : height;

        this.pointsVisited = new Stack<>();
        this.pointsToVisit = new Stack<>();
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

    private void fillInGrid(List<Line> lines, GroundType groundType) {
        for (Line line : lines) {
            for (int x = line.getFrom().getX(); x <= line.getTo().getX(); x++) {
                for (int y = line.getFrom().getY(); y <= line.getTo().getY(); y++) {
                    getGroundTile(x, y).ifPresent(tile -> tile.setGroundType(groundType));
                }
            }
        }
    }

    public void simulateWaterFlow(Point firstPoint) {
        Point currentPoint = firstPoint;
        while (currentPoint != null) {
            Tile currentTile = getGroundTile(currentPoint).get();

            List<Point> nextPoints;
            if (currentPoint.getY() == bottom - 1) {
                nextPoints = Collections.emptyList();
                pointsVisited.clear();
            } else {
                nextPoints = getNextPoint(currentPoint, currentTile);
            }

            boolean waterCanFlowThrough = false;
            if (!nextPoints.isEmpty()) {
                pointsVisited.push(currentPoint);
                currentPoint = nextPoints.get(0);

                for (int i = 1; i < nextPoints.size(); i++) {
                    pointsToVisit.push(nextPoints.get(i));
                }
            } else if (!pointsVisited.isEmpty()) {
                currentPoint = pointsVisited.pop();
            } else if (!pointsToVisit.isEmpty()) {
                currentPoint = pointsToVisit.pop();
            } else {
                currentPoint = null;
            }
            currentTile.flowWaterThrough(waterCanFlowThrough);

            printGround(450);
        }
    }

    private List<Point> getNextPoint(Point currentPoint, Tile currentTile) {
        final List<Point> possiblePoints;
        if (currentTile.getGroundType() == GroundType.SAND) {
            possiblePoints = new LinkedList<>();
            possiblePoints.add(currentPoint.getPointBelow());
            possiblePoints.addAll(getHorizontalNeighbours(currentPoint));
        } else if (currentTile.getGroundType() == GroundType.WATER_FLOWING) {
            possiblePoints = getHorizontalNeighbours(currentPoint);
        } else if (currentTile.getGroundType() == GroundType.WATER_STILL) {
            possiblePoints = Arrays.asList(
                    currentPoint.getPointToTheRight()
            );
        } else {
            possiblePoints = Collections.emptyList();
        }

        List<Point> nextPoints = possiblePoints.stream()
                .filter(this::canWaterFlowThrough)
                .collect(Collectors.toList());
        if (nextPoints.isEmpty()) {
            return nextPoints;
        } else {
            return currentTile.getGroundType() == GroundType.WATER_FLOWING ? nextPoints : Collections.singletonList(nextPoints.get(0));
        }
    }

    private List<Point> getHorizontalNeighbours(Point currentPoint) {
        return Stream.of(currentPoint.getPointToTheLeft(), currentPoint.getPointToTheRight())
                .sorted(Comparator.comparingInt(point -> getGroundTile(point).map(Tile::getGroundType).map(GroundType::getPriority).orElse(5)))
                .collect(Collectors.toList());
    }

    private boolean canWaterFlowThrough(Point point) {
        return getGroundTile(point)
                .map(Tile::getGroundType)
                .filter(GroundType::canWaterFlowThrough)
                .isPresent();
    }

    public int measureWaterVolume() {
        int waterVolume = 0;
        for (int x = 0; x < width - 1; x++) {
            for (int y = 0; y < height - 1; y++) {
                GroundType groundType = getGroundTile(x, y).get().getGroundType();
                if (groundType == GroundType.WATER_FLOWING || groundType == GroundType.WATER_STILL) {
                    waterVolume++;
                }
            }
        }

        return waterVolume - 1;
    }

    public void printGround(int offsetX) {
        iterateOverGrid(
                (x, y) -> getGroundTile(x, y)
                        .ifPresent(groundTile -> System.out.printf("%s", groundTile.getGroundType().getSymbol())),
                (y) -> System.out.printf("\n"),
                offsetX);
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
                            .filter(type -> type == GroundType.WATER_STILL || type == GroundType.CLAY)
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

        public Tile(GroundType groundType) {
            this.groundType = groundType;
        }

        public void flowWaterThrough(boolean waterCanFlowThrough) {
//            this.groundType = GroundType.WATER_FLOWING;
            if (groundType == GroundType.SAND) {
                this.groundType = GroundType.WATER_FLOWING;
            } else if (groundType == GroundType.WATER_FLOWING) {// && waterCanFlowThrough
                this.groundType = GroundType.WATER_STILL;
            }
        }
    }
}
