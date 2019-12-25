package pl.javanexus.year2019.day19;

import pl.javanexus.year2019.day5.DiagnosticProgram;

import java.util.Arrays;

public class TractorBeam {

    public static final int STATIC = 0;
    public static final int MOVING = 1;

    private final DiagnosticProgram program;
    private final long[] instructions;
    private final int[][] grid;

    public TractorBeam(int width, int height, long[] instructions) {
        this.program = new DiagnosticProgram();
        this.grid = createGrid(width, height);
        this.instructions = instructions;
    }

    private int[][] createGrid(int width, int height) {
        int[][] newGrid = new int[height][];
        for (int y = 0; y < newGrid.length; y++) {
            newGrid[y] = new int[width];
            Arrays.fill(newGrid[y], 0);
        }

        return newGrid;
    }

    public void findTractorBeamArea() {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                final DiagnosticProgram.State state = new DiagnosticProgram.State(instructions);
                state.addInput(x);
                state.addInput(y);

                program.execute(state);
                if (state.hasOutput()) {
                    int status = (int) state.getOutput();
                    grid[y][x] = status;
                }
            }
        }
    }

    public int[] countFieldsByType() {
        int[] types = new int[2];
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                int type = grid[y][x];
                types[type]++;
            }
        }

        return types;
    }

    public void printGrid() {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                System.out.print(grid[y][x]);
            }
            System.out.print("\n");
        }
    }
}
