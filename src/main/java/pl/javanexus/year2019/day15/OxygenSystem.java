package pl.javanexus.year2019.day15;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import pl.javanexus.year2019.Grid;
import pl.javanexus.year2019.Point;
import pl.javanexus.year2019.day5.DiagnosticProgram;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OxygenSystem {

    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;

    //north (1), south (2), west (3), and east (4)
    enum Move {
        NORTH(1) {
            @Override
            public Point getPosition(Point droidPosition) {
                return new Point(droidPosition.getX(), droidPosition.getY() - 1);
            }
        },
        SOUTH(2) {
            @Override
            public Point getPosition(Point droidPosition) {
                return new Point(droidPosition.getX(), droidPosition.getY() + 1);
            }
        },
        WEST(3) {
            @Override
            public Point getPosition(Point droidPosition) {
                return new Point(droidPosition.getX() - 1, droidPosition.getY());
            }
        },
        EAST(4) {
            @Override
            public Point getPosition(Point droidPosition) {
                return new Point(droidPosition.getX() + 1, droidPosition.getY());
            }
        };

        @Getter
        private final int input;

        Move(int input) {
            this.input = input;
        }

        public abstract Point getPosition(Point droidPosition);
    }

    @AllArgsConstructor
    public enum TileType {
        WALL(0, 0, true),
        EMPTY(1, 2, false),
        OXYGEN_TANK(2, 1, false),
        UNKNOWN(3, 3, false);

        private static final TileType[] INDEX;
        static {
            TileType[] allTypes = TileType.values();
            INDEX = new TileType[allTypes.length];
            for (TileType type : allTypes) {
                INDEX[type.getStatus()] = type;
            }
        }

        public static TileType getType(int status) {
            return INDEX[status];
        }

        @Getter
        private final int status;
        @Getter
        private final int priority;
        @Getter
        private final boolean blocking;
    }

    private final DiagnosticProgram program = new DiagnosticProgram();
    private final Grid grid = new Grid(WIDTH, HEIGHT, TileType.UNKNOWN.getStatus());
    private final Point droidPosition = new Point(WIDTH / 2, HEIGHT / 2);

    public void findOxygenTank(long[] instructions) {
        grid.setTileType(droidPosition, TileType.EMPTY.getStatus());

        DiagnosticProgram.State state = new DiagnosticProgram.State(instructions);

        Move nextMove = Move.NORTH;
        state.addInput(nextMove.getInput());

        while(!state.isFinished()) {
            program.execute(state);
            if (state.hasOutput()) {
                Point nextPosition = nextMove.getPosition(droidPosition);

                int movementStatus = (int) state.getOutput();
                grid.setTileType(nextPosition, movementStatus);

                TileType tileType = TileType.getType(movementStatus);
                if (!tileType.isBlocking()) {
                    droidPosition.setCoordinates(nextPosition);
                    grid.addVisit(droidPosition);
                }
                if (tileType == TileType.OXYGEN_TANK) {
                    System.out.println(" >: Found oxygen tank at: " + droidPosition);
                }

                nextMove = getNextMove();
                System.out.println("Move: " + nextMove + " from: " + droidPosition);
                state.addInput(nextMove.getInput());
            }
            grid.printGrid();
        }
    }

    private Move getNextMove() {
        List<Tile> availableMoves = getAvailableMoves();
        return availableMoves.get(availableMoves.size() - 1).getMove();

//        for (Move move : Move.values()) {
//            Point nextPosition = move.getPosition(droidPosition);
//            // TODO: 2019-12-15 find first unknown
//            if (!getTileType(nextPosition).isBlocking()) {
//                return move;
//            }
//        }

//        throw new RuntimeException("Dead end at position: " + droidPosition);
    }

    private List<Tile> getAvailableMoves() {
        return Arrays.stream(Move.values())
                .map(move -> {
                    Point position = move.getPosition(droidPosition);
                    return new Tile(position, getTileType(position), move);
                })
                .sorted((left, right) -> {
                    if (left.getPriority() == right.getPriority()) {
                        return grid.getNumberOfVisits(right.getPosition())
                                - grid.getNumberOfVisits(left.getPosition());
                    } else {
                        return left.getPriority() - right.getPriority();
                    }
                })
                .collect(Collectors.toList());
    }

    private TileType getTileType(Point point) {
        return TileType.getType(grid.getTileType(point));
    }

    @Data
    private static class Tile {

        private final Point position;
        private final TileType type;
        private final Move move;

        public int getPriority() {
            return type.getPriority();//type.getStatus()
        }
    }
}
