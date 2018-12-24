package pl.javanexus.day13;

import lombok.Getter;

import java.util.*;

public enum CartDirection {

    NORTH('^', TrackDirection.VERTICAL, 0, -1),
    SOUTH('v', TrackDirection.VERTICAL, 0, 1),
    EAST('>', TrackDirection.HORIZONTAL, 1, 0),
    WEST('<', TrackDirection.HORIZONTAL, -1, 0);

    private static final Map<Character, CartDirection> SYMBOLS = new HashMap<>();
    static {
        for (CartDirection dir : values()) {
            SYMBOLS.put(dir.symbol, dir);
        }

        NORTH.addTransition(TrackDirection.VERTICAL, CartDirection.NORTH);
        NORTH.addTransition(TrackDirection.LEFT, CartDirection.WEST);
        NORTH.addTransition(TrackDirection.RIGHT, CartDirection.EAST);

        SOUTH.addTransition(TrackDirection.VERTICAL, CartDirection.SOUTH);
        SOUTH.addTransition(TrackDirection.LEFT, CartDirection.EAST);
        SOUTH.addTransition(TrackDirection.RIGHT, CartDirection.WEST);

        EAST.addTransition(TrackDirection.HORIZONTAL, CartDirection.EAST);
        EAST.addTransition(TrackDirection.LEFT, CartDirection.SOUTH);
        EAST.addTransition(TrackDirection.RIGHT, CartDirection.NORTH);

        WEST.addTransition(TrackDirection.HORIZONTAL, CartDirection.WEST);
        WEST.addTransition(TrackDirection.LEFT, CartDirection.NORTH);
        WEST.addTransition(TrackDirection.RIGHT, CartDirection.SOUTH);

        //TURNS:
        NORTH.addTurn(Turn.LEFT, CartDirection.WEST);
        NORTH.addTurn(Turn.STRAIGHT, CartDirection.NORTH);
        NORTH.addTurn(Turn.RIGHT, CartDirection.EAST);

        SOUTH.addTurn(Turn.LEFT, CartDirection.EAST);
        SOUTH.addTurn(Turn.STRAIGHT, CartDirection.SOUTH);
        SOUTH.addTurn(Turn.RIGHT, CartDirection.WEST);

        EAST.addTurn(Turn.LEFT, CartDirection.NORTH);
        EAST.addTurn(Turn.STRAIGHT, CartDirection.EAST);
        EAST.addTurn(Turn.RIGHT, CartDirection.SOUTH);

        WEST.addTurn(Turn.LEFT, CartDirection.SOUTH);
        WEST.addTurn(Turn.STRAIGHT, CartDirection.WEST);
        WEST.addTurn(Turn.RIGHT, CartDirection.NORTH);
    }

    @Getter
    private final char symbol;

    @Getter
    private final TrackDirection trackDirection;

    @Getter
    private final int dx;

    @Getter
    private final int dy;

    private final Map<TrackDirection, CartDirection> directionTransitions;
    private final Map<Turn, CartDirection> turnsOnIntersection;

    CartDirection(char symbol, TrackDirection trackDirection, int dx, int dy) {
        this.symbol = symbol;
        this.trackDirection = trackDirection;
        this.dx = dx;
        this.dy = dy;
        this.directionTransitions = new HashMap<>();
        this.turnsOnIntersection = new HashMap<>();
    }

    private void addTransition(TrackDirection trackDirection, CartDirection cartDirection) {
        this.directionTransitions.put(trackDirection, cartDirection);
    }

    private void addTurn(Turn turn, CartDirection cartDirection) {
        this.turnsOnIntersection.put(turn, cartDirection);
    }

    public CartDirection getNextCartDirection(TrackDirection trackDirection) {
        return directionTransitions.get(trackDirection);
    }

    public CartDirection getCartDirectionAfterIntersection(int numberOfIntersectionsVisited) {
        Turn nextTurn = Turn.values()[numberOfIntersectionsVisited % Turn.values().length];
        return turnsOnIntersection.get(nextTurn);
    }

    public static CartDirection getDirection(char symbol) {
        return SYMBOLS.get(symbol);
    }
}
