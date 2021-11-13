package pl.javanexus.year2020.day7;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RulesParser {

    private final Pattern bagPattern = Pattern.compile("([ a-z]+) bags contain");
    private final Pattern contentPattern = Pattern.compile("([0-9]+) ([ a-z]+) bags?");

    public Map<String, Bag> parseRules(List<String> rules) {
        Map<String, Bag> bags = new HashMap<>();
        for (String rule : rules) {
            Matcher matcher = bagPattern.matcher(rule);
            if (matcher.find()) {
                String color = matcher.group(1);
                Bag bag = getBag(color, bags);
                List<Content> contents = parseContent(rule, bags);
                for (Content content : contents) {
                    content.getBag().addParent(new Content(content.getNumberOfBags(), bag));
                }
                bag.addChildren(contents);
            } else {
                throw new IllegalArgumentException("Unknown rule format: " + rule);
            }
        }

        return bags;
    }

    List<Content> parseContent(String rule, Map<String, Bag> bags) {
        List<Content> contents = new LinkedList<>();

        Matcher matcher = contentPattern.matcher(rule);
        while (matcher.find()) {
            contents.add(new Content(
                    Integer.parseInt(matcher.group(1)),
                    getBag(matcher.group(2), bags)));
        }

        return contents;
    }

    private Bag getBag(String color, Map<String, Bag> bags) {
        return bags.computeIfAbsent(color, (bagColor) -> new Bag(bagColor));
    }
}
