package pl.javanexus.year2019.day11;

import lombok.Data;
import lombok.Getter;
import pl.javanexus.year2019.day5.DiagnosticProgram;

import java.util.Arrays;

public class PaintingRobot {

    enum Direction {
        UP {
            @Override
            public Step turnLeft(int x, int y) {
                return new Step(x - 1, y, LEFT);
            }

            @Override
            public Step turnRight(int x, int y) {
                return new Step(x + 1, y, RIGHT);
            }
        },
        DOWN {
            @Override
            public Step turnLeft(int x, int y) {
                return new Step(x + 1, y, RIGHT);
            }

            @Override
            public Step turnRight(int x, int y) {
                return new Step(x - 1, y, LEFT);
            }
        },
        LEFT {
            @Override
            public Step turnLeft(int x, int y) {
                return new Step(x, y + 1, DOWN);
            }

            @Override
            public Step turnRight(int x, int y) {
                return new Step(x, y - 1, UP);
            }
        },
        RIGHT {
            @Override
            public Step turnLeft(int x, int y) {
                return new Step(x, y - 1, UP);
            }

            @Override
            public Step turnRight(int x, int y) {
                return new Step(x, y + 1, DOWN);
            }
        };

        public abstract Step turnLeft(int x, int y);
        public abstract Step turnRight(int x, int y);
    }

    //provide 0 if the robot is over a black panel or 1 if the robot is over a white panel
    public static final int COLOR_BLACK = 0;
    public static final int COLOR_WHITE = 1;
    //0 means it should turn left 90 degrees, and 1 means it should turn right 90 degrees
    public static final int TURN_LEFT = 0;
    public static final int TURN_RIGHT = 1;

    private final DiagnosticProgram program;
    private final Panel[][] hull;
    private long[] instructions;

    public PaintingRobot(int width, int height, long[] instructions) {
        this.program = new DiagnosticProgram();
        this.hull = createHull(width, height);
        this.instructions = instructions;
    }

    public void paintFromCenter() {
        paint(getStartingStep());
    }

    public Step getStartingStep() {
        int centerY = hull.length / 2;
        int centerX = hull[centerY].length / 2;
        return new Step(centerX, centerY, Direction.UP);
    }

    public void paintPanel(int x, int y, int color) {
        hull[y][x].paint(color);
    }

    public void paint(Step start) {
        int x = start.getX();
        int y = start.getY();
        Direction currentDirection = start.getDirection();

        final DiagnosticProgram.State state = new DiagnosticProgram.State(hull[y][x].getColor(), instructions);
        while (!state.isFinished()) {
            program.execute(state);
            hull[y][x].paint((int) state.getOutput());
            long turn = state.getOutput();
            Step nextStep = getNextStep(currentDirection, turn, x, y);
            currentDirection = nextStep.getDirection();
            x = nextStep.getX();
            y = nextStep.getY();
            state.addInput(hull[y][x].getColor());
        }
    }

    private Step getNextStep(Direction currentDirection, long turn, int x, int y) {
        if (turn == TURN_LEFT) {
            return currentDirection.turnLeft(x, y);
        } else if (turn == TURN_RIGHT) {
            return currentDirection.turnRight(x, y);
        } else {
            throw new RuntimeException("Unexpected turn: " + turn);
        }
    }

    public int countPanelsPaintedAtLeastOnce() {
        int numberOfPaintedPanels = 0;

        for (int y = 0; y < hull.length; y++) {
            for (int x = 0; x < hull[y].length; x++) {
                if (hull[y][x].getNumberOfPaintLayers() > 0) {
                    numberOfPaintedPanels++;
                }
            }
        }

        return numberOfPaintedPanels;
    }

    public int[] countPaintedPanelsByColor() {
        int[] numberOfPaintedPanels = {0, 0};

        for (int y = 0; y < hull.length; y++) {
            for (int x = 0; x < hull[y].length; x++) {
                Panel panel = hull[y][x];
                numberOfPaintedPanels[panel.getColor()]++;
            }
        }

        return numberOfPaintedPanels;
    }

    private Panel[][] createHull(int width, int height) {
        final Panel[][] hull = new Panel[height][];
        for (int y = 0; y < hull.length; y++) {
            hull[y] = new Panel[width];
            for (int x = 0; x < hull[y].length; x++) {
                hull[y][x] = new Panel(COLOR_BLACK);
            }
        }

        return hull;
    }

    public void printHull() {
        for (int y = 0; y < hull.length; y++) {
            for (int x = 0; x < hull[y].length; x++) {
                System.out.print(hull[y][x]);
            }
            System.out.print("\n");
        }
    }

    public static class Panel {

        @Getter
        private int color;
        @Getter
        private int numberOfPaintLayers;

        public Panel(int color) {
            this.color = color;
            this.numberOfPaintLayers = 0;
        }

        public void paint(int color) {
            this.color = color;
            this.numberOfPaintLayers++;
        }

        @Override
        public String toString() {
            return color == COLOR_BLACK ? " " : "#";
        }
    }

    @Data
    public static class Step {
        private final int x;
        private final int y;
        private final Direction direction;
    }
}
