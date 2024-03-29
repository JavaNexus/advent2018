package pl.javanexus.year2018.day15;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Board {

    private final Tile[][] board;
    private final int maxDistance;
    private final int minDistance;

    public Board(Tile[][] board) {
        this.board = board;
        this.minDistance = 0;
        this.maxDistance = board.length * board.length + 1;
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

        while (!unvisitedTiles.isEmpty()) {
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
                    .sorted()
                    .findFirst().get();
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

    public List<Tile> getEmptyAdjacentTiles(int ox, int oy) {
        return getAdjacentTiles(ox, oy, Tile::isEmpty);
    }

    public List<Tile> getEnemyAdjacentTiles(Unit selectedUnit) {
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


    public void printMap() {
        List<Unit> unitsInTheRow = new LinkedList<>();
        iterateOverBoard(
                (x, y) -> {
                    Tile tile = getTile(y, x);
                    Unit unit = tile.getUnit();
                    if (unit != null) {
                        unitsInTheRow.add(unit);
                    }
                    System.out.printf("%s", tile.getSymbol());
                },
                (y) -> {
                    String units = unitsInTheRow.stream()
                            .map(unit -> String.format("%s(%d)", unit.getUnitType().name(), unit.getHp()))
                            .collect(Collectors.joining(", ", " ", " "));
                    unitsInTheRow.clear();
                    System.out.printf("%s\n", units);
                });
    }

    private void iterateOverBoard(BiConsumer<Integer, Integer> cellConsumer, Consumer<Integer> rowConsumer) {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                cellConsumer.accept(x, y);
            }
            rowConsumer.accept(x);
        }
    }
}
