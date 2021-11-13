package pl.javanexus.year2020.day7;

import lombok.Value;

@Value
public class Content {

    int numberOfBags;
    Bag bag;

    @Override
    public String toString() {
        return "Content{" +
                "numberOfBags=" + numberOfBags +
                ", bag=" + bag.getColor() +
                '}';
    }
}
