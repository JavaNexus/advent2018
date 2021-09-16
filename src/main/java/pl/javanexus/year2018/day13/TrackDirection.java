package pl.javanexus.year2018.day13;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum TrackDirection {

    HORIZONTAL('-'),
    VERTICAL('|'),
    RIGHT('/'),
    LEFT('\\'),
    INTERSECTION('+');

    private static final Map<Character, TrackDirection> SYMBOLS = new HashMap<>();
    static {
        for (TrackDirection dir : values()) {
            SYMBOLS.put(dir.symbol, dir);
        }
    }

    @Getter
    private final char symbol;

    TrackDirection(char symbol) {
        this.symbol = symbol;
    }

    public static TrackDirection getDirection(char symbol) {
        return SYMBOLS.get(symbol);
    }
}
