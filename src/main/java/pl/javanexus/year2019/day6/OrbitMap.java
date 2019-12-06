package pl.javanexus.year2019.day6;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class OrbitMap {

    public static final String ROOT_ID = "COM";

    private Map<String, Planet> index = new HashMap<>();
    private Planet sun;

    /**
     * AAA)BBB means "BBB is in orbit around AAA"
     *
     * @param rows
     */
    public void readInput(List<String> rows) {
        for (String row : rows) {
            String[] planetIds = row.split("\\)");

            Planet planet = getPlanet(planetIds[0]);
            Planet moon = getPlanet(planetIds[1]);

            moon.setParent(planet);
            planet.addToOrbit(moon);

            if (planet.getId().equals(ROOT_ID)) {
                this.sun = planet;
            }
        }
    }

    private Planet getPlanet(String planetId) {
        return index.computeIfAbsent(planetId, id -> new Planet(id));
    }

    public int getTotalNumberOfOrbits() {
        return sun.getTotalNumberOfOrbits(-1);
    }

    public int getMinNumberOfOrbitalTransfers(String from, String to) {
        Planet source = index.get(from);
        Planet target = index.get(to);

        Path pathFromSourceToRoot = source.calculatePath(ROOT_ID);
        System.out.println(pathFromSourceToRoot);

        Path pathFromTargetToRoot = target.calculatePath(ROOT_ID);
        System.out.println(pathFromTargetToRoot);

        return pathFromSourceToRoot.findMinNumberOfTransitions(pathFromTargetToRoot);
    }

    @Data
    private class Transition {

        private final String planetId;
        private final int distanceFromRoot;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Transition that = (Transition) o;
            return Objects.equals(planetId, that.planetId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(planetId);
        }

        @Override
        public String toString() {
            return "{" + planetId + ", " + distanceFromRoot + "}";
        }
    }

    private class Path {

        private final List<String> planetsOnPath = new LinkedList<>();
        private final Map<String, Transition> transitionIndex = new HashMap<>();

        public void addTransition(Transition transition) {
            planetsOnPath.add(transition.getPlanetId());
            transitionIndex.put(transition.getPlanetId(), transition);
        }

        private Transition getTransition(String planetId) {
            return transitionIndex.get(planetId);
        }

        public int findMinNumberOfTransitions(Path pathFromTargetToRoot) {
            for (String planetId : planetsOnPath) {
                Transition otherTransition = pathFromTargetToRoot.getTransition(planetId);
                if (otherTransition != null) {
                    System.out.println("First common planet: " + planetId);
                    return getTransition(planetId).getDistanceFromRoot() + otherTransition.getDistanceFromRoot();
                }
            }

            throw new RuntimeException("No common planet found on paths");
        }

        @Override
        public String toString() {
            return "Path{" +
                    "planetsOnPath=" + planetsOnPath +
                    ", transitionIndex=" + transitionIndex +
                    '}';
        }
    }

    private class Planet {

        @Getter
        private final String id;

        private final Map<String, Planet> planetsInOrbit = new HashMap<>();

        @Getter @Setter
        private Planet parent;

        public Planet(String id) {
            this.id = id;
        }

        public void addToOrbit(Planet planet) {
            planetsInOrbit.put(planet.id, planet);
        }

        public int getTotalNumberOfOrbits(int numberOfOrbits) {
            numberOfOrbits++;

            int totalNumberOfOrbits = numberOfOrbits;
            for (Map.Entry<String, Planet> planet : planetsInOrbit.entrySet()) {
                totalNumberOfOrbits += planet.getValue().getTotalNumberOfOrbits(numberOfOrbits);
            }

            return totalNumberOfOrbits;
        }

        public Path calculatePath(String destinationId) {
            Path path = new Path();

            int distanceFromStart = 0;
            Planet planet = parent;
            do {
                Transition transition = new Transition(planet.getId(), distanceFromStart++);
                path.addTransition(transition);
                planet = planet.getParent();
            } while (planet != null && !planet.getId().equals(destinationId));

            return path;
        }
    }
}
