package pl.javanexus.year2019.day13;

import lombok.Getter;
import pl.javanexus.year2019.day5.DiagnosticProgram;

import java.util.Arrays;

public class CarePackage {

    public static final int MAX_SIZE = 50;

    private long[] instructions;
    private final int[][] grid;

    enum Tile {
        EMPTY(0, " "),//No game object appears in this tile.
        WALL(1, "|"),//Walls are indestructible barriers.
        BLOCK(2, "#"),//Blocks can be broken by the ball.
        PADDLE(3, "-"),//The paddle is indestructible.
        BALL(4, "O");//The ball moves diagonally and bounces off objects.

        private final int id;
        private final String symbol;

        Tile(int id, String symbol) {
            this.id = id;
            this.symbol = symbol;
        }

        public int getId() {
            return id;
        }
    }

    private final DiagnosticProgram program;

    public CarePackage(long[] instructions) {
        this.program = new DiagnosticProgram();
        this.instructions = instructions;
        this.grid = createGrid();
    }

    private int[][] createGrid() {
        final int[][] grid = new int[MAX_SIZE][];

        for (int y = 0; y < grid.length; y++) {
            grid[y] = new int[MAX_SIZE];
            Arrays.fill(grid[y], 0);
        }

        return grid;
    }

    public void playGame() {
        DiagnosticProgram.State state = new DiagnosticProgram.State(instructions);
        while (!state.isFinished()) {
            program.execute(state);
        }

        final Point point = new Point();
        while (state.hasOutput()) {
            if (point.setValueFromOutput((int) state.getOutput())) {
                grid[point.getY()][point.getX()] = point.getTileId();
            }
        }

        printGrid();
    }

    public void printGrid() {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                System.out.print(grid[y][x]);
            }
            System.out.print("\n");
        }
    }

    public int[] countTiles() {
        final int[] numberOfTiles = new int[Tile.values().length];
        Arrays.fill(numberOfTiles, 0);

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                numberOfTiles[grid[y][x]]++;
            }
        }

        return numberOfTiles;
    }

    private class Point {
        @Getter
        private int x;
        @Getter
        private int y;
        @Getter
        private int tileId;

        private int outputIndex = 0;

        public boolean setValueFromOutput(int value) {
            switch (outputIndex++) {
                case 0:
                    this.x = value;
                    return false;
                case 1:
                    this.y = value;
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
