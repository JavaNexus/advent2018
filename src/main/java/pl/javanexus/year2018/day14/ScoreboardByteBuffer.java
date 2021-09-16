package pl.javanexus.year2018.day14;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ScoreboardByteBuffer implements Scoreboard {

    public static final int BASE_OFFSET = 1;
    public static final int NUMBER_OF_RECIPES = 10;

    public static final int CAPACITY = 128 * 1024 * 1024;
    public static final int MAX_RECIPES_GENERATED_AT_A_TIME = 2;

    private final NextPoints nextPoints = new NextPoints();
    private final ByteBuffer points;

    private int firstElfIndex;
    private int secondElfIndex;

    public ScoreboardByteBuffer(byte[] initialPoints, int firstElfIndex, int secondElfIndex) {
        this.points = ByteBuffer.allocate(CAPACITY);
        addPoints(initialPoints);
        this.firstElfIndex = firstElfIndex;
        this.secondElfIndex = secondElfIndex;
    }

    @Override
    public int findRecipe(String recipe) {
        ByteBuffer recipeBytes = toByteBuffer(recipe);
        while (!doesLastRecipeMatch(recipeBytes)) {
            calculateNextPoints();
        }

        return points.position() - recipe.length();
    }

    public void saveRecipes() {
        int lastPosition = 20403327;
        while (points.position() < lastPosition) {
            calculateNextPoints();
        }

        String outputFileName = "D:\\Projekty\\Java\\advent2018\\src\\test\\resources\\recipe\\recipes.txt";
        try (FileWriter fileWriter = new FileWriter(outputFileName)) {
            points.position(0);
            while (points.position() < lastPosition) {
                fileWriter.write(Byte.toString(points.get()));
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }

    private ByteBuffer toByteBuffer(String recipe) {
        ByteBuffer recipeBytes = ByteBuffer.allocate(recipe.length());
        for (char c : recipe.toCharArray()) {
            recipeBytes.put((byte) (c - '0'));
        }
        return recipeBytes;
    }

    private void findPattern(String pattern) {
        for (int i = 0; i < points.position() - pattern.length(); i++) {
            if (pattern.equals(getBufferSubSetAsString(i, pattern.length()))) {
                System.out.println(i);
            }
        }
    }

    private String getBufferSubSetAsString(int from, int offset) {
        int lastPosition = points.position();
        points.position(from);

        StringBuilder builder = new StringBuilder();
        while (points.position() < from + offset) {
            builder.append(points.get());
        }
        points.position(lastPosition);

        return builder.toString();
    }

    private boolean doesLastRecipeMatch(ByteBuffer recipeBytes) {
        boolean isMatchFound = false;
        for (int i = 0; i < MAX_RECIPES_GENERATED_AT_A_TIME; i++) {//recipeBytes.position();
            isMatchFound |= doesRecipeMatchPoints(recipeBytes, i);
        }

        return isMatchFound;
    }

    private boolean doesRecipeMatchPoints(ByteBuffer recipeBytes, int offsetFromLastPoint) {
        byte[] lastRecipe = new byte[recipeBytes.position()];

        int lastPosition = points.position();
        points.position(lastPosition - offsetFromLastPoint - recipeBytes.position());
        points.get(lastRecipe);

        int i = 0;
        boolean isEqual = true;
        while (isEqual && i < lastRecipe.length) {
            isEqual &= (lastRecipe[i] == recipeBytes.get(i));
            i++;
        }
        points.position(lastPosition);

        return isEqual;
    }

    @Override
    public String getScore(int numberOfDiscardedRecipes) {
        int offset = numberOfDiscardedRecipes + NUMBER_OF_RECIPES;
        while (points.position() < offset) {
            calculateNextPoints();
        }

        return getBytesAsString(numberOfDiscardedRecipes, NUMBER_OF_RECIPES);

    }

    private String getBytesAsString(int offset, int length) {
        points.position(offset);

        StringBuilder score = new StringBuilder();
        for (int i = 0; i < length; i++) {
            score.append(points.get());
        }

        return score.toString();
    }

    private void calculateNextPoints() {
        byte[] next = nextPoints.get(getByte(firstElfIndex) + getByte(secondElfIndex));
        points.put(next);

        this.firstElfIndex = getNextIndex(firstElfIndex);
        this.secondElfIndex = getNextIndex(secondElfIndex);
    }

    private byte getByte(int index) {
        return points.get(index);
    }

    private int getNextIndex(int offset) {
        int index = offset + BASE_OFFSET + points.get(offset);
        return index < points.position() ? index : index - points.position();
    }

    private void addPoints(byte[] newPoints) {
        for (byte point : newPoints) {
            points.put(point);
        }
    }
}
