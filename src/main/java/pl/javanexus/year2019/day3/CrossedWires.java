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

            @Override
            public List<Intersection> traverseWire(byte[][] grid, Point from, int length) {
                List<Intersection> intersections = new LinkedList<>();
                for (int x = from.getX(); x <= from.getX() + length; x++) {
                    if (grid[from.getY()][x] == INTERSECTION) {
                        intersections.add(new Intersection(new Point(x, from.getY()), x - from.getX()));
                    }
                }
                return intersections;
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

            @Override
            public List<Intersection> traverseWire(byte[][] grid, Point from, int length) {
                List<Intersection> intersections = new LinkedList<>();
                for (int x = from.getX(); x >= from.getX() - length; x--) {
                    if (grid[from.getY()][x] == INTERSECTION) {
                        intersections.add(new Intersection(new Point(x, from.getY()), from.getX() - x));
                    }
                }
                return intersections;
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

            @Override
            public List<Intersection> traverseWire(byte[][] grid, Point from, int length) {
                List<Intersection> intersections = new LinkedList<>();
                for (int y = from.getY(); y >= from.getY() - length; y--) {
                    if (grid[y][from.getX()] == INTERSECTION) {
                        intersections.add(new Intersection(new Point(from.getX(), y), from.getY() - y));
                    }
                }
                return intersections;
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

            @Override
            public List<Intersection> traverseWire(byte[][] grid, Point from, int length) {
                List<Intersection> intersections = new LinkedList<>();
                for (int y = from.getY(); y <= from.getY() + length; y++) {
                    if (grid[y][from.getX()] == INTERSECTION) {
                        intersections.add(new Intersection(new Point(from.getX(), y), y - from.getY()));
                    }
                }
                return intersections;
            }
        };

        private static final Map<String, Direction> SYMBOLS = new HashMap<>();

        public static final byte INTERSECTION = 3;
        public static final byte START = 9;
        public static final byte EMPTY = 0;

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

        public abstract List<Intersection> traverseWire(byte[][] grid, Point from, int length);

        void lieWire(byte[][] grid, int x, int y, byte wireId, List<Point> intersections) {
            if (grid[y][x] != EMPTY && grid[y][x] != START && grid[y][x] != wireId) {
                intersections.add(new Point(x, y));
                grid[y][x] = INTERSECTION;
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
        grid[centralPort.getY()][centralPort.getX()] = Direction.START;
//        printGrid(grid);

        List<Point> intersections = new LinkedList();
        lieWire(grid, centralPort, firstWire, (byte)1, intersections);
        lieWire(grid, centralPort, secondWire, (byte)2, intersections);

        Map<Point, Integer> distanceToIntersection = new HashMap<>();
        System.out.println("Traverse first wire:");
        traverseWire(grid, centralPort, firstWire, distanceToIntersection);
        System.out.println("Traverse second wire:");
        traverseWire(grid, centralPort, secondWire, distanceToIntersection);

        Integer minDistance = distanceToIntersection.values().stream()
                .min(Comparator.comparingInt(Integer::intValue)).get();
        System.out.println("Min distance: " + minDistance + "\n\n");

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

    public void traverseWire(byte[][] grid, Point from, String[] wireDirections, Map<Point, Integer> distanceToIntersection) {
        grid[from.getY()][from.getX()] = 1;
//        printGrid(grid);

        int distance = 0;
        for (String wireDirection : wireDirections) {
            Matcher matcher = INPUT_PATTERN.matcher(wireDirection);
            if (matcher.find()) {
                Direction direction = Direction.getDirection(matcher.group(1));
                int length = Integer.parseInt(matcher.group(2));

                List<Intersection> intersections = direction.traverseWire(grid, from, length);
                for (Intersection intersection : intersections) {
                    System.out.println("Found intersection: " + (distance + intersection.getDistance()) + " at " + intersection.getPoint());
                    int totalDistance = distanceToIntersection.getOrDefault(intersection.getPoint(), 0);
                    distanceToIntersection.put(intersection.getPoint(), totalDistance + distance + intersection.getDistance());
                }

                distance += length;
                from = direction.getTo(from, length);
            } else {
                throw new RuntimeException("Unexpected input: " + wireDirection);
            }
        }
    }
}
