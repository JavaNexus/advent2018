package pl.javanexus.year2019.day17;

import pl.javanexus.year2019.day5.DiagnosticProgram;

public class Scaffold {

    public static final int WIDTH = 38;
    public static final int HEIGHT = 45;

    public static final char SCAFFOLD = '#';
    public static final char EMPTY = '.';

    private final DiagnosticProgram program;
    private final char[][] grid = new char[HEIGHT][];

    public Scaffold() {
        program = new DiagnosticProgram();
    }

    public void traverseScaffold(long[] instructions) {
        DiagnosticProgram.State state = new DiagnosticProgram.State(instructions);
        while (!state.isFinished()) {
            program.execute(state);
        }
    }

    public void readInput(long[] instructions) {
        DiagnosticProgram.State state = new DiagnosticProgram.State(instructions);
        while (!state.isFinished()) {
            program.execute(state);
        }

        populateGrid(state);
    }

    private void populateGrid(DiagnosticProgram.State state) {
        int x = 0, y = 0;
        grid[y] = new char[WIDTH];
        while (state.hasOutput()) {
            char output = (char) state.getOutput();
            if (output == '\n') {
                x = 0;
                y++;
                if (y < HEIGHT) {
                    grid[y] = new char[WIDTH];
                }
            } else {
                grid[y][x++] = output;
            }

//            System.out.print(output);
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

    public int countIntersections() {
        int intersections = 0;

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (isIntersection(x, y)) {
                    intersections += x * y;
                    System.out.println("Intersection at: [" + x + ", " + y + "] " + intersections);
                }
            }
        }

        return intersections;
    }

    private boolean isIntersection(int x, int y) {
        return grid[y][x] == SCAFFOLD
                && y > 0 && y < HEIGHT - 1
                && x > 0 && x < WIDTH - 1
                && grid[y - 1][x] == SCAFFOLD
                && grid[y + 1][x] == SCAFFOLD
                && grid[y][x - 1] == SCAFFOLD
                && grid[y][x + 1] == SCAFFOLD;
    }
}
