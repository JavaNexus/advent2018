package pl.javanexus.year2019.day6;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    }
}
