package pl.javanexus.year2019.day3;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrossedWires {

    public static final Pattern INPUT_PATTERN = Pattern.compile("([RULD])([0-9]+)");

    enum Direction {
        RIGHT("R") {
            @Override
            public Point getTo(Point from, int wireLength) {
                return new Point(from.getX() + wireLength, from.getY());
            }

            @Override
            public Point lieWire(byte[][] grid, Point from, int length, byte wireId, List<Point> intersections) {
                for (int x = from.getX(); x <= from.getX() + length; x++) {
                    lieWire(grid, x, from.getY(), wireId, intersections);
                }
                return getTo(from, length);
            }
        },
        LEFT("L") {
            @Override
            public Point getTo(Point from, int wireLength) {
                return new Point(from.getX() - wireLength, from.getY());
            }

            @Override
            public Point lieWire(byte[][] grid, Point from, int length, byte wireId, List<Point> intersections) {
                for (int x = from.getX(); x >= from.getX() - length; x--) {
                    lieWire(grid, x, from.getY(), wireId, intersections);
                }
                return getTo(from, length);
            }
        },
        UP("U") {
            @Override
            public Point getTo(Point from, int wireLength) {
                return new Point(from.getX(), from.getY() - wireLength);
            }

            @Override
            public Point lieWire(byte[][] grid, Point from, int length, byte wireId, List<Point> intersections) {
                for (int y = from.getY(); y >= from.getY() - length; y--) {
                    lieWire(grid, from.getX(), y, wireId, intersections);
                }
                return getTo(from, length);
            }
        },
        DOWN("D") {
            @Override
            public Point getTo(Point from, int wireLength) {
                return new Point(from.getX(), from.getY() + wireLength);
            }

            @Override
            public Point lieWire(byte[][] grid, Point from, int length, byte wireId, List<Point> intersections) {
                for (int y = from.getY(); y <= from.getY() + length; y++) {
                    lieWire(grid, from.getX(), y, wireId, intersections);
                }
                return getTo(from, length);
            }
        };

        private static final Map<String, Direction> SYMBOLS = new HashMap<>();
        static {
            for (Direction dir : Direction.values()) {
                SYMBOLS.put(dir.symbol, dir);
            }
        }

        public static Direction getDirection(String symbol) {
            return SYMBOLS.get(symbol);
        }

        private final String symbol;

        Direction(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }

        public abstract Point getTo(Point from, int wireLength);

        public abstract Point lieWire(byte[][] grid, Point from, int length, byte wireId, List<Point> intersections);

        void lieWire(byte[][] grid, int x, int y, byte wireId, List<Point> intersections) {
            if (grid[y][x] != 0 && grid[y][x] != wireId) {
                intersections.add(new Point(x, y));
                grid[y][x] = 3;
            } else {
                grid[y][x] = wireId;
            }
        }
    }

    public Boundary findBoundary(String[] firstWire, String[] secondWire) {
        final Point centralPort = new Point(0, 0);
        return findBoundary(
                findPointsOnPath(centralPort, firstWire),
                findPointsOnPath(centralPort, secondWire));
    }

    public Boundary findBoundary(List<Point> pointsOnFirstWire, List<Point> pointsOnSecondWire) {
        List<Point> points = new LinkedList<>();
        points.addAll(pointsOnFirstWire);//findPointsOnPath(centralPort, firstWire)
        points.addAll(pointsOnSecondWire);//findPointsOnPath(centralPort, secondWire)

        Boundary boundary = new Boundary();
        for (Point point : points) {
            boundary.update(point);
        }

        System.out.println(boundary.getWidth() + " x " + boundary.getHeight());

        return boundary;
    }

    private List<Point> findPointsOnPath(Point from, String[] wireDirections) {
        List<Point> points = new LinkedList<>();
        points.add(from);

        for (String wireDirection : wireDirections) {
            Matcher matcher = INPUT_PATTERN.matcher(wireDirection);
            if (matcher.find()) {
                Direction direction = Direction.getDirection(matcher.group(1));
                int length = Integer.parseInt(matcher.group(2));

                from = direction.getTo(from, length);
                points.add(from);
            } else {
                throw new RuntimeException("Unexpected input: " + wireDirection);
            }
        }

        return points;
    }

    public int readValues(String[] firstWire, String[] secondWire) {
        Point centralPort = new Point(0, 0);
        List<Point> pointsOnFirstPath = findPointsOnPath(centralPort, firstWire);
        List<Point> pointsOnSecondPath = findPointsOnPath(centralPort, secondWire);

        final Boundary boundary = findBoundary(pointsOnFirstPath, pointsOnSecondPath);
        centralPort = boundary.getCentralPort();

        byte[][] grid = getGrid(boundary.getWidth() + 1, boundary.getHeight() + 1);
        grid[centralPort.getY()][centralPort.getX()] = 9;
//        printGrid(grid);

        List<Point> intersections = new LinkedList();
        lieWire(grid, centralPort, firstWire, (byte)1, intersections);
        lieWire(grid, centralPort, secondWire, (byte)2, intersections);

        int minDistance = -1;
        int distance = 0;

        Point previousPoint = centralPort;
        Iterator<Point> pointsIterator = pointsOnFirstPath.iterator();
        Iterator<Point> intersectionIterator = intersections.iterator();
        Point intersection = intersectionIterator.next();

        while (pointsIterator.hasNext() && intersectionIterator.hasNext()) {
            Point nextPoint = pointsIterator.next();
            if (intersection.isBetweenPoints(previousPoint, nextPoint, centralPort)) {
               //todo: calculate distance
            }
            distance += nextPoint.getDistance(previousPoint);
        }
        System.out.println(distance);

        return findSmallestDistanceToIntersection(boundary, centralPort, intersections);
    }

    private int findSmallestDistanceToIntersection(Boundary boundary, Point centralPort, List<Point> intersections) {
        int smallestDistance = boundary.getWidth() + boundary.getHeight();
        for (Point intersection : intersections) {
            if (!centralPort.equals(intersection)) {
                int distance = intersection.getDistance(centralPort);
                if (distance < smallestDistance) {
                    smallestDistance = distance;
                }
            }
        }

        return smallestDistance;
    }

    private void calculateDistanceToIntersection(byte[][] grid, Point from, String[] wireDirections) {
    }

    private void lieWire(byte[][] grid, Point from, String[] wireDirections, byte wireId, List<Point> intersections) {
        for (String wireDirection : wireDirections) {
//            System.out.println(wireId + " : " + wireDirection);
            Matcher matcher = INPUT_PATTERN.matcher(wireDirection);
            if (matcher.find()) {
                Direction direction = Direction.getDirection(matcher.group(1));
                int length = Integer.parseInt(matcher.group(2));

                from = direction.lieWire(grid, from, length, wireId, intersections);
            } else {
                throw new RuntimeException("Unexpected input: " + wireDirection);
            }
        }
    }

    private byte[][] getGrid(int width, int height) {
        byte[][] grid = new byte[height][];//rows
        for (int row = 0; row < grid.length; row++) {
//            System.out.println("Row: " + row);
            grid[row] = new byte[width];//columns
        }

        return grid;
    }

    private void printGrid(byte[][] grid) {
        for (byte[] row : grid) {
            for (int col = 0; col < row.length; col++) {
                System.out.print(row[col]);
            }
            System.out.print("\n");
        }
    }

    private Map<String, Integer> getDimensions(String[] wireDirections) {
        Map<String, Integer> dimensions = new HashMap<>();

        for (String wireDirection : wireDirections) {
            Matcher matcher = INPUT_PATTERN.matcher(wireDirection);
            if (matcher.find()) {
                String direction = matcher.group(1);
                Integer length = dimensions.getOrDefault(direction, 0);
                length += Integer.parseInt(matcher.group(2));
                dimensions.put(direction, length);
            } else {
                throw new RuntimeException("Unexpected input: " + wireDirection);
            }
        }

        return dimensions;
    }

    private int[] getWidthAndHeight(Map<String, Integer> firstDimensions, Map<String, Integer> secondDimensions) {
        int maxWidth = getMaxLength(firstDimensions, secondDimensions, "R")
                + getMaxLength(firstDimensions, secondDimensions, "L");
        int maxHeight = getMaxLength(firstDimensions, secondDimensions, "U")
                + getMaxLength(firstDimensions, secondDimensions, "D");

        return new int[] {maxWidth, maxHeight};
    }

    private int getMaxLength(Map<String, Integer> firstDimensions, Map<String, Integer> secondDimensions, String direction) {
        return Integer.max(firstDimensions.get(direction), secondDimensions.get(direction));
    }

    public void getIntersections(byte[][] grid) {

    }
}
