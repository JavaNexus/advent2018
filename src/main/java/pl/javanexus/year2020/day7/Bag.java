package pl.javanexus.year2020.day7;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Data
public class Bag {

    private final String color;
    private final List<Content> childBags = new LinkedList<>();
    private final List<Content> parentBags = new LinkedList<>();

    public void addChild(Content content) {
        childBags.add(content);
    }

    public void addChildren(List<Content> contents) {
        childBags.addAll(contents);
    }

    public void addParent(Content parentBags) {
        this.parentBags.add(parentBags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bag bag = (Bag) o;
        return Objects.equals(color, bag.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}
