package pl.javanexus.year2019.day13;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.javanexus.year2019.day5.DiagnosticProgram;

import java.io.*;
import java.util.Arrays;

public class CarePackage {

    public static final int GRID_HEIGHT = 25;
    public static final int GRID_WIDTH = 40;

    private Point ballPosition;
    private Point paddlePosition;

    private long[] instructions;
    private final int[][] grid;

    enum TileType {
        EMPTY(0, " ", false),//No game object appears in this tile.
        WALL(1, "|", true),//Walls are indestructible barriers.
        BLOCK(2, "#", true),//Blocks can be broken by the ball.
        PADDLE(3, "-", true),//The paddle is indestructible.
        BALL(4, "O", true);//The ball moves diagonally and bounces off objects.

        private static final TileType[] INDEX = new TileType[TileType.values().length];
        static {
            for(TileType tileType : TileType.values()) {
                INDEX[tileType.getId()] = tileType;
            }
        }

        public static TileType getType(int tileId) {
            return INDEX[tileId];
        }

        @Getter
        private final int id;
        @Getter
        private final String symbol;
        @Getter
        private final boolean blocking;

        TileType(int id, String symbol, boolean blocking) {
            this.id = id;
            this.symbol = symbol;
            this.blocking = blocking;
        }
    }

    private final DiagnosticProgram program;

    public static void main(String[] args) throws IOException {
        long[] instructions = getInstructions("./src/test/resources/year2019/day13/input1.csv", ",");
        CarePackage carePackage = new CarePackage(instructions);
        carePackage.insertQuarters(2);
//        carePackage.executeProgram();
        carePackage.playGame();
    }

    private static long[] getInstructions(String filePath, String delimiter) throws IOException {
        String[] values = new BufferedReader(new FileReader(new File(filePath))).readLine().split(delimiter);
        return Arrays.stream(values).mapToLong(Long::parseLong).toArray();
    }

    public CarePackage(long[] instructions) {
        this.program = new DiagnosticProgram();
        this.instructions = instructions;
        this.grid = createGrid();

        this.ballPosition = new Point(18, 20);
        this.paddlePosition = new Point(20, 23);
    }

    private int[][] createGrid() {
        final int[][] grid = new int[GRID_HEIGHT][];

        for (int y = 0; y < grid.length; y++) {
            grid[y] = new int[GRID_WIDTH];
            Arrays.fill(grid[y], 0);
        }

        return grid;
    }

    public void playGame() {
        DiagnosticProgram.State state = new DiagnosticProgram.State(instructions);
        while (!state.isFinished()) {
            program.execute(state);
            updateGrid(state);
            printGrid();

            state.addInput(getNextInput());
        }
    }

    private int getNextInput() {
        if (paddlePosition.getX() > ballPosition.getX()) {
            return -1;
        } else if (paddlePosition.getX() < ballPosition.getX()) {
            return 1;
        } else {
            return 0;
        }
    }

    private TileType getTileType(Point point) {
        return getTileType(point.getX(), point.getY());
    }

    private TileType getTileType(int x, int y) {
        int nextTileId = grid[y][x];
        return TileType.getType(nextTileId);
    }

    public void executeProgram() {
        DiagnosticProgram.State state = new DiagnosticProgram.State(instructions);
        while (!state.isFinished()) {
            program.execute(state);
        }
        updateGrid(state);
        printGrid();
    }

    private void updateGrid(DiagnosticProgram.State state) {
        final Tile tile = new Tile();
        while (state.hasOutput()) {
            if (tile.setValueFromOutput((int) state.getOutput())) {
                if (tile.getPoint().containsScore()) {
                    System.out.println("Score: " + tile.getTileId());
                } else {
                    if (tile.getTileId() == TileType.BALL.getId()) {
                        this.ballPosition.update(tile.getPoint());
                    } else if (tile.getTileId() == TileType.PADDLE.getId()) {
                        this.paddlePosition.update(tile.getPoint());
                    }
                    grid[tile.getPoint().getY()][tile.getPoint().getX()] = tile.getTileId();
                }
            }
        }
    }

    public void printGrid() {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                System.out.print(grid[y][x]);
            }
            System.out.print("\n");
        }
    }

    public void printGrid(PrintWriter writer) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                writer.print(grid[y][x]);
            }
            writer.print("\n");
        }
        writer.flush();
    }

    public int[] countTiles() {
        final int[] numberOfTiles = new int[TileType.values().length];
        Arrays.fill(numberOfTiles, 0);

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                numberOfTiles[grid[y][x]]++;
            }
        }

        return numberOfTiles;
    }

    public void insertQuarters(int quarters) {
        instructions[0] = quarters;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Point {
        private int x;
        private int y;

        public Point(Point point) {
            this.update(point);
        }

        public void update(int dX, int dY) {
            this.x += dX;
            this.y += dY;
        }

        public boolean containsScore() {
            return x == -1 && y == 0;
        }

        public void update(Point point) {
            this.x = point.getX();
            this.y = point.getY();
        }
    }

    private static class Tile {
        @Getter
        private final Point point = new Point();
        @Getter
        private int tileId;

        private int outputIndex = 0;

        public boolean setValueFromOutput(int value) {
            switch (outputIndex++) {
                case 0:
                    point.setX(value);
                    return false;
                case 1:
                    point.setY(value);
                    return false;
                case 2:
                    this.tileId = value;
                    this.outputIndex = 0;
                    return true;
                default:
                    throw new RuntimeException("Unexpected output size");
            }
        }


    }
}
