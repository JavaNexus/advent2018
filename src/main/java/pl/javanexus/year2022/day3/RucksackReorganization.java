package pl.javanexus.year2022.day3;

import java.util.stream.Stream;

public class RucksackReorganization {

    private final int[] emptyPriorities = new int['z' + 1];

    public int getSumOfDuplicateItemTypesPriorites(Stream<String> rucksacks) {
        return rucksacks.mapToInt(this::findItemTypePresentInBothCompartments).sum();
    }

    private int findItemTypePresentInBothCompartments(String rucksack) {
        char[] items = rucksack.toCharArray();

        int[] priorities = new int['z' + 1];
        System.arraycopy(emptyPriorities, 0, priorities, 0, emptyPriorities.length);

        for (int i = 0; i < items.length / 2; i++) {
            priorities[items[i]]++;
        }
        for (int i = items.length / 2; i < items.length; i++) {
            if (priorities[items[i]] > 0) {
                return getItemTypePriority(items[i]);
            }
        }
        throw new IllegalStateException("No item type found in both compartments");
    }

    /**
     * Lowercase item types a through z have priorities 1 through 26.
     * Uppercase item types A through Z have priorities 27 through 52.
     */
    private int getItemTypePriority(char itemType) {
        return itemType >= 'a' ? itemType - 'a' + 1 : itemType - 'A' + 27;
    }
}
