package pl.javanexus.year2019.day13;

import lombok.Getter;
import pl.javanexus.year2019.day5.DiagnosticProgram;

import java.io.*;
import java.util.Arrays;

public class CarePackage {

    public static final int GRID_HEIGHT = 25;
    public static final int GRID_WIDTH = 40;

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

    public static void main(String[] args) throws IOException {
        long[] instructions = getInstructions("./src/test/resources/year2019/day13/input1.csv", ",");
        CarePackage carePackage = new CarePackage(instructions);
        carePackage.setNumberOfQuarters(2);
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
        Console console = System.console();

        DiagnosticProgram.State state = new DiagnosticProgram.State(instructions);
        while (!state.isFinished()) {
            program.execute(state);
            updateGrid(state);
            printGrid(console.writer());

            long input = Long.parseLong(console.readLine().trim());
            state.addInput(input);
        }
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
        final Point point = new Point();
        while (state.hasOutput()) {
            if (point.setValueFromOutput((int) state.getOutput())) {
                if (point.containsScore()) {
                    System.out.println("Score: " + point.getTileId());
                } else {
                    grid[point.getY()][point.getX()] = point.getTileId();
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
        final int[] numberOfTiles = new int[Tile.values().length];
        Arrays.fill(numberOfTiles, 0);

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                numberOfTiles[grid[y][x]]++;
            }
        }

        return numberOfTiles;
    }

    public void setNumberOfQuarters(int quarters) {
        instructions[0] = quarters;
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

        public boolean containsScore() {
            return x == -1 && y == 0;
        }
    }
}
