package pl.javanexus.day17;

import java.util.HashMap;
import java.util.Map;

public enum GroundType {

    SAND('.', true),
    CLAY('#', false),
    WATER_STILL('~', false),
    WATER_FLOWING('|', false);

    private final char symbol;
    private final boolean canWaterFlowThrough;

    GroundType(char symbol, boolean canWaterFlowThrough) {
        this.symbol = symbol;
        this.canWaterFlowThrough = canWaterFlowThrough;
    }

    public char getSymbol() {
        return symbol;
    }

    public boolean canWaterFlowThrough() {
        return canWaterFlowThrough;
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
