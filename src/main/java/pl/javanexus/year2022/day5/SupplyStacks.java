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

    enum CraneType {
        CM_9000 {
            @Override
            public void moveCrates(Deque<String> from, Deque<String> to, int count) {
                while (count-- > 0) {
                    to.push(from.poll());
                }
            }
        },
        CM_9001 {
            @Override
            public void moveCrates(Deque<String> from, Deque<String> to, int count) {
                Deque<String> stack = new ArrayDeque<>();
                for (int i = count; i > 0; i--) {
                    stack.push(from.poll());
                }
                for (int i = count; i > 0; i--) {
                    to.push(stack.poll());
                }
            }
        },
        ;
        public abstract void moveCrates(Deque<String> from, Deque<String> to, int count);
    }

    private final Pattern stacksPattern = Pattern.compile("\\[([A-Z])\\]| {3} ?");
    private final Pattern movePattern = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");

    private final CraneType craneType;

    public SupplyStacks(CraneType craneType) {
        this.craneType = craneType;
    }

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
                craneType.moveCrates(stacks.get(from), stacks.get(to), count);
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
