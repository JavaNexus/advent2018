package pl.javanexus.year2020.day7;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class HandyHaversacks {

    private final Map<String, Bag> bags;

    public Collection<Bag> getParentBags(String bagColor) {
        Bag bag = bags.get(bagColor);
        if (bag == null) {
            throw new IllegalArgumentException(String.format("Bag %s not found", bagColor));
        }

        Set<Bag> parentBags = new HashSet<>();
        addParents(bag, parentBags);

        return parentBags;
    }

    private void addParents(Bag bag, Set<Bag> parentBags) {
        for (Content parent : bag.getParentBags()) {
            parentBags.add(parent.getBag());
            addParents(parent.getBag(), parentBags);
        }
    }
}
