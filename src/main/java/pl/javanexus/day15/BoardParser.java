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
            public Tile createTile() {
                return new Tile(new Unit(Unit.UnitType.ELF), getSymbol());
            }
        },
        GOBLIN('G') {
            @Override
            public Tile createTile() {
                return new Tile(new Unit(Unit.UnitType.GOBLIN), getSymbol());
            }
        },
        ROCK('#') {
            @Override
            public Tile createTile() {
                return new Tile(true, getSymbol());
            }
        },
        EMPTY('.') {
            @Override
            public Tile createTile() {
                return new Tile(false, getSymbol());
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

        public abstract Tile createTile();
    }

    private final InputReader inputReader = new InputReader();

    @Getter
    private List<Unit> allUnits;

    @Getter
    private Tile[][] board;

    public void parseInput(String fileName) throws IOException {
        this.allUnits = new LinkedList<>();

        List<Tile[]> tiles = inputReader.readValues(fileName, (index, line) -> {
            Tile[] row = new Tile[line.length()];
            char[] symbols = line.toCharArray();
            for (int i = 0; i < symbols.length; i++) {
                row[i] = TileSymbol.getTileSymbol(symbols[i]).createTile();
                Unit unit = row[i].getUnit();
                if (unit != null) {
                    unit.setX(i);
                    unit.setY(index);

                    allUnits.add(unit);
                }
            }

            return row;
        });

        this.board = tiles.toArray(new Tile[tiles.size()][]);
    }
}
