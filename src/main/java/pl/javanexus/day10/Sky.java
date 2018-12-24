package pl.javanexus.day10;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Sky {

    private final List<Star> stars;
    private int time;

    private int numberOfUniqueX = -1;
    private int numberOfUniqueY = -1;

    public Sky(List<Star> stars) {
        this.stars = stars;
    }

    public void moveStarsByOneStep() {
        this.stars.forEach(Star::moveOneStep);
    }

    public boolean isAreaAfterNextStepSmaller() {
        Set<Integer> uniqueX = stars.stream().map(Star::getNextX).collect(Collectors.toSet());
        Set<Integer> uniqueY = stars.stream().map(Star::getNextY).collect(Collectors.toSet());
        time++;

        if ((numberOfUniqueX < 0 && numberOfUniqueY < 0)
                || (uniqueX.size() <= numberOfUniqueX || uniqueY.size() <= numberOfUniqueY)) {
            System.out.printf("X: %d --> %d, Y: %d --> %d\n",
                    numberOfUniqueX, uniqueX.size(),
                    numberOfUniqueY, uniqueY.size());

            this.numberOfUniqueX = uniqueX.size();
            this.numberOfUniqueY = uniqueY.size();

            return true;
        } else {
            return false;
        }
    }

    public int getTime() {
        return time - 1;
    }

    public String printStars() {
        return stars.stream()
                .map(star -> String.format("{x:%d, y:%d}", star.getX(), star.getY()))
                .collect(Collectors.joining(",\n", "var stars = [", "]"));
    }
}
