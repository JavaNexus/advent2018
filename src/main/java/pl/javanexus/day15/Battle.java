package pl.javanexus.day15;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Battle {

    private final Tile[][] board;
    private final List<Unit> allUnits;
    private final Map<Unit.UnitType, List<Unit>> unitsByType;

    public Battle(Tile[][] board, List<Unit> allUnits) {
        this.board = board;
        this.allUnits = allUnits;
        this.unitsByType = new HashMap<>();
        for (Unit unit : allUnits) {
            unitsByType.compute(unit.getUnitType(), (type, units) -> {
                if (units == null) {
                    units = new LinkedList<>();
                }
                units.add(unit);

                return units;
            });
        }
    }

    public void printMap() {
        iterateOverTracks(
                (x, y) -> System.out.printf("%s", board[x][y]),
                (y) -> System.out.printf("\n"));
    }

    private void iterateOverTracks(BiConsumer<Integer, Integer> cellConsumer, Consumer<Integer> rowConsumer) {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                cellConsumer.accept(x, y);
            }
            rowConsumer.accept(x);
        }
    }

    public int getResult() {
        // TODO: 2018-12-23 while there are units with targets
        executeTurn();
        return -1;
    }

    private void executeTurn() {
        allUnits.stream().sorted((unit1, unit2) -> {
            int dy = unit1.getY() - unit2.getY();
            return dy == 0 ?unit1.getX() - unit2.getX() : dy;
        }).forEach(unit -> executeTurn(unit));
    }

    private void executeTurn(Unit unit) {
        //isInRange()
        selectReachableDestinations(board[unit.getX()][unit.getY()], unit.getUnitType().getEnemy());
    }

    private List<Tile> selectReachableDestinations(Tile unitTile, Unit.UnitType targetType) {
        return unitsByType.get(targetType).stream()
                .flatMap(enemy -> getEmptyAdjacentTiles(enemy.getX(), enemy.getY()).stream())
                .filter(enemyAdjacentTile -> isReachable(unitTile, enemyAdjacentTile))
                .collect(Collectors.toList());
    }

    private boolean isReachable(Tile from, Tile to) {
        // TODO: 2018-12-23 implement A*
        throw new RuntimeException("Not implemented yet");
    }

    private List<Tile> getEmptyAdjacentTiles(int ox, int oy) {
        List<Tile> tiles = new LinkedList<>();
        for (int y = getPrevious(oy, 0); y < getNext(oy, board.length - 1); y++) {
            for (int x = getPrevious(ox, 0); x < getNext(ox, board.length - 1); x++) {
                if (board[x][y].isEmpty()) {
                    tiles.add(board[x][y]);
                }
            }
        }

        return tiles;
    }

    private int getPrevious(int center, int minValue) {
        return center == minValue ? minValue : center - 1;
    }

    private int getNext(int center, int maxValue) {
        return center == maxValue ? center = maxValue : center + 1;
    }
}
