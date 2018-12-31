package pl.javanexus.day17;

import java.util.HashMap;
import java.util.Map;

public enum GroundType {

    SAND('.'),
    CLAY('#'),
    WATER('~');

    private final char symbol;

    GroundType(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    private static final Map<Character, GroundType> SYMBOLS = new HashMap<>();
    static {
        for (GroundType layerType : values()) {
            SYMBOLS.put(layerType.getSymbol(), layerType);
        }
    }

    public GroundType getBySymbol(char symbol) {
        return SYMBOLS.get(symbol);
    }
}
