package pl.javanexus.day17;

import lombok.Getter;
import lombok.Setter;
import pl.javanexus.Line;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Ground implements SerializableToJson {

    private final Tile[][] grid;
    private final int width;
    private final int height;

    private final boolean[] overflowPoints;
    private final Stack<Point> visitedPoints;

    public Ground(List<Line> lines) {
        this.width = lines.stream().mapToInt(reservoir -> reservoir.getTo().getX()).max().getAsInt() + 2;
        this.height = lines.stream().mapToInt(reservoir -> reservoir.getTo().getY()).max().getAsInt() + 2;
        this.grid = initGrid(GroundType.SAND);
        fillInGrid(lines, GroundType.CLAY);
        this.overflowPoints = new boolean[width];
        this.visitedPoints = new Stack<>();
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

    public void flood(Point firstPoint) {
        Stack<Point> waterSources = new Stack<>();
        waterSources.push(firstPoint);

        while (!waterSources.isEmpty()) {
            Point waterSource = waterSources.pop();

            getGroundTile(waterSource).ifPresent(currentTile -> {
                GroundType groundType = currentTile.getGroundType();
                if (groundType == GroundType.SAND) {
                    currentTile.setGroundType(GroundType.WATER_FLOWING);

                    waterSources.push(waterSource.getPointBelow());
                } else if (groundType == GroundType.CLAY) {
                    final Point pointAbove = waterSource.getPointAbove();
                    floodReservoirLevel(findReservoirEdges(pointAbove));
                    waterSources.push(pointAbove);
                } else if (groundType == GroundType.WATER_FLOWING) {
                    Line edges = findReservoirEdges(waterSource);
                    boolean isLeftEdgeInsideReservoir = isInsideReservoir(edges.getFrom());
                    boolean isRightEdgeInsideReservoir = isInsideReservoir(edges.getTo());
                    if (isLeftEdgeInsideReservoir && isRightEdgeInsideReservoir) {
                        floodReservoirLevel(findReservoirEdges(waterSource));
                        waterSources.push(waterSource.getPointAbove());
                    } else if (isLeftEdgeInsideReservoir && !isRightEdgeInsideReservoir) {
                        Line reservoirEdges = findReservoirEdges(waterSource.getPointBelow());
                        Line reservoirLevel = new Line(
                                reservoirEdges.getFrom().getPointAbove(),
                                reservoirEdges.getTo().getPointAbove().getPointToTheRight());
                        floodReservoirLevel(reservoirLevel);
                        waterSources.push(reservoirLevel.getTo());
                    } else if (!isLeftEdgeInsideReservoir && isRightEdgeInsideReservoir) {
                        Line reservoirEdges = findReservoirEdges(waterSource.getPointBelow());
                        Line reservoirLevel = new Line(
                                reservoirEdges.getFrom().getPointAbove().getPointToTheLeft(),
                                reservoirEdges.getTo().getPointAbove());
                        floodReservoirLevel(reservoirLevel);
                        waterSources.push(reservoirLevel.getFrom());
                    } else {
                        Line reservoirEdges = findReservoirEdges(waterSource.getPointBelow());
                        Line reservoirLevel = new Line(
                                reservoirEdges.getFrom().getPointAbove().getPointToTheLeft(),
                                reservoirEdges.getTo().getPointAbove().getPointToTheRight());
                        floodReservoirLevel(reservoirLevel);
                        waterSources.push(reservoirLevel.getFrom());
                        waterSources.push(reservoirLevel.getTo());
                    }
                }
            });
            printGround(450);
        }
    }

    private boolean isInsideReservoir(Point reservoirEdge) {
        return getGroundTile(reservoirEdge.getPointBelow())
                .filter(tile -> tile.getGroundType() == GroundType.CLAY)
                .isPresent();
    }

    private Line findReservoirEdges(Point waterSource) {
        int x = waterSource.getX();
        while (x > 0 && grid[waterSource.getY()][x].getGroundType() != GroundType.CLAY) {
            x--;
        }
        final Point from = new Point(x, waterSource.getY());

        x = waterSource.getX();
        while (x < width && grid[waterSource.getY()][x].getGroundType() != GroundType.CLAY) {
            x++;
        }

        return new Line(from, new Point(x, waterSource.getY()));
    }

    public void floodReservoirLevel(Line reservoirLevel) {
        final Point from = reservoirLevel.getFrom().getPointToTheRight();
        for (int x = from.getX(); x < reservoirLevel.getTo().getX(); x++) {
            grid[from.getY()][x].setGroundType(GroundType.WATER_FLOWING);
        }
    }

    public void simulateWaterFlow(Point firstPoint) {
        Point currentPoint = firstPoint;
        while (currentPoint != null) {
            Tile currentTile = getGroundTile(currentPoint).get();

            Optional<Point> nextPoint = getNextPoint(currentPoint, currentTile);
            boolean foo = false;
            if (nextPoint.isPresent()) {
                visitedPoints.push(currentPoint);
                foo = nextPoint.get().getX() > currentPoint.getX();
                currentPoint = nextPoint.get();
            } else if (!visitedPoints.isEmpty()) {
                currentPoint = visitedPoints.pop();
            } else {
                currentPoint = null;
            }
            currentTile.flowWaterThrough(foo);

            printGround(400);
        }
    }

    private boolean isBetweenOverflowPoints(int ox) {
        boolean isOverflowPointToTheLeft = false;
        for (int x = ox; x >= 0; x--) {
            if (overflowPoints[x]) {
                isOverflowPointToTheLeft = true;
            }
        }

        boolean isOverflowPointToTheRight = false;
        for (int x = ox; x < width; x++) {
            if (overflowPoints[x]) {
                isOverflowPointToTheRight = true;
            }
        }

        return isOverflowPointToTheLeft && isOverflowPointToTheRight;
    }

    private Optional<Point> getNextPoint(Point currentPoint, Tile currentTile) {
        if (currentPoint.getY() == height - 1) {
            return Optional.empty();
        }

        final List<Point> possiblePoints;
        if (currentTile.getGroundType() == GroundType.SAND) {
            possiblePoints = Arrays.asList(
                    currentPoint.getPointBelow(),
                    currentPoint.getPointToTheLeft(),
                    currentPoint.getPointToTheRight()
            );
        } else if (currentTile.getGroundType() == GroundType.WATER_FLOWING) {
            possiblePoints = Arrays.asList(
                    currentPoint.getPointToTheLeft(),
                    currentPoint.getPointToTheRight()
            );
        } else if (currentTile.getGroundType() == GroundType.WATER_STILL) {
            possiblePoints = Arrays.asList(
                    currentPoint.getPointToTheRight()
            );
        } else {
            possiblePoints = Collections.emptyList();
        }

        return possiblePoints.stream()
                .filter(this::canWaterFlowThrough)
                .findAny();
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
                if (getGroundTile(x, y).get().getGroundType() == GroundType.WATER_STILL) {
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
            if (groundType == GroundType.SAND) {
                this.groundType = GroundType.WATER_FLOWING;
            } else if (groundType == GroundType.WATER_FLOWING && !waterCanFlowThrough) {
                this.groundType = GroundType.WATER_STILL;
            }
        }
    }
}
