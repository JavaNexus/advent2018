package pl.javanexus.day17;

import java.util.HashMap;
import java.util.Map;

public enum GroundType {

    SAND('.', 1, true),
    CLAY('#', 3, false),
    WATER_STILL('~', 3, false),
    WATER_FLOWING('|', 2, true);

    private final char symbol;
    private final int priority;
    private final boolean canWaterFlowThrough;

    GroundType(char symbol, int priority, boolean canWaterFlowThrough) {
        this.symbol = symbol;
        this.priority = priority;
        this.canWaterFlowThrough = canWaterFlowThrough;
    }

    public char getSymbol() {
        return symbol;
    }

    public int getPriority() {
        return priority;
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
