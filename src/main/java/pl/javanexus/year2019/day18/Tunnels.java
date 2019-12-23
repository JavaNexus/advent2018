package pl.javanexus.year2019.day18;

import lombok.Data;
import lombok.Getter;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tunnels {

    public static final int INFINITY = 1000000;
    public static final int STEP = 1;

    enum TileType {
        ENTRANCE("@", true),
        EMPTY("\\.", true),
        WALL("#", false),
        KEY("[a-z]", true),
        DOOR("[A-Z]", false);

        public static TileType getType(String symbol) {
            return Arrays.stream(TileType.values())
                    .filter(tileType -> tileType.isMatching(symbol))
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("Unknown tile type: " + symbol));
        }

        @Getter
        private final boolean passable;

        private final String pattern;

        TileType(String pattern, boolean passable) {
            this.pattern = pattern;
            this.passable = passable;
        }

        private boolean isMatching(String symbol) {
            return symbol.matches(pattern);
        }
    }

    private final Tile[][] grid;
    private final List<Tile> tiles = new LinkedList<>();
    private final Map<String, Key> keys = new TreeMap();
    private final Map<String, Tile> doors = new TreeMap();
    private Key entrance;
    private int minDistance = INFINITY;

    public Tunnels(List<String> lines) {
        this.grid = parseInput(lines);
        calculateDistancesBetweenKeys();
    }

    public int collectKeys() {
        Map<String, Tile> remainingKeys = new TreeMap(keys);
        collectKeys(entrance, remainingKeys, 0);

        return minDistance;
    }

    private int collectKeys(Tile position, Map<String, Tile> remainingKeys, int totalDistance) {
        List<ReachableKey> reachableKeys = getReachableKeys(position, remainingKeys);
        if (reachableKeys.isEmpty()) {
            System.out.println("Collected all keys in: " + totalDistance + " steps");
            if (totalDistance < minDistance) {
                this.minDistance = totalDistance;
            }
        }

        for (ReachableKey reachableKey : reachableKeys) {
            String nextKey = reachableKey.getKey();
            Tile nextPosition = keys.get(nextKey);
            Map<String, Tile> nextRemainingKeys = getOtherRemainingKeys(reachableKey, remainingKeys);

            openDoor(nextKey);
            collectKeys(nextPosition, nextRemainingKeys, totalDistance + reachableKey.getDistance());
            closeDoor(nextKey);
        }

        return totalDistance;
    }

    private Map<String, Tile> getOtherRemainingKeys(ReachableKey currentKey, Map<String, Tile> remainingKeys) {
        Map<String, Tile> otherRemainingKeys = new HashMap<>(remainingKeys);
        otherRemainingKeys.remove(currentKey.getKey());

        return otherRemainingKeys;
    }

    private void openDoor(String key) {
        Tile door = doors.get(getDoorSymbol(key));
        if (door != null) {
            door.setType(TileType.EMPTY);
        }
    }

    private void closeDoor(String key) {
        Tile door = doors.get(getDoorSymbol(key));
        if (door != null) {
            door.setType(TileType.DOOR);
        }
    }

    private String getDoorSymbol(String keySymbol) {
        char door = (char) (keySymbol.charAt(0) + ('A' - 'a'));
        return String.valueOf(door);
    }

    private List<ReachableKey> getReachableKeys(Tile position, Map<String, Tile> remainingKeys) {
        List<ReachableKey> reachableKeys = new LinkedList<>();

        Map<String, Integer> distanceToEachKey = getDistanceToEachTile(position, remainingKeys.entrySet());
        for (Map.Entry<String, Integer> key : distanceToEachKey.entrySet()) {
            if (key.getValue() < INFINITY) {
                reachableKeys.add(new ReachableKey(key.getKey(), key.getValue()));
            }
        }

        return reachableKeys;
    }

    private Map<String, Integer> getDistanceToEachTile(Tile from, Set<Map.Entry<String, Tile>> destinations) {
        Map<String, Integer> distanceToEachKey = new HashMap<>();
        for (Map.Entry<String, Tile> destination : destinations) {
            int distance =
                    getDistance(from, destination.getValue(), neighbor -> neighbor.isPassable() && !neighbor.isVisited());
            System.out.println("Distance to: " + destination.getKey() + " is: " + distance);
            distanceToEachKey.put(destination.getKey(), distance);
        }

        return distanceToEachKey;
    }

    private Path getPathBetweenKeys(Tile from, Tile to) {
        final Predicate<Tile> tileFilter =
                tile -> tile.getType() == TileType.DOOR ? true : tile.isPassable();
        Path path = new Path(getDistance(from, to, tileFilter));

        Tile position = to;
        while (!position.equals(from)) {
            List<Tile> nextSteps =
                    getNextSteps(position, tileFilter);
            nextSteps.sort(Comparator.comparingInt(Tile::getDistance));

            position = nextSteps.get(0);//nextSteps.size() - 1
            if (position instanceof Key && !((Key)position).getSymbol().equals("@")) {
                path.addKey(((Key) position).getSymbol());
            }
        }

        return path;
    }

    private int getDistance(Tile from, Tile to, Predicate<Tile> filter) {
        List<Tile> unvisitedTiles = getUnvisitedTiles();

        Tile currentStep = from;
        currentStep.setDistance(0);
        do {
            unvisitedTiles.sort(Comparator.comparingInt(Tile::getDistance));
            currentStep = unvisitedTiles.get(0);

            List<Tile> nextSteps = getNextSteps(currentStep, filter);
            for (Tile nextStep : nextSteps) {
                int distanceThroughCurrentNode = currentStep.getDistance() + STEP;
                if (distanceThroughCurrentNode < nextStep.getDistance()) {
                    nextStep.setDistance(distanceThroughCurrentNode);
                }
            }
            currentStep.setVisited(true);
            unvisitedTiles.remove(currentStep);
        } while (!unvisitedTiles.isEmpty() && !currentStep.equals(to) && currentStep.getDistance() != INFINITY);

        return to.getDistance();
    }

    private List<Tile> getUnvisitedTiles() {
        List<Tile> unvisited = new LinkedList<>();
        unvisited.addAll(tiles);

        for (Tile tile : unvisited) {
            tile.setVisited(false);
            tile.setDistance(INFINITY);
        }

        return unvisited;
    }

    private List<Tile> getNextSteps(Tile tile, Predicate<Tile> filter) {
        return Stream.of(
                getTile(tile.getX() - 1, tile.getY()),
                getTile(tile.getX() + 1, tile.getY()),
                getTile(tile.getX(), tile.getY() - 1),
                getTile(tile.getX(), tile.getY() + 1))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(filter)
                .collect(Collectors.toList());
    }

    private Optional<Tile> getTile(int x, int y) {
        if (y < 0 || y >= grid.length) {
            return Optional.empty();
        }
        if (x < 0 || x >= grid[y].length) {
            return Optional.empty();
        }
        return Optional.of(grid[y][x]);
    }

    private Tile[][] parseInput(List<String> lines) {
        Tile[][] grid = new Tile[lines.size()][];
        for (int y = 0; y < grid.length; y++) {
            String[] symbols = lines.get(y).split("");
            grid[y] = new Tile[symbols.length];
            for (int x = 0; x < grid[y].length; x++) {
                Tile tile = createTile(x, y, symbols[x]);
                grid[y][x] = tile;
                tiles.add(tile);
            }
        }

        return grid;
    }

    private Tile createTile(int x, int y, String symbol) {
        TileType tileType = TileType.getType(symbol);

        Tile tile;
        switch (tileType) {
            case ENTRANCE:
                this.entrance = new Key(x, y, symbol);
                tile = entrance;
                break;
            case KEY:
                Key key = new Key(x, y, symbol);
                tile = key;
                keys.put(symbol, key);
                break;
            case DOOR:
                tile = new Tile(x, y, tileType);
                doors.put(symbol, tile);
                break;
            default:
                tile = new Tile(x, y, tileType);
                break;
        }

        return tile;
    }

    private void calculateDistancesBetweenKeys() {
        for (Map.Entry<String, Key> from : keys.entrySet()) {
            Path path = getPathBetweenKeys(entrance, from.getValue());
            entrance.addPath(from.getKey(), path);

            for (Map.Entry<String, Key> to : keys.entrySet()) {
                if (!from.getKey().equals(to.getKey())) {
                    path = getPathBetweenKeys(from.getValue(), to.getValue());
                    from.getValue().addPath(to.getKey(), path);
                }
            }
        }
    }

    @Data
    private static class ReachableKey {
        private final String key;
        private final int distance;
    }

    @Data
    private static class Tile {

        private final int x, y;

        private TileType type;
        private boolean visited;
        private int distance;

        public Tile() {
            this(-1, -1, TileType.EMPTY);
        }

        public Tile(int x, int y, TileType type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }

        public boolean isPassable() {
            return type.isPassable();
        }
    }

    @Data
    private static class Key extends Tile {

        private final String symbol;
        private final Map<String, Path> pathsToOtherKeys = new HashMap<>();

        public Key(int x, int y, String symbol) {
            super(x, y, TileType.KEY);
            this.symbol = symbol;
        }

        //int distance, Collection<String> requiredKeys
        public void addPath(String destination, Path path) {
            pathsToOtherKeys.put(destination, path);//new Path(distance, requiredKeys)
        }
    }

    @Data
    private static class Path {

        private final int distance;
        private final Set<String> requiredKeys = new HashSet<>();

        public void addKey(String key) {
            requiredKeys.add(key);
        }

        public void addKeys(Collection<String> keys) {
            requiredKeys.addAll(keys);
        }
    }
}
