package pl.javanexus.year2018.day14;

import java.util.*;
import java.util.stream.Collectors;

public class ScoreboardByteList implements Scoreboard {

    public static final int BASE_OFFSET = 1;
    public static final int NUMBER_OF_RECIPES = 10;

    private final NextPoints nextPoints = new NextPoints();
    private final List<Byte> points = new LinkedList<>();

    private int firstElfIndex;
    private int secondElfIndex;

    public ScoreboardByteList(byte[] initialPoints, int firstElfIndex, int secondElfIndex) {
        addPoints(initialPoints);
        this.firstElfIndex = firstElfIndex;
        this.secondElfIndex = secondElfIndex;
    }

    private void addPoints(byte[] newPoints) {
        for (byte point : newPoints) {
            points.add(point);
        }
    }

    @Override
    public int findRecipe(String recipe) {
        List<Byte> recipeBytes = Arrays.stream(recipe.split(""))
                .map(Byte::valueOf)
                .collect(Collectors.toList());

        while (!doesLastRecipeMatch(recipeBytes)) {
            calculateNextPoints();
        }

        return points.size() - recipeBytes.size();
    }

    private boolean doesLastRecipeMatch(List<Byte> recipeBytes) {
        return recipeBytes.equals(getLastRecipe(recipeBytes.size()));
    }

    private List<Byte> getLastRecipe(int recipeSize) {
        List<Byte> lastRecipe = points.subList(points.size() - recipeSize, points.size());
//        System.out.printf("%s\n", lastRecipe.stream().map(b -> b.toString()).collect(Collectors.joining()));
        return lastRecipe;
    }

    @Override
    public String getScore(int numberOfDiscardedRecipes) {
        int offset = numberOfDiscardedRecipes + NUMBER_OF_RECIPES;
        while (points.size() < offset) {
            calculateNextPoints();
//            System.out.printf("%s\n", points.stream().map(b -> b.toString()).collect(Collectors.joining()));
        }

        return points.subList(numberOfDiscardedRecipes, offset)
                .stream().map(Object::toString)
                .collect(Collectors.joining());
    }

    private byte[] calculateNextPoints() {
        byte[] newPoints = calculatePoints(points.get(firstElfIndex), points.get(secondElfIndex));
        addPoints(newPoints);

//        System.out.printf("%d,%d,%d\n", points.size(), firstElfIndex, secondElfIndex);
        this.firstElfIndex = getNextIndex(firstElfIndex);
        this.secondElfIndex = getNextIndex(secondElfIndex);

        return newPoints;
    }

    private int getNextIndex(int offset) {
        int index = offset + BASE_OFFSET + points.get(offset);
        return index < points.size() ? index : index - points.size();
    }

    public byte[] calculatePoints(byte first, byte second) {
        byte sum = (byte)(first + second);
        return nextPoints.get(sum);
    }
}
