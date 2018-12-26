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
        iterateOverTracks(
                (x, y) -> System.out.printf("%s", board[x][y].getSymbol()),
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
            return dy == 0 ? unit1.getX() - unit2.getX() : dy;
        }).forEach(unit -> executeTurn(unit));
    }

    private void executeTurn(Unit unit) {
        List<Tile> enemiesInRange = getEnemyAdjacentTiles(unit);
        if (enemiesInRange.isEmpty()) {
            moveTowardNearestEnemy(unit);
        }
        // TODO: 25.12.2018 attack enemy in range
    }

    private void moveTowardNearestEnemy(Unit unit) {
        final Tile tile = board[unit.getX()][unit.getY()];

        List<Tile> destinations = selectReachableDestinations(tile, unit.getUnitType().getEnemyType());
        calculateDistance(tile, destinations).stream()
                .sorted()
                .findFirst().ifPresent(selectedDestination -> {
            unit.move(tile, selectedDestination.getTile());
        });
    }

    private List<Tile> selectReachableDestinations(Tile unitTile, Unit.UnitType targetType) {
        return unitsByType.get(targetType).stream()
                .flatMap(enemy -> getEmptyAdjacentTiles(enemy.getX(), enemy.getY()).stream())
                .filter(enemyAdjacentTile -> isReachable(unitTile, enemyAdjacentTile))
                .collect(Collectors.toList());
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= board.length || y >= board.length) {
            String exceptionMessage = String.format("Point [%d, %d] is outside the board: %d", x, y, board.length);
            throw new IllegalArgumentException(exceptionMessage);
        }

        return board[x][y];
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
            if (to.contains(currentTile)) {
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
        Optional<PathTile> nextTileOnPath = Optional.of(to);
        while (nextTileOnPath.isPresent()) {
            Tile tile = nextTileOnPath.map(PathTile::getTile).get();
            nextTileOnPath = getAdjacentTiles(tile.getY(), tile.getX()).stream()
                    .map(mapping::get)
                    .min(Comparator.comparingInt(PathTile::getDistance));
            nextTileOnPath
                    .ifPresent(pathTile -> to.getPath().add(pathTile.getTile()));
        }
    }

    private List<PathTile> getPathTiles(int srcX, int srcY) {
        List<PathTile> unvisitedTiles = new ArrayList<>(board.length * board.length);
        iterateOverTracks(
                (x, y) -> {
                    Tile tile = board[x][y];
                    if (tile.isEmpty()) {
                        unvisitedTiles.add(new PathTile(tile, maxDistance));
                    } else if (x == srcX && y == srcY) {
                        unvisitedTiles.add(new PathTile(tile, minDistance));
                    }
                },
                (y) -> {});

        return unvisitedTiles;
    }

    private boolean isReachable(Tile from, Tile to) {
        // TODO: 2018-12-23 implement path finding
        throw new RuntimeException("Not implemented yet");
    }

    private List<Tile> getAdjacentTiles(int ox, int oy) {
        return getAdjacentTiles(ox, oy, (tile) -> true);
    }

    private List<Tile> getEmptyAdjacentTiles(int ox, int oy) {
        return getAdjacentTiles(ox, oy, Tile::isEmpty);
    }

    private List<Tile> getEnemyAdjacentTiles(Unit selectedUnit) {
        return getAdjacentTiles(selectedUnit.getX(), selectedUnit.getY(), (tile) -> {
            return Optional.ofNullable(tile.getUnit())
                    .filter(unit -> unit.getUnitType() == selectedUnit.getUnitType().getEnemyType())
                    .isPresent();
        });
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
            return Optional.of(board[x][y]);
        } else {
            return Optional.empty();
        }
    }
}
