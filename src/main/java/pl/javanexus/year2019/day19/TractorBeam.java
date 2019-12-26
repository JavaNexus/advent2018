package pl.javanexus.year2019.day19;

import pl.javanexus.year2019.day5.DiagnosticProgram;

public class TractorBeam {

    public static final int STATIC = 0;
    public static final int MOVING = 1;

    private final DiagnosticProgram program;
    private final long[] instructions;

    public TractorBeam(long[] instructions) {
        this.program = new DiagnosticProgram();
        this.instructions = instructions;
    }

    public int[] findTractorBeamArea(int ox, int oy, int width, int height) {
        final int[] types = new int[2];

        for (int y = oy; y < oy + height; y++) {
            double tgLeft = 0, tgRight = 0;
            for (int x = ox; x < ox + width; x++) {
                if (checkIfFitsInTractorBeam(x, y, 100)) {
                    System.out.println("Start at: " + x + ", " + y + " -> " + (10000 * x + y));
                }
                int status = getStatus(x, y);
                types[status]++;

                if (tgLeft == 0 && status == MOVING) {
                    tgLeft = (double)x / (double)y;
                } else if (tgLeft > 0 && tgRight == 0 && status == STATIC) {
                    tgRight = (double)x / (double)y;
                }
//                System.out.print(status);
            }
//            System.out.print("\n");
//            System.out.println(y + ", LEFT: " + tgLeft + ", RIGHT: " + tgRight);
        }

        return types;
    }

    public boolean checkIfFitsInTractorBeam(int ox, int oy, int r) {
        int[][] corners = {
                {ox, oy},
                {ox + r, oy},
                {ox, oy + r},
                {ox + r, oy + r},
        };

        boolean fits = true;
        for (int[] corner : corners) {
            int x = corner[0];
            int y = corner[1];
            int status = getStatus(x, y);
//            System.out.println(x + ", " + y + " -> " + status);
            fits &= status == MOVING;
        }

        return fits;
    }

    private int getStatus(int x, int y) {
        final DiagnosticProgram.State state = new DiagnosticProgram.State(instructions);
        state.addInput(x);
        state.addInput(y);

        program.execute(state);
        return (int) state.getOutput();
    }
}
