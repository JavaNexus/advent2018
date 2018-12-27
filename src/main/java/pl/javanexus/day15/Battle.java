package pl.javanexus.day15;

import java.util.*;
import java.util.stream.Collectors;

public class Battle {

    private final Board board;
    private final List<Unit> allUnits;
    private final Map<Unit.UnitType, List<Unit>> unitsByType;

    public Battle(Tile[][] tiles, List<Unit> allUnits) {
        this.board = new Board(tiles);
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

    public int getResult() {
        // TODO: 2018-12-23 while there are units with targets
        boolean hasAnyUnitMoved;
//        do {
//        } while (hasAnyUnitMoved);

        board.printMap();
        for (int i = 0; i < 3; i++) {
            hasAnyUnitMoved = executeTurn();
            board.printMap();
        }

        return -1;
    }

    private boolean executeTurn() {
        return allUnits.stream()
                .sorted()
                .map(this::executeTurn)
                .reduce(Boolean.FALSE, (left, right) -> left || right);
    }

    private boolean executeTurn(Unit unit) {
        List<Tile> enemiesInRange = board.getEnemyAdjacentTiles(unit);
        if (enemiesInRange.isEmpty()) {
            moveTowardNearestEnemy(unit);
            return true;
        } else {
            // TODO: 25.12.2018 attack enemy in range
            return false;
        }
    }

    private void moveTowardNearestEnemy(Unit unit) {
        final Tile tile = board.getTile(unit.getX(), unit.getY());

        final List<Tile> destinations = selectReachableDestinations(unit.getUnitType().getEnemyType());
        board.calculateDistance(tile, destinations).stream()
                .sorted()
                .findFirst()
                .ifPresent(selectedDestination -> unit.move(tile, selectedDestination.getFirstTileOnPath()));
    }

    private List<Tile> selectReachableDestinations(Unit.UnitType targetType) {
        return unitsByType.get(targetType).stream()
                .flatMap(enemy -> board.getEmptyAdjacentTiles(enemy.getX(), enemy.getY()).stream())
                .collect(Collectors.toList());
    }
}
