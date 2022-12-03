package pl.javanexus.year2022.day3;

import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

import java.util.List;
import java.util.stream.Stream;

public class RucksackReorganization {

    public static final int GROUP_SIZE = 3;
    private final int[] emptyPriorities = new int['z' + 1];

    public int getSumOfDuplicateItemTypesPriorites(Stream<String> rucksacks) {
        return rucksacks.mapToInt(this::findItemTypePresentInBothCompartments).sum();
    }

    public int findGroupsBadges(Stream<String> rucksacks) {
        int sum = 0;
        UnmodifiableIterator<List<String>> iterator = Iterators.partition(rucksacks.iterator(), GROUP_SIZE);
        while (iterator.hasNext()) {
            sum += getGroupBadgePriority(iterator.next());
        }

        return sum;
    }

    private int getGroupBadgePriority(List<String> groupRucksacks) {
        int[] priorities = getPriorities();
        countItemTypes(groupRucksacks.get(0), priorities, 0);
        countItemTypes(groupRucksacks.get(1), priorities, 1);
        return getBadgeItemType(groupRucksacks.get(2), priorities);
    }

    private void countItemTypes(String rucksack, int[] priorities, int maxValue) {
        char[] items = rucksack.toCharArray();

        for (char item : items) {
            if (priorities[item] == maxValue) {
                priorities[item]++;
            }
        }
    }

    private int getBadgeItemType(String rucksack, int[] priorities) {
        char[] items = rucksack.toCharArray();

        for (char item : items) {
            if (priorities[item] == 2) {
                return getItemTypePriority(item);
            }
        }
        throw new IllegalStateException("Badge not found in group");
    }

    private int findItemTypePresentInBothCompartments(String rucksack) {
        char[] items = rucksack.toCharArray();
        int[] priorities = getPriorities();

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

    private int[] getPriorities() {
        int[] priorities = new int['z' + 1];
        System.arraycopy(emptyPriorities, 0, priorities, 0, emptyPriorities.length);

        return priorities;
    }

    /**
     * Lowercase item types a through z have priorities 1 through 26.
     * Uppercase item types A through Z have priorities 27 through 52.
     */
    private int getItemTypePriority(char itemType) {
        return itemType >= 'a' ? itemType - 'a' + 1 : itemType - 'A' + 27;
    }
}
