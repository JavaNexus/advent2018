package pl.javanexus.year2022.day5;

import com.google.common.base.Strings;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class SupplyStacks {

    private final Pattern stacksPattern = Pattern.compile("\\[([A-Z])\\]| {3} ?");
    private final Pattern movePattern = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");

    public String rearrangeCrates(Stream<String> input) {
        Iterator<String> iterator = input.iterator();

        List<Deque<String>> stacks = parseStacks(iterator);
        parseMoves(stacks, iterator);

        return getTopCrates(stacks);
    }

    private List<Deque<String>> parseStacks(Iterator<String> iterator) {
        List<Deque<String>> stacks = new ArrayList<>();

        boolean hasParsedInitialState = false;
        do {
            if (iterator.hasNext()) {
                String line = iterator.next();
                if (Strings.isNullOrEmpty(line)) {
                    hasParsedInitialState = true;
                } else {
                    int stackId = 0;
                    Matcher matcher = stacksPattern.matcher(line);
                    while (matcher.find()) {
                        if (matcher.group(1) != null) {
                            getStack(stacks, stackId).addLast(matcher.group(1));
                        }
                        stackId++;
                    }
                }
            } else {
                throw new IllegalArgumentException("Input doesn't contain moves");
            }
        } while (!hasParsedInitialState);

        return stacks;
    }

    private void parseMoves(List<Deque<String>> stacks, Iterator<String> iterator) {
        while (iterator.hasNext()) {
            String line = iterator.next();
            Matcher matcher = movePattern.matcher(line);
            if (matcher.find()) {
                int count = Integer.parseInt(matcher.group(1));
                int from = Integer.parseInt(matcher.group(2)) - 1;
                int to = Integer.parseInt(matcher.group(3)) - 1;
                while (count-- > 0) {
                    stacks.get(to).push(stacks.get(from).poll());
                }
            } else {
                throw new IllegalArgumentException("Unexpected line: " + line);
            }
        }
    }

    private String getTopCrates(List<Deque<String>> stacks) {
        StringBuilder builder = new StringBuilder();

        for (Deque<String> stack : stacks) {
            builder.append(stack.peekFirst());
        }

        return builder.toString();
    }

    private Deque<String> getStack(List<Deque<String>> stacks, int stackId) {
        while (stacks.size() <= stackId) {
            stacks.add(new ArrayDeque<>());
        }

        return stacks.get(stackId);
    }
}
