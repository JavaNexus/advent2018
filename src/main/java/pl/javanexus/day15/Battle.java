package pl.javanexus.day15;

import java.util.*;
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

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= board.length || y >= board.length) {
            String exceptionMessage = String.format("Point [%d, %d] is outside the board: %d", x, y, board.length);
            throw new IllegalArgumentException(exceptionMessage);
        }

        return board[x][y];
    }

    public void calculateDistance(Tile from, List<Tile> to) {
        final List<PathTile> unvisitedTiles = getPathTiles(from.getX(), from.getY());
        final Map<Tile, PathTile> mapping = unvisitedTiles.stream()
                .collect(Collectors.toMap(PathTile::getTile, pathTile -> pathTile));

        boolean hasVisitedAllReachableTile = false;
        while (!hasVisitedAllReachableTile) {
            unvisitedTiles.sort(Comparator.comparingInt(PathTile::getDistance));

            PathTile currentTile = unvisitedTiles.get(0);//get tile with min distance

            List<Tile> nextStepsOnPath = getEmptyAdjacentTiles(from.getX(), from.getY());
            nextStepsOnPath.stream()
                    .map(mapping::get)
                    .filter(pathTile -> !pathTile.isVisited())
                    .forEach(pathTile -> {
                        int newDistance = currentTile.getDistance() + 1;
                        if (newDistance < pathTile.getDistance()) {
                            pathTile.setDistance(newDistance);
                        }
                    });

            unvisitedTiles.remove(currentTile);
            currentTile.setVisited(true);

            hasVisitedAllReachableTile = nextStepsOnPath.isEmpty();
        }
    }

    private List<PathTile> getPathTiles(int srcX, int srcY) {
        final int maxDistance = board.length * board.length + 1;

        List<PathTile> unvisitedTiles = new ArrayList<>(board.length * board.length);
        iterateOverTracks(
                (x, y) -> {
                    Tile tile = board[x][y];
                    if (tile.isEmpty()) {
                        int distance = (x == srcX && y == srcY ? 0 : maxDistance);
                        unvisitedTiles.add(new PathTile(tile, distance));
                    }
                },
                (y) -> {});

        return unvisitedTiles;
    }

    private boolean isReachable(Tile from, Tile to) {
        // TODO: 2018-12-23 implement path finding
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
