package pl.javanexus.day15;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Battle {

    private final Tile[][] board;
    private final List<Unit> allUnits;
    private final Map<Unit.UnitType, List<Unit>> unitsByType;

    private final int maxDistance;
    private final int minDistance;

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
        this.minDistance = 0;
        this.maxDistance = board.length * board.length + 1;
    }

    public void printMap() {
        iterateOverBoard(
                (x, y) -> System.out.printf("%s", board[x][y].getSymbol()),
                (y) -> System.out.printf("\n"));
    }

    private void iterateOverBoard(BiConsumer<Integer, Integer> cellConsumer, Consumer<Integer> rowConsumer) {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                cellConsumer.accept(x, y);
            }
            rowConsumer.accept(x);
        }
    }

    public int getResult() {
        // TODO: 2018-12-23 while there are units with targets
        boolean hasAnyUnitMoved;
//        do {
//        } while (hasAnyUnitMoved);

        printMap();
        for (int i = 0; i < 2; i++) {
            hasAnyUnitMoved = executeTurn();
            printMap();
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
        List<Tile> enemiesInRange = getEnemyAdjacentTiles(unit);
        if (enemiesInRange.isEmpty()) {
            moveTowardNearestEnemy(unit);
            return true;
        } else {
            // TODO: 25.12.2018 attack enemy in range
            return false;
        }
    }

    private void moveTowardNearestEnemy(Unit unit) {
        final Tile tile = getTile(unit.getX(), unit.getY());

        final List<Tile> destinations = selectReachableDestinations(unit.getUnitType().getEnemyType());
        calculateDistance(tile, destinations).stream()
                .sorted()
                .findFirst()
                .ifPresent(selectedDestination -> unit.move(tile, selectedDestination.getFirstTileOnPath()));
    }

    private List<Tile> selectReachableDestinations(Unit.UnitType targetType) {
        return unitsByType.get(targetType).stream()
                .flatMap(enemy -> getEmptyAdjacentTiles(enemy.getX(), enemy.getY()).stream())
                .collect(Collectors.toList());
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= board.length || y >= board.length) {
            String exceptionMessage = String.format("Point [%d, %d] is outside the board: %d", x, y, board.length);
            throw new IllegalArgumentException(exceptionMessage);
        }

        return board[y][x];
    }

    public List<PathTile> calculateDistance(Tile from, List<Tile> to) {
        final List<PathTile> unvisitedTiles = getPathTiles(from.getX(), from.getY());
        final List<PathTile> visitedTargets = new ArrayList<>();
        final Map<Tile, PathTile> mapping = unvisitedTiles.stream()
                .collect(Collectors.toMap(PathTile::getTile, pathTile -> pathTile));

        boolean hasVisitedAllReachableTile = false;
        while (!hasVisitedAllReachableTile) {
            PathTile currentPathTile = unvisitedTiles.stream()
                    .min(Comparator.comparingInt(PathTile::getDistance))
                    .get();
            Tile currentTile = currentPathTile.getTile();
            List<Tile> nextStepsOnPath = getEmptyAdjacentTiles(currentTile.getX(), currentTile.getY());
            nextStepsOnPath.stream()
                    .map(mapping::get)
                    .filter(pathTile -> !pathTile.isVisited())
                    .forEach(pathTile -> {
                        int newDistance = currentPathTile.getDistance() + 1;
                        if (newDistance < pathTile.getDistance()) {
                            pathTile.setDistance(newDistance);
                        }
                    });

            unvisitedTiles.remove(currentPathTile);
            currentPathTile.setVisited(true);
            if (to.contains(currentTile) && currentPathTile.getDistance() < maxDistance) {
                visitedTargets.add(currentPathTile);
            }

            hasVisitedAllReachableTile = unvisitedTiles.isEmpty() || nextStepsOnPath.isEmpty();
        }

        for (PathTile target : visitedTargets) {
            populatePath(target, mapping);
        }

        return visitedTargets;
    }

    private void populatePath(PathTile to, Map<Tile, PathTile> mapping) {
        PathTile nextTileOnPath = to;
        while (nextTileOnPath.getDistance() > minDistance) {
            Tile tile = nextTileOnPath.getTile();
            nextTileOnPath = getAdjacentTiles(tile.getX(), tile.getY(),
                    (adjacentTile) -> adjacentTile.isEmpty() || Optional.ofNullable(mapping.get(adjacentTile))
                            .map(PathTile::getDistance)
                            .filter(distance -> distance == minDistance).isPresent()
                    ).stream()
                    .map(mapping::get)
                    .min(Comparator.comparingInt(PathTile::getDistance))
                    .get();
            to.getPath().add(nextTileOnPath.getTile());
        }
    }

    private List<PathTile> getPathTiles(int srcX, int srcY) {
        List<PathTile> unvisitedTiles = new ArrayList<>(board.length * board.length);
        iterateOverBoard(
                (x, y) -> {
                    Tile tile = getTile(x, y);
                    if (tile.isEmpty()) {
                        unvisitedTiles.add(new PathTile(tile, maxDistance));
                    } else if (x == srcX && y == srcY) {
                        unvisitedTiles.add(new PathTile(tile, minDistance));
                    }
                },
                (y) -> {});

        return unvisitedTiles;
    }

    private List<Tile> getEmptyAdjacentTiles(int ox, int oy) {
        return getAdjacentTiles(ox, oy, Tile::isEmpty);
    }

    private List<Tile> getEnemyAdjacentTiles(Unit selectedUnit) {
        return getAdjacentTiles(selectedUnit.getX(), selectedUnit.getY(),
                (tile) -> Optional.ofNullable(tile.getUnit())
                        .filter(unit -> unit.getUnitType() == selectedUnit.getUnitType().getEnemyType())
                        .isPresent());
    }

    private List<Tile> getAdjacentTiles(int ox, int oy, Predicate<Tile> filter) {
        List<Tile> tiles = new LinkedList<>();
        getTileInRange(ox - 1, oy).filter(filter).ifPresent(tiles::add);
        getTileInRange(ox + 1, oy).filter(filter).ifPresent(tiles::add);
        getTileInRange(ox, oy - 1).filter(filter).ifPresent(tiles::add);
        getTileInRange(ox, oy + 1).filter(filter).ifPresent(tiles::add);

        return tiles;
    }

    private Optional<Tile> getTileInRange(int x, int y) {
        if (x >= 0 && x < board.length && y >= 0 && y < board.length) {
            return Optional.of(getTile(x, y));
        } else {
            return Optional.empty();
        }
    }
}
