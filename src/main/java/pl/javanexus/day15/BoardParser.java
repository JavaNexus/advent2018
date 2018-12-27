package pl.javanexus.day15;

import lombok.Getter;
import pl.javanexus.InputReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BoardParser {

    enum TileSymbol {
        ELF('E') {
            @Override
            public Tile createTile(int x, int y) {
                return new Tile(x, y, new Unit(Unit.UnitType.ELF, x, y));
            }
        },
        GOBLIN('G') {
            @Override
            public Tile createTile(int x, int y) {
                return new Tile(x, y, new Unit(Unit.UnitType.GOBLIN, x, y));
            }
        },
        ROCK('#') {
            @Override
            public Tile createTile(int x, int y) {
                return new Tile(x, y, true);
            }
        },
        EMPTY('.') {
            @Override
            public Tile createTile(int x, int y) {
                return new Tile(x, y, false);
            }
        };

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

        TileSymbol(char symbol) {
            this.symbol = symbol;
        }

        public char getSymbol() {
            return symbol;
        }

        public abstract Tile createTile(int x, int y);
    }

    private final InputReader inputReader = new InputReader();

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
                row[x] = TileSymbol.getTileSymbol(symbols[x]).createTile(x, y);
                Unit unit = row[x].getUnit();
                if (unit != null) {
                    allUnits.add(unit);
                }
            }

            return row;
        });

        this.board = tiles.toArray(new Tile[tiles.size()][]);
    }
}
