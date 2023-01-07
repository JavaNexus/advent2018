package pl.javanexus.year2022.day8;

import lombok.Value;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TreetopTreeHouse {

    private final Integer[][] forrest;

    public TreetopTreeHouse(Integer[][] forrest) {
        this.forrest = forrest;
    }

    public int countVisibleTrees() {
        Set<Tree> visibleTrees = new HashSet<>();

        int[] tallestInRow = new int[forrest.length];
        int[] tallestInCol = new int[forrest.length];

        Arrays.fill(tallestInRow, -1);
        Arrays.fill(tallestInCol, -1);
        for (int row = 0; row < forrest.length; row++) {
            for (int col = 0; col < forrest[row].length; col++) {
                if (isVisible(row, col, tallestInRow, tallestInCol)) {
                    visibleTrees.add(new Tree(row, col));
                }
            }
        }

        Arrays.fill(tallestInRow, -1);
        Arrays.fill(tallestInCol, -1);
        for (int row = forrest.length - 1; row >= 0; row--) {
            for (int col = forrest[row].length - 1; col >= 0; col--) {
                if (isVisible(row, col, tallestInRow, tallestInCol)) {
                    visibleTrees.add(new Tree(row, col));
                }
            }
        }

        return visibleTrees.size();
    }

    private boolean isVisible(int row, int col, int[] tallestInRow, int[] tallestInCol) {
        boolean isVisible = false;
        int treeHeight = forrest[row][col];
        if (treeHeight > tallestInRow[row]) {
            tallestInRow[row] = treeHeight;
            isVisible = true;
        }
        if (treeHeight > tallestInCol[col]) {
            tallestInCol[col] = treeHeight;
            isVisible = true;
        }

        return isVisible;
    }

    @Value
    private static class Tree {
        int row, col;
    }
}
