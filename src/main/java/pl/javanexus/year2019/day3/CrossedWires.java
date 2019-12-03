package pl.javanexus.year2019.day3;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrossedWires {

    public static final Pattern INPUT_PATTERN = Pattern.compile("([RULD])([0-9]+)");

    enum Direction {
        RIGHT("R") {
            @Override
            public int[] lieWire(byte[][] grid, int fromX, int fromY, int length, byte wireId, List<int[]> intersections) {
                for (int x = fromX; x <= fromX + length; x++) {
                    lieWire(grid, x, fromY, wireId, intersections);
                }
                return new int[] {fromX + length, fromY};
            }
        },
        LEFT("L") {
            @Override
            public int[] lieWire(byte[][] grid, int fromX, int fromY, int length, byte wireId, List<int[]> intersections) {
                for (int x = fromX; x >= fromX - length; x--) {
                    lieWire(grid, x, fromY, wireId, intersections);
                }
                return new int[] {fromX - length, fromY};
            }
        },
        UP("U") {
            @Override
            public int[] lieWire(byte[][] grid, int fromX, int fromY, int length, byte wireId, List<int[]> intersections) {
                for (int y = fromY; y >= fromY - length; y--) {
                    lieWire(grid, fromX, y, wireId, intersections);
                }
                return new int[] {fromX, fromY - length};
            }
        },
        DOWN("D") {
            @Override
            public int[] lieWire(byte[][] grid, int fromX, int fromY, int length, byte wireId, List<int[]> intersections) {
                for (int y = fromY; y <= fromY + length; y++) {
                    lieWire(grid, fromX, y, wireId, intersections);
                }
                return new int[] {fromX, fromY + length};
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

        public abstract int[] lieWire(byte[][] grid, int fromX, int fromY, int length, byte wireId, List<int[]> intersections);

        void lieWire(byte[][] grid, int x, int y, byte wireId, List<int[]> intersections) {
            if (grid[y][x] != 0 && grid[y][x] != wireId) {
                intersections.add(new int[] {x, y});
                grid[y][x] = 3;
            } else {
                grid[y][x] = wireId;
            }
        }
    }

    public int readValues(String[] firstWire, String[] secondWire) {//byte[][]
        Map<String, Integer> firstWireDimensions = getDimensions(firstWire);
        Map<String, Integer> secondWireDimensions = getDimensions(secondWire);

        System.out.println(firstWireDimensions);
        System.out.println(secondWireDimensions);

        int[] widthAndHeight = new int[] {500, 500};//new int[] {20, 20};//
//        getWidthAndHeight(firstWireDimensions, secondWireDimensions);
        //new int[] {1, 8};
        int[] centralPort = new int [] {
                Math.floorDiv(widthAndHeight[0], 2),
                Math.floorDiv(widthAndHeight[1], 2)};

        byte[][] grid = getGrid(widthAndHeight[0], widthAndHeight[1]);
        grid[centralPort[1]][centralPort[0]] = 9;

        List<int[]> intersections = new LinkedList();
        lieWire(grid, centralPort, firstWire, (byte)1, intersections);
        lieWire(grid, centralPort, secondWire, (byte)2, intersections);

        int smallestDistance = widthAndHeight[0] + widthAndHeight[1];
        for (int[] intersection : intersections) {
            if (centralPort[0] != intersection[0] && centralPort[1] != intersection[1]) {
                int distance = Math.abs(intersection[0] - centralPort[0]) + Math.abs(intersection[0] - centralPort[0]);
                System.out.println(Arrays.toString(intersection) + " : " + distance);
                if (distance < smallestDistance) {
                    smallestDistance = distance;
                }
            }
        }

//        intersections.stream().forEach(i -> System.out.println(Arrays.toString(i)));

        System.out.println(smallestDistance);
        printGrid(grid);

        return smallestDistance;//grid;
    }

    private void lieWire(byte[][] grid, int[] from, String[] wireDirections, byte wireId, List<int[]> intersections) {
        for (String wireDirection : wireDirections) {
//            System.out.println(wireId + " : " + wireDirection);
            Matcher matcher = INPUT_PATTERN.matcher(wireDirection);
            if (matcher.find()) {
                Direction direction = Direction.getDirection(matcher.group(1));
                int length = Integer.parseInt(matcher.group(2));

                from = direction.lieWire(grid, from[0], from[1], length, wireId, intersections);
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
