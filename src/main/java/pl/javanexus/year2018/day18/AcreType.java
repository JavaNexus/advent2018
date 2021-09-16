package pl.javanexus.year2018.day18;

import java.util.*;
import java.util.stream.Collectors;

public enum AcreType {

    OPEN('.') {
        private final int MIN_ADJACENT_TREES = 3;

        @Override
        public AcreType getNextGroundType(List<AcreType> adjacentTypes) {
            return countType(adjacentTypes, TREES) >= MIN_ADJACENT_TREES ? TREES : OPEN;
        }
    },
    TREES('|') {
        private final int MIN_ADJACENT_LUMBERYARDS = 3;

        @Override
        public AcreType getNextGroundType(List<AcreType> adjacentTypes) {
            return countType(adjacentTypes, LUMBERYARD) >= MIN_ADJACENT_LUMBERYARDS ? LUMBERYARD : TREES;
        }
    },
    LUMBERYARD('#') {
        private final int MIN_ADJACENT_TREES = 1;
        private final int MIN_ADJACENT_LUMBERYARDS = 1;

        @Override
        public AcreType getNextGroundType(List<AcreType> adjacentTypes) {
            return countType(adjacentTypes, LUMBERYARD) >= MIN_ADJACENT_LUMBERYARDS
                    && countType(adjacentTypes, TREES) >= MIN_ADJACENT_TREES ? LUMBERYARD : OPEN;
        }
    };

    private static final Map<Character, AcreType> BY_SYMBOL = Arrays.stream(values())
            .collect(Collectors.toMap(AcreType::getSymbol, type -> type));

    private final char symbol;

    AcreType(char symbol) {
        this.symbol = symbol;
    }

    public static AcreType getBySymbol(char symbol) {
        return Optional
                .ofNullable(BY_SYMBOL.get(symbol))
                .orElseThrow(() -> new IllegalArgumentException("Unexpected symbol: " + symbol));
    }

    private static long countType(List<AcreType> adjacentTypes, AcreType expectedType) {
        return adjacentTypes.stream().filter(type -> type == expectedType).count();
    }

    public abstract AcreType getNextGroundType(List<AcreType> adjacentTypes);

    public char getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return Character.toString(symbol);
    }

}
