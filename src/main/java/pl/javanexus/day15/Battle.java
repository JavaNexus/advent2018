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
        board.printMap();

        int turn = 0;
        do {
            executeTurn();
            System.out.println(" >>> " + turn);
            board.printMap();

            turn++;
        } while (haveAllUnitsOfAtLeastOnTypeDied());

        return turn * getHpOfAliveUnits();
    }

    private boolean executeTurn() {
        return allUnits.stream()
                .sorted()
                .map(this::executeTurn)
                .reduce(Boolean.FALSE, (left, right) -> left || right);
    }

    private boolean executeTurn(Unit unit) {
        boolean hasExecutedAnyAction = false;

        if (unit.isAlive()) {
            List<Tile> enemiesInRange = board.getEnemyAdjacentTiles(unit);
            if (enemiesInRange.isEmpty()) {
                moveTowardNearestEnemy(unit);
                enemiesInRange = board.getEnemyAdjacentTiles(unit);
            }

            if (!enemiesInRange.isEmpty()) {
                Unit selectedEnemy = selectEnemy(enemiesInRange);
                boolean hasKilledEnemy = unit.attack(selectedEnemy);
                if (hasKilledEnemy) {
                    Tile selectedEnemyTile = board.getTile(selectedEnemy.getX(), selectedEnemy.getY());
                    selectedEnemyTile.setUnit(null);
                }
            }
        }

        return hasExecutedAnyAction;
    }

    private Unit selectEnemy(List<Tile> enemiesInRange) {
        Unit selectedEnemy = null;
        for (Tile tileWithEnemy : enemiesInRange) {
            Unit possibleEnemy = tileWithEnemy.getUnit();
            if (selectedEnemy == null || possibleEnemy.getHp() < selectedEnemy.getHp() ||
                    (possibleEnemy.getHp() == selectedEnemy.getHp() && possibleEnemy.compareTo(selectedEnemy) < 0)) {
                selectedEnemy = possibleEnemy;
            }
        }

        return selectedEnemy;
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
                .filter(Unit::isAlive)
                .flatMap(enemy -> board.getEmptyAdjacentTiles(enemy.getX(), enemy.getY()).stream())
                .collect(Collectors.toList());
    }

    private boolean haveAllUnitsOfAtLeastOnTypeDied() {
        return isAnyAlive(Unit.UnitType.ELF) && isAnyAlive(Unit.UnitType.GOBLIN);
    }

    private boolean isAnyAlive(Unit.UnitType unitType) {
        return unitsByType.get(unitType).stream().filter(Unit::isAlive).findAny().isPresent();
    }

    private int getHpOfAliveUnits() {
        return allUnits.stream().filter(Unit::isAlive).mapToInt(Unit::getHp).sum();
    }
}
