package pl.javanexus.year2018.day15;

import lombok.Getter;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BoardParser {

    enum TileSymbol {
        ELF('E') {
            @Override
            public Optional<Unit.UnitType> getUnitType() {
                return Optional.of(Unit.UnitType.ELF);
            }
        },
        GOBLIN('G') {
            @Override
            public Optional<Unit.UnitType> getUnitType() {
                return Optional.of(Unit.UnitType.GOBLIN);
            }
        },
        ROCK('#', true),
        EMPTY('.');

        private static final Map<Character, TileSymbol> SYMBOLS = new HashMap<>();
        static {
            for (TileSymbol tileSymbol : values()) {
                SYMBOLS.put(tileSymbol.getSymbol(), tileSymbol);
            }
        }

        public static TileSymbol getTileSymbol(char symbol) {
            return SYMBOLS.get(symbol);
        }

        private final char symbol;
        private final boolean isRock;

        TileSymbol(char symbol) {
            this(symbol, false);
        }

        TileSymbol(char symbol, boolean isRock) {
            this.symbol = symbol;
            this.isRock = isRock;
        }

        public char getSymbol() {
            return symbol;
        }

        public Optional<Unit.UnitType> getUnitType() {
            return Optional.empty();
        }

        public Tile createTile(int x, int y) {
            return new Tile(x, y, isRock);
        }
    }

    private final InputReader inputReader = new InputReader();
    private final Map<Unit.UnitType, UnitFactory> unitFactory;

    public BoardParser(UnitFactory... unitFactories) {
        this.unitFactory = Arrays.stream(unitFactories)
                .collect(Collectors.toMap(factory -> factory.getUnitType(), factory -> factory));
        for (Unit.UnitType unitType : Unit.UnitType.values()) {
            unitFactory.computeIfAbsent(unitType, (type) -> new UnitFactory(type));
        }
    }

    @Getter
    private List<Unit> allUnits;

    @Getter
    private Tile[][] board;

    public void parseInput(String fileName) throws IOException {
        this.allUnits = new LinkedList<>();

        List<Tile[]> tiles = inputReader.readValues(fileName, (y, line) -> {
            Tile[] row = new Tile[line.length()];
            char[] symbols = line.toCharArray();
            for (int x = 0; x < symbols.length; x++) {
                TileSymbol tileSymbol = TileSymbol.getTileSymbol(symbols[x]);
                row[x] = tileSymbol.createTile(x, y);

                if (tileSymbol.getUnitType().isPresent()) {
                    UnitFactory unitFactory = this.unitFactory.get(tileSymbol.getUnitType().get());
                    Unit unit = unitFactory.createUnit(x, y);

                    allUnits.add(unit);
                    row[x].setUnit(unit);
                }
            }

            return row;
        });

        this.board = tiles.toArray(new Tile[tiles.size()][]);
    }
}
