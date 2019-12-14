package pl.javanexus.year2019.day14;

import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Nanofactory {

    public static final Pattern PATTERN_REACTION = Pattern.compile("([ 0-9A-Z,]+) => ([ 0-9A-Z]+)");
    public static final Pattern PATTERN_CHEMICAL = Pattern.compile("([0-9]+) ([A-Z]+)");

    public static final String ORE = "ORE";
    public static final String FUEL = "FUEL";

    private final Map<String, Reaction> reactions = new HashMap<>();
    private final Map<String, Integer> leftovers = new HashMap<>();

    public void readInput(List<String> lines) {
        for (String line : lines) {
            Matcher matcher = PATTERN_REACTION.matcher(line);
            if (matcher.find()) {
                Reaction reaction = new Reaction(parseChemical(matcher.group(2)));
                for (String chemical : matcher.group(1).split(", ")) {
                    reaction.addIngredient(parseChemical(chemical));
                }
                reactions.put(reaction.getProductName(), reaction);
            } else {
                throw new RuntimeException("Unexpected line format: " + line);
            }
        }
    }

    public int calculateTotalNumberOfOreUnits() {
        return calculateNumberOfOreUnits(reactions.get(FUEL).getProduct());
    }

    /**
     * 48 HKGWZ => 1 FUEL
     * 177 ORE => 5 HKGWZ
     *
     * @param product
     */
    private int calculateNumberOfOreUnits(Chemical product) {
        int totalNumberOfOreUnits = 0;

        Reaction reaction = reactions.get(product.getName());
        for (Chemical ingredient : reaction.getIngredients()) {
            if (ingredient.isOre()) {
                totalNumberOfOreUnits += ingredient.getQuantity();
            } else {
                int numberOfOreUnitsRequired = calculateNumberOfOreUnits(ingredient);
                System.out.println("Take: " + numberOfOreUnitsRequired + " ORE to produce: " + ingredient.getName());
                totalNumberOfOreUnits += numberOfOreUnitsRequired;
            }
        }

        int numberOfReactions = getNumberOfReactions(reaction.getProduct().getQuantity(), product.getQuantity());
        return numberOfReactions * reaction.getProduct().getQuantity() * totalNumberOfOreUnits;
    }

    private int getNumberOfReactions(int numberOfUnitsProduced, int numberOfUnitsRequired) {
        return (int)Math.ceil((double) numberOfUnitsRequired / (double) numberOfUnitsProduced);
    }

    private Chemical parseChemical(String input) {
        Matcher matcher = PATTERN_CHEMICAL.matcher(input);
        if (matcher.find()) {
            return new Chemical(matcher.group(2), Integer.parseInt(matcher.group(1)));
        } else {
            throw new RuntimeException("Unexpected chemical formula: " + input);
        }
    }

    @Data
    public static class Chemical {

        private final String name;
        private final int quantity;

        public boolean isOre() {
            return name.equals(ORE);
        }
    }

    @Data
    public static class Reaction {

        private final List<Chemical> ingredients = new LinkedList<>();
        private final Chemical product;

        public void addIngredient(Chemical ingredient) {
            ingredients.add(ingredient);
        }

        public String getProductName() {
            return product.getName();
        }
    }
}
